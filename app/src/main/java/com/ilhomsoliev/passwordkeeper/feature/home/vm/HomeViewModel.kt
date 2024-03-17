package com.ilhomsoliev.passwordkeeper.feature.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.passwordkeeper.domain.ResponseUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.CheckIsFirstTimeUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.CheckMasterPasswordUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.GetWebsitePasswordByIdUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.GetWebsitesPasswordsUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.StoreMasterPasswordUseCase
import com.ilhomsoliev.passwordkeeper.feature.shared.model.WebsitePasswordUiModel
import com.ilhomsoliev.passwordkeeper.feature.shared.model.map
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.model.SnackbarMessage
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.model.UserMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getWebsitePasswordUseCase: GetWebsitesPasswordsUseCase,
    private val getWebsitePasswordByIdUseCase: GetWebsitePasswordByIdUseCase,
    private val checkMasterPasswordUseCase: CheckMasterPasswordUseCase,
    private val storeMasterPasswordUseCase: StoreMasterPasswordUseCase,
    private val checkIsFirstTimeUseCase: CheckIsFirstTimeUseCase,
) : ViewModel() {

    private val _dialogStatus = MutableStateFlow<HomeDialogStatus>(HomeDialogStatus.Closed)
    val dialogStatus: StateFlow<HomeDialogStatus> = _dialogStatus

    private val _list = MutableStateFlow<List<WebsitePasswordUiModel>>(emptyList())
    val list: StateFlow<List<WebsitePasswordUiModel>> = _list

    private val channel = Channel<HomeScreenEvents>()
    val flow = channel.receiveAsFlow()

    // Snackbar
    private val _snackbarMessage = MutableStateFlow<SnackbarMessage?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()


    init {
        getWebsitePasswordUseCase.invoke().onEach {
            _list.value = it.map { it.map() }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val response = checkIsFirstTimeUseCase.invoke()
            if (response)
                _dialogStatus.value = HomeDialogStatus.FirstPassword()
            else
                channel.send(HomeScreenEvents.AskMasterPasswordFirstTime)

        }

    }

    fun dismissSnackbar() = run { _snackbarMessage.value = null }

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = SnackbarMessage.from(
            userMessage = UserMessage.from(message),
        )
    }

    fun onConfirm() {
        viewModelScope.launch {
            when (val status = _dialogStatus.value) {
                is HomeDialogStatus.NewPassword -> {
                    val response = checkMasterPasswordUseCase.invoke(status.oldPassword)
                    if (response) {
                        val newPassword = status.newPassword
                        if (!isValidPassword(newPassword)) {
                            _dialogStatus.value =
                                status.copy(warning = DialogWarning.InvalidPassword)
                            return@launch
                        }
                        storeMasterPasswordUseCase.invoke(newPassword)
                        _dialogStatus.value = HomeDialogStatus.Closed
                    } else {
                        _dialogStatus.value = status.copy(warning = DialogWarning.IncorrectPassword)
                    }
                }

                is HomeDialogStatus.CheckPasswordOnEnteringDetail -> {
                    val response = checkMasterPasswordUseCase.invoke(status.password)
                    if (response) {
                        if (status.isForDetail) {
                            channel.send(HomeScreenEvents.OpenAddPasswordScreen(status.id))
                        } else {
                            showPassword(status.id)
                        }
                        _dialogStatus.value = HomeDialogStatus.Closed
                    } else {
                        _dialogStatus.value =
                            status.copy(warning = DialogWarning.IncorrectPassword)
                    }

                }

                is HomeDialogStatus.FirstPassword -> {
                    val password = status.password
                    if (!isValidPassword(password)) {
                        _dialogStatus.value = status.copy(warning = DialogWarning.InvalidPassword)
                        return@launch
                    }
                    storeMasterPasswordUseCase.invoke(password)
                    _dialogStatus.value = HomeDialogStatus.Closed
                }

                is HomeDialogStatus.CheckMasterPasswordOnStart -> {
                    val response = checkMasterPasswordUseCase.invoke(status.password)
                    if (response) {
                        _dialogStatus.value = HomeDialogStatus.Closed
                    } else {
                        _dialogStatus.value = status.copy(warning = DialogWarning.IncorrectPassword)
                    }
                }

                is HomeDialogStatus.Closed -> {}

            }
        }
    }

    fun changeNewPassword(value: String) {
        if (_dialogStatus.value is HomeDialogStatus.NewPassword)
            _dialogStatus.value =
                (_dialogStatus.value as HomeDialogStatus.NewPassword).copy(newPassword = value)
    }

    fun dismissDialog() {
        _dialogStatus.value = HomeDialogStatus.Closed
    }

    fun changeOldPassword(value: String) {
        if (_dialogStatus.value is HomeDialogStatus.NewPassword)
            _dialogStatus.value =
                (_dialogStatus.value as HomeDialogStatus.NewPassword).copy(oldPassword = value)
    }

    fun newPasswordDialogActivate() {
        _dialogStatus.value = HomeDialogStatus.NewPassword()
    }

    fun showPassword(id: Int?) {
        viewModelScope.launch {
            val newList = _list.value.toMutableList()
            val index = newList.indexOfFirst { it.id == id }
            if (!newList[index].isShown())
                when (val response = getWebsitePasswordByIdUseCase.invoke(id)) {
                    is ResponseUseCase.Success -> {
                        newList[index] = response.value.map()
                        _list.value = newList
                    }

                    is ResponseUseCase.Error -> {
                        showSnackbarMessage(response.message.message)
                    }
                }
            else {
                newList[index] = newList[index].copy(password = "")
                _list.value = newList
            }
        }
    }

    fun checkPasswordOnEnteringDetailDialogActivate(id: Int, isForDetail: Boolean) {
        _dialogStatus.value =
            HomeDialogStatus.CheckPasswordOnEnteringDetail(id, isForDetail = isForDetail)
    }

    fun checkMasterPasswordDialogActivate() {
        _dialogStatus.value = HomeDialogStatus.CheckMasterPasswordOnStart()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 3
    }

    fun changePassword(value: String) {
        when (_dialogStatus.value) {
            is HomeDialogStatus.CheckPasswordOnEnteringDetail -> {
                _dialogStatus.value =
                    (_dialogStatus.value as HomeDialogStatus.CheckPasswordOnEnteringDetail).copy(
                        password = value
                    )
            }

            is HomeDialogStatus.FirstPassword -> {
                _dialogStatus.value =
                    (_dialogStatus.value as HomeDialogStatus.FirstPassword).copy(password = value)
            }

            is HomeDialogStatus.CheckMasterPasswordOnStart -> {
                _dialogStatus.value =
                    (_dialogStatus.value as HomeDialogStatus.CheckMasterPasswordOnStart).copy(
                        password = value
                    )
            }

            else -> {}
        }
    }

}

sealed class HomeScreenEvents {
    data class OpenAddPasswordScreen(val id: Int?) : HomeScreenEvents()
    data object AskMasterPasswordFirstTime : HomeScreenEvents()
}

sealed class HomeDialogStatus(open val warning: DialogWarning = DialogWarning.None) {
    data object Closed : HomeDialogStatus()
    data class NewPassword(
        val oldPassword: String = "",
        val newPassword: String = "",
        override val warning: DialogWarning = DialogWarning.None
    ) :
        HomeDialogStatus(warning)

    data class FirstPassword(
        val password: String = "",
        override val warning: DialogWarning = DialogWarning.None
    ) : HomeDialogStatus(warning)

    data class CheckPasswordOnEnteringDetail(
        val id: Int?, val password: String = "", val isForDetail: Boolean,
        override val warning: DialogWarning = DialogWarning.None
    ) : HomeDialogStatus(warning)

    data class CheckMasterPasswordOnStart(
        val password: String = "",
        override val warning: DialogWarning = DialogWarning.None
    ) : HomeDialogStatus(warning)

}

sealed class DialogWarning(val message: String) {
    data object IncorrectPassword : DialogWarning("Incorrect Password")
    data object None : DialogWarning("")
    data object InvalidPassword :
        DialogWarning("Invalid Password, it should be more than 4 characters")
}
package com.ilhomsoliev.passwordkeeper.feature.add.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.passwordkeeper.domain.ResponseUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.DeleteWebsitePasswordUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.GetWebsitePasswordByIdUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.InsertWebsitePasswordUseCase
import com.ilhomsoliev.passwordkeeper.feature.shared.model.WebsitePasswordUiModel
import com.ilhomsoliev.passwordkeeper.feature.shared.model.map
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.model.SnackbarMessage
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.model.UserMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddPasswordViewModel(
    private val insertWebsitePasswordUseCase: InsertWebsitePasswordUseCase,
    private val getWebsitePasswordByIdUseCase: GetWebsitePasswordByIdUseCase,
    private val deleteWebsitePasswordUseCase: DeleteWebsitePasswordUseCase,
) : ViewModel() {

    private val _websitePassword =
        MutableStateFlow(WebsitePasswordUiModel(null, "", ""))
    val websitePassword: StateFlow<WebsitePasswordUiModel> = _websitePassword

    // Snackbar
    private val _snackbarMessage = MutableStateFlow<SnackbarMessage?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private val channel = Channel<AddScreenEvents>()
    val flow = channel.receiveAsFlow()

    fun dismissSnackbar() = run { _snackbarMessage.value = null }

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = SnackbarMessage.from(
            userMessage = UserMessage.from(message),
        )
    }

    fun loadPassword(id: Int?) {
        viewModelScope.launch {
            id?.let {
                val response = getWebsitePasswordByIdUseCase.invoke(it)
                response.let { websiteData ->
                    when (websiteData) {
                        is ResponseUseCase.Success -> {
                            _websitePassword.value = websiteData.value.map()
                        }

                        is ResponseUseCase.Error -> {
                            showSnackbarMessage("Some error")
                        }
                    }
                }
            }
        }
    }

    fun changeWebsite(value: String) {
        _websitePassword.value = _websitePassword.value.copy(website = value)
    }

    fun changePassword(value: String) {
        _websitePassword.value = _websitePassword.value.copy(password = value)
    }

    fun onSave() {
        viewModelScope.launch {
            when (val response =
                insertWebsitePasswordUseCase.invoke(_websitePassword.value.map())) {
                is ResponseUseCase.Success -> {
                    channel.send(AddScreenEvents.PopBack)
                }

                is ResponseUseCase.Error -> {
                    showSnackbarMessage(response.message.message)
                }
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            deleteWebsitePasswordUseCase.invoke(_websitePassword.value.map())
            channel.send(AddScreenEvents.PopBack)
        }
    }

}

sealed class AddScreenEvents {
    data object PopBack : AddScreenEvents()
    data object Error : AddScreenEvents()
}
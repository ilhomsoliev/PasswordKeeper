package com.ilhomsoliev.passwordkeeper.feature.home.presentation

import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.ilhomsoliev.passwordkeeper.feature.home.vm.HomeScreenEvents
import com.ilhomsoliev.passwordkeeper.feature.home.vm.HomeViewModel
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.SnackbarMessageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


@Composable
fun HomeScreen(
    vm: HomeViewModel,
    openAddPasswordScreen: (id: Int?) -> Unit,
) {
    val dialogStatus by vm.dialogStatus.collectAsState()
    val list by vm.list.collectAsState()
    val context = LocalContext.current
    val activity by lazy { context.getActivity() }
    val executor: Executor by lazy { ContextCompat.getMainExecutor(activity!!) }

    // Snackbar
    val snackbarMessage by vm.snackbarMessage.collectAsState()

    SnackbarMessageHandler(
        snackbarMessage = snackbarMessage,
        onDismissSnackbar = { vm.dismissSnackbar() }
    )

    fun showBiometricPrompt(
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errString: String) -> Unit
    ) {
        val biometricPrompt = BiometricPrompt(activity!!, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString.toString())
                }
            })
        biometricPrompt.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Input fingerprint")
                .setSubtitle("Authenticate via fingerprint to get password of website")
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(BIOMETRIC_STRONG)
                .build()
        )
    }

    LaunchedEffect(key1 = Unit, block = {
        vm.flow.collect {
            when (it) {
                is HomeScreenEvents.OpenAddPasswordScreen -> {
                    launch(Dispatchers.Main) {
                        openAddPasswordScreen.invoke(it.id)
                    }
                }

                HomeScreenEvents.AskMasterPasswordFirstTime -> {
                    showBiometricPrompt(
                        onSuccess = {},
                        onFailed = {
                            vm.showSnackbarMessage("Fingerprint failed")
                            vm.checkMasterPasswordDialogActivate()
                        },
                        onError = { errorCode, errString ->
                            vm.showSnackbarMessage(errString)
                            vm.checkMasterPasswordDialogActivate()
                        },
                    )
                }
            }
        }
    })

    HomeContent(
        state = HomeState(
            list = list,
            dialogStatus = dialogStatus,
        ),
        callback = object : HomeCallback {
            override fun onAddPasswordClick() {
                openAddPasswordScreen.invoke(null)
            }

            override fun onOpenPasswordClick(id: Int) {
                showBiometricPrompt(
                    onSuccess = {
                        openAddPasswordScreen.invoke(id)
                    },
                    onFailed = {
                        vm.showSnackbarMessage("Fingerprint failed")
                        vm.checkPasswordOnEnteringDetailDialogActivate(id, true)
                    },
                    onError = { errorCode, errString ->
                        vm.showSnackbarMessage(errString)
                        vm.checkPasswordOnEnteringDetailDialogActivate(id, true)
                    },
                )
            }

            override fun onConfirmClick() {
                vm.onConfirm()
            }

            override fun onNewPasswordChange(value: String) {
                vm.changeNewPassword(value)
            }

            override fun onPasswordChange(value: String) {
                vm.changePassword(value)
            }

            override fun dismissDialog() {
                vm.dismissDialog()
            }

            override fun onOldPasswordChange(value: String) {
                vm.changeOldPassword(value)
            }

            override fun onEditMasterPassowrdClick() {
                vm.newPasswordDialogActivate()
            }

            override fun omShowPassword(id: Int) {
                showBiometricPrompt(
                    onSuccess = {
                        vm.showPassword(id)
                    },
                    onFailed = {
                        vm.showSnackbarMessage("Fingerprint failed")
                        vm.checkPasswordOnEnteringDetailDialogActivate(id, false)
                    },
                    onError = { errorCode, errString ->
                        vm.showSnackbarMessage(errString)
                        vm.checkPasswordOnEnteringDetailDialogActivate(id, false)
                    },
                )
            }
        }
    )
}
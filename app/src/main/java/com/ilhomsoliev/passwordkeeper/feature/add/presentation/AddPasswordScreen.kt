package com.ilhomsoliev.passwordkeeper.feature.add.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ilhomsoliev.passwordkeeper.feature.add.vm.AddPasswordViewModel
import com.ilhomsoliev.passwordkeeper.feature.add.vm.AddScreenEvents
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.SnackbarMessageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddPasswordScreen(
    vm: AddPasswordViewModel,
    id: Int?,
    onBack: () -> Unit
) {
    val websitePassword by vm.websitePassword.collectAsState()
    val website = websitePassword.website
    val password = websitePassword.password

    // Snackbar
    val snackbarMessage by vm.snackbarMessage.collectAsState()

    SnackbarMessageHandler(
        snackbarMessage = snackbarMessage,
        onDismissSnackbar = { vm.dismissSnackbar() }
    )

    LaunchedEffect(key1 = Unit, block = {
        vm.loadPassword(id)
        vm.flow.collect {
            when (it) {
                is AddScreenEvents.PopBack -> {
                    launch(Dispatchers.Main) {
                        onBack()
                    }
                }

                AddScreenEvents.Error -> {
                    vm.showSnackbarMessage("Some error")
                }
            }
        }
    })

    AddPasswordContent(
        state = AddPasswordState(
            isEditing = id != null,
            website = website,
            password = password,
        ),
        callback = object : AddPasswordCallback {
            override fun onBack() {
                onBack()
            }

            override fun onDelete() {
                vm.onDelete()
            }

            override fun onSave() {
                vm.onSave()
            }

            override fun onWebsiteChange(value: String) {
                vm.changeWebsite(value)
            }

            override fun onPasswordChange(value: String) {
                vm.changePassword(value)

            }
        }
    )
}
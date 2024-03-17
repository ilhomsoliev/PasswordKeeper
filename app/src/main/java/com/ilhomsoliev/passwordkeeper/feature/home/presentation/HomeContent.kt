package com.ilhomsoliev.passwordkeeper.feature.home.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.passwordkeeper.R
import com.ilhomsoliev.passwordkeeper.feature.home.presentation.components.PasswordItem
import com.ilhomsoliev.passwordkeeper.feature.home.vm.HomeDialogStatus
import com.ilhomsoliev.passwordkeeper.feature.shared.dialogs.CheckPasswordOnEnteringDetailDialog
import com.ilhomsoliev.passwordkeeper.feature.shared.dialogs.PasswordOnStartDialog
import com.ilhomsoliev.passwordkeeper.feature.shared.dialogs.SetFirstPasswordDialog
import com.ilhomsoliev.passwordkeeper.feature.shared.dialogs.SetNewPasswordDialog
import com.ilhomsoliev.passwordkeeper.feature.shared.model.WebsitePasswordUiModel

data class HomeState(
    val list: List<WebsitePasswordUiModel>,
    val dialogStatus: HomeDialogStatus,
)

interface HomeCallback {
    fun onAddPasswordClick()
    fun onOpenPasswordClick(id: Int)
    fun onConfirmClick()
    fun onNewPasswordChange(value: String)
    fun onPasswordChange(value: String)
    fun dismissDialog()
    fun onOldPasswordChange(value: String)
    fun onEditMasterPassowrdClick()
    fun omShowPassword(id: Int)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
    callback: HomeCallback
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.password_keeper))
                }, actions = {
                    IconButton(onClick = {
                        callback.onEditMasterPassowrdClick()
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                callback.onAddPasswordClick()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            items(state.list) { website ->
                PasswordItem(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    image = website.websiteIconUrl,
                    title = website.website,
                    password = website.password,
                    isShown = website.password.isNotEmpty(),
                    onShowClick = {
                        website.id?.let { it1 -> callback.omShowPassword(it1) }
                    }, onClick = {
                        website.id?.let { it1 -> callback.onOpenPasswordClick(it1) }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }


    when (val dialogStatus = state.dialogStatus) {
        is HomeDialogStatus.FirstPassword -> {
            SetFirstPasswordDialog(
                password = dialogStatus.password,
                warning = dialogStatus.warning.message,
                onConfirmClick = {
                    callback.onConfirmClick()
                },
                onValueChange = {
                    callback.onPasswordChange(it)
                },
            )
        }

        is HomeDialogStatus.CheckPasswordOnEnteringDetail -> {
            CheckPasswordOnEnteringDetailDialog(
                password = dialogStatus.password,
                warning = dialogStatus.warning.message,
                onConfirmClick = {
                    callback.onConfirmClick()
                },
                onDismiss = {
                    callback.dismissDialog()
                },
                onValueChange = {
                    callback.onPasswordChange(it)
                },
            )
        }

        is HomeDialogStatus.NewPassword -> {
            SetNewPasswordDialog(
                newPassword = dialogStatus.newPassword,
                oldPassword = dialogStatus.oldPassword,
                warning = dialogStatus.warning.message,
                onConfirmClick = {
                    callback.onConfirmClick()
                },
                onDismiss = {
                    callback.dismissDialog()
                },
                onOldValueChange = {
                    callback.onOldPasswordChange(it)
                },
                onNewValueChange = {
                    callback.onNewPasswordChange(it)
                }
            )
        }

        is HomeDialogStatus.CheckMasterPasswordOnStart -> {
            PasswordOnStartDialog(
                password = dialogStatus.password,
                warning = dialogStatus.warning.message,
                onConfirmClick = {
                    callback.onConfirmClick()
                },
                onValueChange = {
                    callback.onPasswordChange(it)
                },
            )
        }

        HomeDialogStatus.Closed -> {}
    }

}
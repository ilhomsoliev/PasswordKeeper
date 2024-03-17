package com.ilhomsoliev.passwordkeeper.feature.shared.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.passwordkeeper.feature.shared.textfield.PasswordTextField

@Composable
fun SetNewPasswordDialog(
    modifier: Modifier = Modifier,
    newPassword: String,
    oldPassword: String,
    warning: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
    onOldValueChange: (String) -> Unit,
    onNewValueChange: (String) -> Unit
) {
    AlertDialog(modifier = modifier.fillMaxWidth(), onDismissRequest = {
        onDismiss()
    },
        dismissButton = {
            OutlinedButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirmClick()
            }) {
                Text(text = "Confirm")
            }
        }, title = {
            Text(text = "Set New Master-Password")
        }, text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                PasswordTextField(
                    hint = "Old password",
                    value = oldPassword,
                    onValueChange = {
                        onOldValueChange(it)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                PasswordTextField(
                    hint = "New password",
                    value = newPassword,
                    onValueChange = {
                        onNewValueChange(it)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = warning, color = Color.Red)
            }
        })
}
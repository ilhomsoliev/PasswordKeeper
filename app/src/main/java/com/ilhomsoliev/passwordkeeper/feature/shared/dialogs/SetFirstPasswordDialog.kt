package com.ilhomsoliev.passwordkeeper.feature.shared.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.passwordkeeper.feature.shared.textfield.PasswordTextField

@Composable
fun SetFirstPasswordDialog(
    modifier: Modifier = Modifier,
    password: String,
    warning: String,
    onConfirmClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    AlertDialog(modifier = modifier.fillMaxWidth(), onDismissRequest = {},
        confirmButton = {
            Button(onClick = {
                onConfirmClick()
            }) {
                Text(text = "Confirm")
            }
        }, title = {
            Text(text = "Set Master-Password")
        }, text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                PasswordTextField(
                    value = password,
                    onValueChange = {
                        onValueChange(it)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = warning, color = Color.Red)
            }
        })
}
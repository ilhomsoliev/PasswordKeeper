package com.ilhomsoliev.passwordkeeper.feature.shared.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ilhomsoliev.passwordkeeper.R

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.password),
    value: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier,
        value = value,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(text = hint)
        },
        singleLine = true,
        trailingIcon = {
            IconButton(
                modifier = Modifier,
                onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff, contentDescription = null
                )
            }
        }
    )

}
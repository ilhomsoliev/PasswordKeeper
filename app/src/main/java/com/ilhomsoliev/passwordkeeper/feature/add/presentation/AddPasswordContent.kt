package com.ilhomsoliev.passwordkeeper.feature.add.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.passwordkeeper.R
import com.ilhomsoliev.passwordkeeper.feature.shared.textfield.PasswordTextField

data class AddPasswordState(
    val isEditing: Boolean,
    val website: String,
    val password: String,

    )

interface AddPasswordCallback {
    fun onBack()
    fun onSave()
    fun onDelete()
    fun onWebsiteChange(value: String)
    fun onPasswordChange(value: String)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordContent(
    state: AddPasswordState,
    callback: AddPasswordCallback,
) {


    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = {
                    callback.onBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, title = {
                Text(text = if (state.isEditing) stringResource(R.string.edit_password) else stringResource(
                    R.string.add_password
                )
                )
            }, actions = {
                if (state.isEditing)
                    IconButton(onClick = {
                        callback.onDelete()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                IconButton(onClick = {
                    callback.onSave()
                }) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                }
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = state.website,
                    onValueChange = { callback.onWebsiteChange(it) },
                    placeholder = {
                        Text(text = stringResource(R.string.website))
                    }, singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                PasswordTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = state.password,
                    onValueChange = {
                        callback.onPasswordChange(it)
                    }
                )
            }
        }
    }

}
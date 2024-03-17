package com.ilhomsoliev.passwordkeeper.data.local.valueBased

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DataStoreManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preference")
        val encrypted_master_password_key = stringPreferencesKey("encrypted_master_password_key")
        val encrypted_master_password_size_key =
            intPreferencesKey("encrypted_master_password_size_key")
        val encrypted_master_password_cipher_key =
            stringPreferencesKey("encrypted_master_password_cipher_key")
        val encrypted_master_password_cipher_size_key =
            intPreferencesKey("encrypted_master_password_cipher_size_key")
    }

    suspend fun changeEncryptedMasterPassword(value: String) {
        context.dataStore.edit { preferences ->
            preferences[encrypted_master_password_key] = value
        }
    }

    suspend fun getEncryptedMasterPassword() = withContext(IO) {
        context.dataStore.data.first()[encrypted_master_password_key] ?: ""
    }

    suspend fun changeEncryptedMasterPasswordSize(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[encrypted_master_password_size_key] = value
        }
    }

    suspend fun getEncryptedMasterPasswordSize() = withContext(IO) {
        context.dataStore.data.first()[encrypted_master_password_size_key] ?: 0
    }

    suspend fun changeEncryptedMasterPasswordCipher(value: String) {
        context.dataStore.edit { preferences ->
            preferences[encrypted_master_password_cipher_key] = value
        }
    }

    suspend fun getEncryptedMasterPasswordCipher() = withContext(IO) {
        context.dataStore.data.first()[encrypted_master_password_cipher_key] ?: ""
    }

    suspend fun changeEncryptedMasterPasswordCipherSize(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[encrypted_master_password_cipher_size_key] = value
        }
    }

    suspend fun getEncryptedMasterPasswordCipherSize() = withContext(IO) {
        context.dataStore.data.first()[encrypted_master_password_cipher_size_key] ?: 0
    }

}
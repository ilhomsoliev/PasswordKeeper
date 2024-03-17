package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.core.CryptoManager
import com.ilhomsoliev.passwordkeeper.data.local.valueBased.DataStoreManager

class CheckMasterPasswordUseCase(
    private val dataStoreManager: DataStoreManager,
    private val cryptoManager: CryptoManager,
) {
    suspend operator fun invoke(passwordToTry: String) = run {
        try {
            val message = cryptoManager.decrypt(dataStoreManager.getEncryptedMasterPassword())
            message == passwordToTry
        } catch (e: Exception) {
            false
        }
    }
}
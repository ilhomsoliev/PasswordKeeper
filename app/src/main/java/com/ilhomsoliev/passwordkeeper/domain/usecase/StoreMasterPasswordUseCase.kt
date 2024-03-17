package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.core.CryptoManager
import com.ilhomsoliev.passwordkeeper.data.local.valueBased.DataStoreManager

class StoreMasterPasswordUseCase(
    private val dataStoreManager: DataStoreManager,
    private val cryptoManager: CryptoManager,
) {
    suspend operator fun invoke(password: String) {
        dataStoreManager.changeEncryptedMasterPassword(cryptoManager.encrypt(password))
    }
}
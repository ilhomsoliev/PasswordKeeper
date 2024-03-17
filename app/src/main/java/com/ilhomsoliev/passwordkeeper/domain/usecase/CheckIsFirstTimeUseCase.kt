package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.data.local.valueBased.DataStoreManager

class CheckIsFirstTimeUseCase(
    private val dataStoreManager: DataStoreManager,
) {
    suspend operator fun invoke() = dataStoreManager.getEncryptedMasterPassword().isEmpty()
}
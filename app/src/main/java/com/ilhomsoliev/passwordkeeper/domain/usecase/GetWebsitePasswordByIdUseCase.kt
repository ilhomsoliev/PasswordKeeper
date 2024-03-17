package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.core.CryptoManager
import com.ilhomsoliev.passwordkeeper.domain.ErrorUseCase
import com.ilhomsoliev.passwordkeeper.domain.ResponseUseCase
import com.ilhomsoliev.passwordkeeper.domain.model.WebsitePassword
import com.ilhomsoliev.passwordkeeper.domain.model.map
import com.ilhomsoliev.passwordkeeper.domain.repository.WebsitePasswordRepository

class GetWebsitePasswordByIdUseCase(
    private val repository: WebsitePasswordRepository,
    private val cryptoManager: CryptoManager
) {
    suspend operator fun invoke(id: Int?): ResponseUseCase<WebsitePassword> = run {
        if (id == null) return ResponseUseCase.Error(ErrorUseCase.OtherError("id can not be null"))
        val entity = repository.getWebsitePasswordById(id)
        if (entity?.encryptedPassword?.isEmpty() == true)
            return ResponseUseCase.Error(ErrorUseCase.Unknown)
        val decryptedPassword = cryptoManager.decrypt(entity!!.encryptedPassword)
        ResponseUseCase.Success(entity.map(decryptedPassword))
    }
}
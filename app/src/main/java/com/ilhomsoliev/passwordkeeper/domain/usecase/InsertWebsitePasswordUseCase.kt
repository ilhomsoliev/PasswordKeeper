package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.core.CryptoManager
import com.ilhomsoliev.passwordkeeper.domain.ErrorUseCase
import com.ilhomsoliev.passwordkeeper.domain.ResponseUseCase
import com.ilhomsoliev.passwordkeeper.domain.model.WebsitePassword
import com.ilhomsoliev.passwordkeeper.domain.model.map
import com.ilhomsoliev.passwordkeeper.domain.repository.WebsitePasswordRepository

class InsertWebsitePasswordUseCase(
    private val repository: WebsitePasswordRepository,
    private val cryptoManager: CryptoManager,
) {
    suspend operator fun invoke(websitePassword: WebsitePassword): ResponseUseCase<Unit> {
        val website = websitePassword.website
        val password = websitePassword.password

        if (!isValidWebsite(website) || !isValidPassword(password)) {
            return ResponseUseCase.Error(ErrorUseCase.OtherError("Invalid password or website"))
        }
        val key = cryptoManager.encrypt(password)
        websitePassword.encryptedPassword = key
        repository.insertWebsitePassword(websitePassword.map())
        return ResponseUseCase.Success(Unit)
    }

    private fun isValidWebsite(website: String): Boolean {
        return website.isNotEmpty()
    }

    private fun isValidPassword(password: String?): Boolean {
        return password?.isNotEmpty() == true
    }

}
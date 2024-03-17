package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.domain.model.WebsitePassword
import com.ilhomsoliev.passwordkeeper.domain.model.map
import com.ilhomsoliev.passwordkeeper.domain.repository.WebsitePasswordRepository

class DeleteWebsitePasswordUseCase(
    private val repository: WebsitePasswordRepository
) {
    suspend operator fun invoke(websitePassword: WebsitePassword) {
        repository.deleteWebsitePassword(websitePassword.map())
    }
}
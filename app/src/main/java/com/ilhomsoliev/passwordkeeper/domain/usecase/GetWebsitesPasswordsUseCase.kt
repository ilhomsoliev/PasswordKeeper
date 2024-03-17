package com.ilhomsoliev.passwordkeeper.domain.usecase

import com.ilhomsoliev.passwordkeeper.domain.model.map
import com.ilhomsoliev.passwordkeeper.domain.repository.WebsitePasswordRepository
import kotlinx.coroutines.flow.map

class GetWebsitesPasswordsUseCase(
    private val repository: WebsitePasswordRepository
) {
    operator fun invoke() =
        repository.getWebsitesPasswords().map { it.map { it.map("") } }

}
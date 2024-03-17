package com.ilhomsoliev.passwordkeeper.domain.repository

import com.ilhomsoliev.passwordkeeper.data.model.local.WebsitePasswordEntity
import kotlinx.coroutines.flow.Flow

interface WebsitePasswordRepository {
    suspend fun insertWebsitePassword(model: WebsitePasswordEntity)

    suspend fun deleteWebsitePassword(model: WebsitePasswordEntity)

    suspend fun getWebsitePasswordById(id: Int): WebsitePasswordEntity?

    fun getWebsitesPasswords(): Flow<List<WebsitePasswordEntity>>

}
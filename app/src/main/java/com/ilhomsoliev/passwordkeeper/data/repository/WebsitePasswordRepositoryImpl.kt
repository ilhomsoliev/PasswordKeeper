package com.ilhomsoliev.passwordkeeper.data.repository

import com.ilhomsoliev.passwordkeeper.data.local.dao.WebsitePasswordDao
import com.ilhomsoliev.passwordkeeper.data.model.local.WebsitePasswordEntity
import com.ilhomsoliev.passwordkeeper.domain.repository.WebsitePasswordRepository
import kotlinx.coroutines.flow.Flow

class WebsitePasswordRepositoryImpl(
    private val dao: WebsitePasswordDao,
) : WebsitePasswordRepository {

    override suspend fun insertWebsitePassword(model: WebsitePasswordEntity) {
        dao.insert(model)
    }

    override suspend fun deleteWebsitePassword(model: WebsitePasswordEntity) {
        dao.delete(model)
    }

    override suspend fun getWebsitePasswordById(id: Int): WebsitePasswordEntity? =
        dao.getItemById(id)

    override fun getWebsitesPasswords(): Flow<List<WebsitePasswordEntity>> =
        dao.getItems()

}
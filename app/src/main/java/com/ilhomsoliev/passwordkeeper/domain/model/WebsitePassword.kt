package com.ilhomsoliev.passwordkeeper.domain.model

import com.ilhomsoliev.passwordkeeper.data.model.local.WebsitePasswordEntity

data class WebsitePassword(
    val id: Int?,
    val website: String,
    var password: String,
    var encryptedPassword: String,
)

fun WebsitePasswordEntity.map(decryptedPassword: String): WebsitePassword = WebsitePassword(
    id = id,
    website = website,
    password = decryptedPassword,
    encryptedPassword = "",
)

fun WebsitePassword.map(): WebsitePasswordEntity = WebsitePasswordEntity(
    id = id,
    website = website,
    encryptedPassword = encryptedPassword,
)
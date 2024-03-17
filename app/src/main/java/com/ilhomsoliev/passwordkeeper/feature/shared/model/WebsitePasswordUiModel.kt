package com.ilhomsoliev.passwordkeeper.feature.shared.model

import com.ilhomsoliev.passwordkeeper.domain.model.WebsitePassword

data class WebsitePasswordUiModel(
    val id: Int?,
    val website: String,
    var password: String,
) {
    val websiteIconUrl: String = "https://www.google.com/s2/favicons?domain=$website&sz=128"

    fun isShown() = password.isNotEmpty()
}

fun WebsitePasswordUiModel.map(): WebsitePassword = WebsitePassword(
    id = id,
    website = website,
    password = password,
    encryptedPassword = "",
)

fun WebsitePassword.map(): WebsitePasswordUiModel = WebsitePasswordUiModel(
    id = id,
    website = website,
    password = password
)
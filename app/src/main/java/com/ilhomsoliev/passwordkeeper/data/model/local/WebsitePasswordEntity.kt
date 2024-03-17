package com.ilhomsoliev.passwordkeeper.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "website_password")
data class WebsitePasswordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val website: String,
    val encryptedPassword: String,
)
package com.ilhomsoliev.passwordkeeper.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ilhomsoliev.passwordkeeper.data.model.local.WebsitePasswordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WebsitePasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: WebsitePasswordEntity)

    @Update
    suspend fun update(data: WebsitePasswordEntity)

    @Delete
    suspend fun delete(data: WebsitePasswordEntity)

    @Query("SELECT * FROM `website_password`")
    fun getItems(): Flow<List<WebsitePasswordEntity>>

    @Query("SELECT * FROM `website_password` WHERE id = :id")
    suspend fun getItemById(id: Int): WebsitePasswordEntity?

}
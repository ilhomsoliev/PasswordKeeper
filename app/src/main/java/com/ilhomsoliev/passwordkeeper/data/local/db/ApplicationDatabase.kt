package com.ilhomsoliev.passwordkeeper.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ilhomsoliev.passwordkeeper.data.local.dao.WebsitePasswordDao
import com.ilhomsoliev.passwordkeeper.data.model.local.WebsitePasswordEntity

@Database(
    entities = [WebsitePasswordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun websitePasswordDao(): WebsitePasswordDao
}

fun getDatabaseInstance(context: Context) =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, "passwords_database")
        .fallbackToDestructiveMigration()
        .build()

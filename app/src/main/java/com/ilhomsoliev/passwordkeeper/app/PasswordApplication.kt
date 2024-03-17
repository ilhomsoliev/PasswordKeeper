package com.ilhomsoliev.passwordkeeper.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ilhomsoliev.passwordkeeper.app.di.dataStore
import com.ilhomsoliev.passwordkeeper.app.di.database
import com.ilhomsoliev.passwordkeeper.app.di.manager
import com.ilhomsoliev.passwordkeeper.app.di.repository
import com.ilhomsoliev.passwordkeeper.app.di.usecases
import com.ilhomsoliev.passwordkeeper.app.di.viewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PasswordApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PasswordApplication)
            modules(
                listOf(
                    dataStore(),
                    database(),
                    viewmodel(),
                    repository(),
                    manager(),
                    usecases(),
                )
            )
        }
    }

    override fun newImageLoader() = ImageLoader
        .Builder(this)
        .respectCacheHeaders(false)
        .diskCache {
            DiskCache.Builder()
                .directory(
                    this.cacheDir.resolve(
                        "image_cache"
                    )
                )
                .maxSizePercent(0.25)
                .build()
        }
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(0.25)
                .build()
        }
        .build()
}


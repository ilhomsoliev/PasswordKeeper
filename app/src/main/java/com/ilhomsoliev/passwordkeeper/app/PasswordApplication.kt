package com.ilhomsoliev.passwordkeeper.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ilhomsoliev.passwordkeeper.core.CryptoManager
import com.ilhomsoliev.passwordkeeper.data.local.dao.WebsitePasswordDao
import com.ilhomsoliev.passwordkeeper.data.local.db.ApplicationDatabase
import com.ilhomsoliev.passwordkeeper.data.local.db.getDatabaseInstance
import com.ilhomsoliev.passwordkeeper.data.local.valueBased.DataStoreManager
import com.ilhomsoliev.passwordkeeper.data.repository.WebsitePasswordRepositoryImpl
import com.ilhomsoliev.passwordkeeper.domain.repository.WebsitePasswordRepository
import com.ilhomsoliev.passwordkeeper.domain.usecase.CheckIsFirstTimeUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.CheckMasterPasswordUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.DeleteWebsitePasswordUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.GetWebsitePasswordByIdUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.GetWebsitesPasswordsUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.InsertWebsitePasswordUseCase
import com.ilhomsoliev.passwordkeeper.domain.usecase.StoreMasterPasswordUseCase
import com.ilhomsoliev.passwordkeeper.feature.add.vm.AddPasswordViewModel
import com.ilhomsoliev.passwordkeeper.feature.home.vm.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class PasswordApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        val viewModelModule = module {
            viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
            viewModel { AddPasswordViewModel(get(), get(), get()) }
        }
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PasswordApplication)
            modules(
                listOf(
                    module {
                        single<ApplicationDatabase> { getDatabaseInstance(androidContext()) }
                        single<WebsitePasswordDao> { get<ApplicationDatabase>().websitePasswordDao() }
                        single<WebsitePasswordRepository> { WebsitePasswordRepositoryImpl(get()) }
                        single { DataStoreManager(androidContext()) }
                        single { CryptoManager() }
                        single { InsertWebsitePasswordUseCase(get(), get()) }
                        single { GetWebsitesPasswordsUseCase(get()) }
                        single { GetWebsitePasswordByIdUseCase(get(), get()) }
                        single { DeleteWebsitePasswordUseCase(get()) }
                        single { CheckIsFirstTimeUseCase(get()) }
                        single { StoreMasterPasswordUseCase(get(), get()) }
                        single { CheckMasterPasswordUseCase(get(), get()) }

                    },
                    viewModelModule
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
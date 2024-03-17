package com.ilhomsoliev.passwordkeeper.app.di

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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun dataStore() = module {
    single { DataStoreManager(androidContext()) }
}

fun database() = module {
    single<ApplicationDatabase> { getDatabaseInstance(androidContext()) }
    single<WebsitePasswordDao> { get<ApplicationDatabase>().websitePasswordDao() }
}

fun viewmodel() = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { AddPasswordViewModel(get(), get(), get()) }
}

fun repository() = module {
    single<WebsitePasswordRepository> { WebsitePasswordRepositoryImpl(get()) }
}

fun manager() = module {
    single { CryptoManager() }
}

fun usecases() = module {
    single { InsertWebsitePasswordUseCase(get(), get()) }
    single { GetWebsitesPasswordsUseCase(get()) }
    single { GetWebsitePasswordByIdUseCase(get(), get()) }
    single { DeleteWebsitePasswordUseCase(get()) }
    single { CheckIsFirstTimeUseCase(get()) }
    single { StoreMasterPasswordUseCase(get(), get()) }
    single { CheckMasterPasswordUseCase(get(), get()) }

}
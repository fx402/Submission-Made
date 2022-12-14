package com.gilang.githubgilang

import android.app.Application
import com.gilang.core.di.databaseModule
import com.gilang.core.di.networkModule
import com.gilang.core.di.repositoryModule
import com.gilang.githubgilang.di.useCaseModule
import com.gilang.githubgilang.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}
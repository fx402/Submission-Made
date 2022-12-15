package com.gilang.core.di

import androidx.room.Room
import com.gilang.core.data.UserRepository
import com.gilang.core.data.source.local.LocalDataSource
import com.gilang.core.data.source.local.room.UserDatabase
import com.gilang.core.data.source.remote.RemoteDataSource
import com.gilang.core.data.source.remote.network.ApiService
import com.gilang.core.domain.repository.IUserRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val API_KEY = "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"
private const val BASE_URL = "https://api.github.com"

val databaseModule = module {
    factory {
        get<UserDatabase>().userDao()
    }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("gilang".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java,"User_database.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory).build()
    }
}

val networkModule = module {
    single {
        val hostName = "api.github.com"
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", API_KEY)
                val request = requestBuilder.build()
                it.proceed(request)
            }.connectTimeout(1,TimeUnit.MINUTES)
            .readTimeout(1,TimeUnit.MINUTES)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single <IUserRepository>{
        UserRepository(
            get(),
            get()
        )
    }
}
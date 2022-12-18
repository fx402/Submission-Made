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
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val API_KEY = "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"
private const val BASE_URL = "https://api.github.com"
//1UPHAdcUbUoOcd5rDTD/0oMSnngCU6YzXzpByO4CCp4=
val databaseModule = module {
    factory {
        get<UserDatabase>().userDao()
    }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("github".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java,"User_database.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }
}

val networkModule = module {
    single {
        val hostName = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostName,"sha256/uyPYgclc5Jt69vKu92vci6etcBDY8UNTyrHQZJpVoZY=")
            .add(hostName,"sha256/e0IRz5Tio3GA1Xs4fUVWmH1xHDiH2dMbVtCBSkOIdqM=")
            .add(hostName,"sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", API_KEY)
                val request = requestBuilder.build()
                it.proceed(request)
            }.connectTimeout(1,TimeUnit.MINUTES)
            .readTimeout(1,TimeUnit.MINUTES)
            .certificatePinner(certificatePinner)
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
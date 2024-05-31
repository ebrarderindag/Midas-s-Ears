package com.example.midasproject.di


import com.example.midasproject.data.repository.MidasRepository
import com.example.midasproject.data.service.MidasService
import com.example.midasproject.domain.Constant.BASE_URL_FOR_LOGIN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import javax.inject.Singleton
import kotlin.text.Typography.dagger

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    val inceptor = HttpLoggingInterceptor()
    val inceptorLogging= inceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(inceptorLogging).build()

    @Singleton
    @Provides
    fun providePlannerRepository(
        api : MidasService
    ) = MidasRepository(api)

    @Provides
    @Singleton
    fun providePlannerApi() : MidasService{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_FOR_LOGIN)
            .client(client)
            .build()
            .create(MidasService::class.java)
    }

}
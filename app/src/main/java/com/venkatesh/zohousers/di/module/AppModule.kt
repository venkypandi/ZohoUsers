package com.venkatesh.zohousers.di.module

import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.utils.Constants
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : RandomUserApi {
        return retrofit.create(RandomUserApi::class.java)
    }
}
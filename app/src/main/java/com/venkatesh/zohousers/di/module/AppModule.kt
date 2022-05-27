package com.venkatesh.zohousers.di.module

import android.content.Context
import androidx.room.Room
import com.venkatesh.zohousers.data.local.dao.RemoteKeysDao
import com.venkatesh.zohousers.data.local.dao.UserDao
import com.venkatesh.zohousers.data.local.database.UserDatabase
import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : RandomUserApi {
        return retrofit.create(RandomUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            "user_database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(userDatabase: UserDatabase): RemoteKeysDao {
        return userDatabase.remoteKeysDao()
    }
}
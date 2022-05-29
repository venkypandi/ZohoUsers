package com.venkatesh.zohousers.data.repository.user

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.venkatesh.zohousers.data.local.dao.UserDao
import com.venkatesh.zohousers.data.local.database.UserDatabase
import com.venkatesh.zohousers.data.paging.UserRemoteMediator
import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.data.remote.model.Result
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val randomUserApi: RandomUserApi,
    private val userDao: UserDao,
    private val userDatabase: UserDatabase): UserDataSource {

    override fun getUserList(): LiveData<PagingData<Result>> {
        val pagingSourceFactory = { userDatabase.userDao().getAllUsers() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 25),
            remoteMediator = UserRemoteMediator(
                "query",
                randomUserApi,
                userDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    override fun searchUsers(query: String): LiveData<PagingData<Result>> {
        val pagingSourceFactory = { userDatabase.userDao().usersByName(query) }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 25),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    override fun getUserByEmail(email: String): Result {
        return userDao.getUserByEmail(email)
    }


}
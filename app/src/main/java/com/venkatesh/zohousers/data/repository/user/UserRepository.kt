package com.venkatesh.zohousers.data.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.venkatesh.zohousers.data.local.dao.UserDao
import com.venkatesh.zohousers.data.local.database.UserDatabase
import com.venkatesh.zohousers.data.paging.UserRemoteMediator
import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.data.remote.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val randomUserApi: RandomUserApi,
    private val userDao: UserDao,
    private val userDatabase: UserDatabase): UserDataSource {

    //    override fun getUserList()= Pager(
//        config = PagingConfig(pageSize = 25),
//        pagingSourceFactory = {UsersPagingSource(randomUserApi)}
//    ).liveData
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

    override fun searchUsers(query: String): Flow<PagingData<Result>> {
        val pagingSourceFactory = { userDatabase.userDao().usersByName(query) }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 25),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getUserByEmail(email: String): Result {
        Log.d("userdetails1", "onCreateView: ${userDao.getUserByEmail(email)}")
        return userDao.getUserByEmail(email)
    }


}
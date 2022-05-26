package com.venkatesh.zohousers.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.venkatesh.zohousers.data.paging.UsersPagingSource
import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.utils.Resource
import javax.inject.Inject

class UserRepository @Inject constructor(private val randomUserApi: RandomUserApi):UserDataSource {

    override suspend fun getUserList()= Pager(
        config = PagingConfig(pageSize = 25),
        pagingSourceFactory = {UsersPagingSource(randomUserApi)}
    ).liveData
}
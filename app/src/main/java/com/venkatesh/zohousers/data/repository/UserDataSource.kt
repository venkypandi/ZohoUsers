package com.venkatesh.zohousers.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.utils.Resource

interface UserDataSource {

    suspend fun getUserList():LiveData<PagingData<Result>>

}
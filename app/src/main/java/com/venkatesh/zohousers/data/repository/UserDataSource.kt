package com.venkatesh.zohousers.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.venkatesh.zohousers.data.remote.model.Result
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun getUserList(): LiveData<PagingData<Result>>

}
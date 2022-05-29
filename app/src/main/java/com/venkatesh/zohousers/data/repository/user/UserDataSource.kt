package com.venkatesh.zohousers.data.repository.user

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.venkatesh.zohousers.data.remote.model.Result

interface UserDataSource {

    fun getUserList(): LiveData<PagingData<Result>>

    fun searchUsers(query:String): LiveData<PagingData<Result>>

    fun getUserByEmail(email:String): Result

}
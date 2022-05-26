package com.venkatesh.zohousers.data.remote.api

import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.data.remote.model.UserResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {

    @GET("/api")
    suspend fun getRandomUsers(@Query("page") page:Int,@Query("results") results:Int):UserResponseModel

}
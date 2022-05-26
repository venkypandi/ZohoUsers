package com.venkatesh.zohousers.data.remote.api

import com.venkatesh.zohousers.data.remote.model.UserResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/weather")
    suspend fun getWeatherData(@Query("key") key:String, @Query("q") query:String,@Query("aqi") aqi:String): UserResponseModel
}
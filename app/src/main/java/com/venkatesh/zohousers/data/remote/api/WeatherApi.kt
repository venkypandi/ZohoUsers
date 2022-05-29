package com.venkatesh.zohousers.data.remote.api

import com.venkatesh.zohousers.data.remote.model.WeatherResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("/v1/current.json")
    suspend fun getWeatherData(@Query("key") key:String, @Query("q") query:String,@Query("aqi") aqi:String): Response<WeatherResponseModel>
}
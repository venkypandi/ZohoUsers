package com.venkatesh.zohousers.data.repository.weather

import com.venkatesh.zohousers.data.remote.model.WeatherResponseModel
import retrofit2.Response

interface WeatherDataSource {

    suspend fun getWeatherInfo(query:String)
}
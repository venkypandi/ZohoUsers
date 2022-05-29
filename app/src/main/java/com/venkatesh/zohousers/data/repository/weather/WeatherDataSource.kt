package com.venkatesh.zohousers.data.repository.weather

import com.venkatesh.zohousers.data.remote.model.Result


interface WeatherDataSource {

    suspend fun getWeatherInfo(query:String)

    fun getAllLocalUsers():List<Result>
}
package com.venkatesh.zohousers.data.repository.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.venkatesh.zohousers.BuildConfig
import com.venkatesh.zohousers.data.remote.api.WeatherApi
import com.venkatesh.zohousers.data.remote.model.WeatherResponseModel
import com.venkatesh.zohousers.utils.Resource
import javax.inject.Inject


class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) : WeatherDataSource {

    private var _weatherResponse = MutableLiveData<Resource<WeatherResponseModel>>()
    val weatherResponse: LiveData<Resource<WeatherResponseModel>> = _weatherResponse

    override suspend fun getWeatherInfo(query: String) {
        _weatherResponse.value = Resource.loading(null)
        val response = weatherApi.getWeatherData(
            BuildConfig.API_KEY,
            query,
            "yes"
        )
        if (response.isSuccessful) {
            _weatherResponse.value = Resource.success(response.body())
        } else {
            Log.d("weatherReport", response.raw().toString())
            _weatherResponse.value = Resource.error(response.errorBody().toString(),null)
        }
    }
}
package com.venkatesh.zohousers.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkatesh.zohousers.data.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    val weatherResponse = repository.weatherResponse

    fun getWeatherInfo(query:String){
        viewModelScope.launch {
            repository.getWeatherInfo(query)
        }
    }
}
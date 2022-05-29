package com.venkatesh.zohousers.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.data.repository.weather.WeatherRepository
import com.venkatesh.zohousers.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(private val weatherRepository: WeatherRepository):ViewModel(){

    private var _weatherDetails = MutableLiveData<Resource<List<Result>>>()
    val weatherDetails: LiveData<Resource<List<Result>>> = _weatherDetails

    fun getAllLocalUsers(){
        _weatherDetails.value = Resource.loading(null)
        viewModelScope.launch {
            _weatherDetails.value =  Resource.success(weatherRepository.getAllLocalUsers())
            Log.d("userdetails", _weatherDetails.value.toString())
        }

    }

    val weatherResponse = weatherRepository.weatherResponse

    fun getWeatherInfo(query:String){
        viewModelScope.launch {
            weatherRepository.getWeatherInfo(query)
        }
    }
}
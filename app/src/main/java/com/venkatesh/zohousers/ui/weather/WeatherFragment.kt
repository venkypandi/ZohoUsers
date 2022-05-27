package com.venkatesh.zohousers.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.venkatesh.zohousers.databinding.FragmentWeatherBinding
import com.venkatesh.zohousers.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel by viewModels<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        weatherViewModel.getWeatherInfo("kovilpatti")
        weatherViewModel.weatherResponse.observe(viewLifecycleOwner){
            if(it!=null){
                when(it.status){
                    Status.SUCCESS->{
                        Log.d("weatherReport", it.data.toString())
                    }
                    Status.LOADING->{

                    }
                    Status.ERROR->{
                        Log.d("weatherReport", "error")

                    }
                }
            }
        }

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
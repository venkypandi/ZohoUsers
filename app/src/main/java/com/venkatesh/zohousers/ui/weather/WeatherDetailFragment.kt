package com.venkatesh.zohousers.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.databinding.FragmentWeatherDetailBinding
import com.venkatesh.zohousers.ui.adapters.UserWeatherAdapter
import com.venkatesh.zohousers.utils.DateUtils.Companion.getWeatherDateTime
import com.venkatesh.zohousers.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailFragment : Fragment() {

    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel by viewModels<WeatherDetailViewModel>()
    private lateinit var userAdapter:UserWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherDetailBinding.inflate(inflater,container,false)

        weatherViewModel.getAllLocalUsers()
        weatherViewModel.weatherDetails.observe(viewLifecycleOwner){
            if(it!=null){
                when(it.status){
                    Status.SUCCESS->{
                        Log.d("weatherReport", it.data.toString())
                        if(it.data!=null){
                            userAdapter = UserWeatherAdapter(
                                requireContext(),
                                it.data,
                            ){data:Result->
                                listItemClicked(data)
                            }
                            weatherViewModel.getWeatherInfo(
                                "${it.data[0].location.coordinates.latitude},${it.data[0].location.coordinates.longitude}")

                            binding.apply {
                                rvUsers.adapter=userAdapter
                                progressBar.visibility = View.GONE
                                clHeader.visibility = View.VISIBLE
                            }
                        }

                    }
                    Status.LOADING->{
                        binding.apply {
                            progressBar.visibility = View.VISIBLE
                            clHeader.visibility = View.GONE
                        }
                        Log.d("weatherReport", "onCreateView: loading")
                    }
                }
            }
        }

        weatherViewModel.weatherResponse.observe(viewLifecycleOwner){
            if(it!=null){
                when(it.status){
                    Status.SUCCESS->{
                        Log.d("weatherReport", it.data.toString())
                        binding.apply {
                            if (it.data?.current?.isDay == 1){
                                with(it.data?.current?.condition.text.lowercase()){
                                    when{
                                        contains("clear")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.clear))
                                        }
                                        contains("sunny")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.sunny))
                                        }
                                        contains("snow")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.day_snow))
                                        }
                                        contains("rain")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.rain_day))
                                        }
                                        contains("cloudy")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.sunny))
                                        }
                                        else->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.morning))
                                        }
                                    }
                                }
                            }else{
                                with(it.data!!.current.condition.text.lowercase()){
                                    when{
                                        contains("clear")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.night))
                                        }
                                        contains("rain")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.night_rain))
                                        }
                                        contains("snow")->{
                                            ivGallery.setImageDrawable(
                                                ContextCompat.getDrawable(requireContext(),
                                                    R.drawable.night_snow))
                                        }else->{
                                        ivGallery.setImageDrawable(
                                            ContextCompat.getDrawable(requireContext(),
                                                R.drawable.night))
                                        }
                                    }
                                }
                            }
                            tvCityName.text = it.data?.location?.name
                            tvCondition.text = it.data?.current?.condition?.text
                            tvDegree.text = "${it.data?.current?.tempC}\u00ba"
                            tvAqr1.text = it.data?.current?.airQuality?.co.toString()
                            tvAqr2.text = it.data?.current?.airQuality?.no2.toString()
                            tvAqr3.text = it.data?.current?.airQuality?.o3.toString()
                            tvAqr4.text = it.data?.current?.airQuality?.so2.toString()
                            tvCurrentTime.text=it.data?.location?.localtime?.getWeatherDateTime()
                            Glide.with(requireContext())
                                .load("https:/${it.data?.current?.condition?.icon}")
                                .placeholder(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_robot))
                                .into(binding.ivIcon)
                        }
                        binding.progressBar.visibility = View.GONE
                        switchView(View.VISIBLE)
                    }
                    Status.LOADING->{
                        binding.progressBar.visibility = View.VISIBLE
                        switchView(View.INVISIBLE)
                        binding.tvError.visibility = View.GONE

                    }
                    Status.ERROR->{
                        Log.d("weatherReport", "error")
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE

                    }
                }
            }
        }

        binding.ivNavbar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun listItemClicked(data: Result) {
        weatherViewModel.getWeatherInfo("${data.location.coordinates.latitude},${data.location.coordinates.longitude}")
    }

    private fun switchView(visibility:Int){
        binding.apply {
            tvCityName.visibility = visibility
            tvAirQuality.visibility = visibility
            tvAq1.visibility = visibility
            tvAq2.visibility = visibility
            tvAq3.visibility = visibility
            tvAq4.visibility = visibility
            tvAqr1.visibility = visibility
            tvAqr2.visibility = visibility
            tvAqr3.visibility = visibility
            tvAqr4.visibility = visibility
            viewLine.visibility = visibility
            ivIcon.visibility = visibility
            tvCondition.visibility =visibility
            tvCurrentTime.visibility = visibility
            tvDegree.visibility = visibility
        }
    }

}
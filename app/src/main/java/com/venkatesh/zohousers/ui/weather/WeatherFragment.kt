package com.venkatesh.zohousers.ui.weather

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.venkatesh.zohousers.MainActivity
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.databinding.FragmentWeatherBinding
import com.venkatesh.zohousers.utils.DateUtils.Companion.getWeatherDateTime
import com.venkatesh.zohousers.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var permissionRequest : ActivityResultLauncher<Array<String>>
    private var settingsClient: SettingsClient? = null
    private lateinit var locationSetting: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private var gpsState = MutableLiveData<Boolean?>()

    private val weatherViewModel by viewModels<WeatherViewModel>()

    companion object {

        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        binding.ivNavbar.setOnClickListener {
            MainActivity.mainActivity?.showNavDrawer()
        }

        weatherViewModel.weatherResponse.observe(viewLifecycleOwner){
            if(it!=null){
                when(it.status){
                    Status.SUCCESS->{
                        Log.d("weatherReport", it.data.toString())
                        binding.apply {
                            if (it.data?.current?.isDay == 1){
                                with(it.data?.current?.condition.text){
                                    when{
                                        contains("clear")->{
                                            ivGallery.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.clear))
                                        }
                                        contains("sunny")->{
                                            ivGallery.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.sunny))
                                        }
                                        contains("snow")->{
                                            ivGallery.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.day_snow))
                                        }
                                    }
                                }
                            }else{
                                with(it.data!!.current.condition.text){
                                    when{
                                        contains("clear")->{
                                            ivGallery.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.night))
                                        }
                                        contains("rain")->{
                                            ivGallery.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.night_rain))
                                        }
                                        contains("snow")->{
                                            ivGallery.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.night_snow))
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
                        binding.clHeader.visibility = View.VISIBLE
                    }
                    Status.LOADING->{
                        binding.progressBar.visibility = View.VISIBLE
                        binding.clHeader.visibility = View.GONE

                    }
                    Status.ERROR->{
                        Log.d("weatherReport", "error")
                        binding.progressBar.visibility = View.GONE

                    }
                }
            }
        }

        binding.ivUsers.setOnClickListener {
            val directions = WeatherFragmentDirections.actionNavGalleryToWeatherDetailFragment()
            findNavController().navigate(directions)
        }
        getGpsLocation()

        return _binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationSetting = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("locationSetting: ", "true")
                    lifecycleScope.launch {
                        delay(5000)
                        gpsState.value = true
                    }

                }
                Activity.RESULT_CANCELED -> {
                    Log.d("locationSetting: ", "false")
                    gpsState.value = false
                }
            }
        }
        registerPermissionRequest()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun registerPermissionRequest(){
        var permissionCount = 0
        permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if(it.value){
                    permissionCount++
                }
            }
            if(permissionCount == 2){
                getWeatherLocation()
            }
        }
    }

    private fun getWeatherLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerPermissionRequest()
            return
        }
        Log.d("weatherreport", "getWeatherLocation: 1")
        getCurrentLocationState()
        if(isLocationEnabled()){
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location:Location = it.result
                weatherViewModel.getWeatherInfo("${location.latitude},${location.longitude}")
                Log.d("weatherfragment", "getCurrentLocation: ${location.latitude}.${location.longitude}")
            }
        }else{
            Toast.makeText(requireContext(), "Please switch on the GPS", Toast.LENGTH_SHORT).show()
//            getCurrentLocationState()
        }

    }

    private fun getCurrentLocationState() {
        settingsClient?.let { settings ->
            locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest)
            locationSettingsRequest = builder.build()
            settings
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    Log.d("addOnSuccessListener: ", "true")
                    gpsState.value = true
                }
                .addOnFailureListener {
                    Log.d("addOnSuccessListener: ", "false")

                    gpsState.value = false
                    when ((it as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            val intentSenderRequest =
                                IntentSenderRequest.Builder((it as ResolvableApiException).resolution)
                                    .build()
                            locationSetting.launch(intentSenderRequest)
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
        }

    }

    private fun getGpsLocation() {
        if(activity != null){
            permissionRequest.launch(LOCATION_PERMISSIONS)
        }
    }


    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
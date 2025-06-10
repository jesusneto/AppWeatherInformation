package com.exemplo.appweatherinformation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplo.appweatherinformation.data.model.RemoteWeather
import com.exemplo.appweatherinformation.data.source.remote.retrofit.WeatherApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApiService: WeatherApiService
): ViewModel() {

    private val _weatherData = MutableLiveData<RemoteWeather?>()
    val weatherData: LiveData<RemoteWeather?> get() = _weatherData

    private val _dataSearchState = MutableLiveData<Boolean>()
    val dataSearchState: LiveData<Boolean> get() = _dataSearchState


    fun convertKelvinToCelsius(number: Double?): Double {
        return DecimalFormat().run {
            applyPattern(".#")
            parse(format(number?.minus(273))).toDouble()
        }
    }

    fun getWeather(query: String) {

        viewModelScope.launch {

            val result = weatherApiService.getSpecificWeather(query)
            if (result.isSuccessful) {
                val remoteWeather = result.body()
                _weatherData.value = remoteWeather
                _dataSearchState.value = true
            } else {
                _weatherData.value = null
                _dataSearchState.value = false
            }
        }
    }
}
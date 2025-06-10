package com.exemplo.appweatherinformation.data.model

import com.google.gson.annotations.SerializedName

data class RemoteWeather(
    val uId: Int,

    @SerializedName("id")
    val cityId: Int,

    val name: String,

    val wind: Wind,

    @SerializedName("dt")
    val time: Int,

    @SerializedName("weather")
    val weatherDescriptions: List<RemoteWeatherDescription>,

    @SerializedName("main")
    val weatherCondition: RemoteWeatherCondition
)

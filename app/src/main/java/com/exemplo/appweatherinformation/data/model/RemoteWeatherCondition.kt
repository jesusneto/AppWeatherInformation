package com.exemplo.appweatherinformation.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteWeatherCondition(
    var temp: Double,
    var feels_like: Double,
    var temp_min: Double,
    var temp_max: Double,
    val pressure: Double,
    val humidity: Double,
) : Parcelable

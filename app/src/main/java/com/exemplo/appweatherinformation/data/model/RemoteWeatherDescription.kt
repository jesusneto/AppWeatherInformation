package com.exemplo.appweatherinformation.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteWeatherDescription(
    val id: Long,
    val main: String?,
    val description: String?,
    val icon: String?,
) : Parcelable
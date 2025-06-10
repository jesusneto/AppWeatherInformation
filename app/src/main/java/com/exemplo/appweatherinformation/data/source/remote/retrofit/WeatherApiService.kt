package com.exemplo.appweatherinformation.data.source.remote.retrofit

import com.exemplo.appweatherinformation.data.model.RemoteWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getSpecificWeather(
        @Query("q") location: String
    ) : Response<RemoteWeather>
}
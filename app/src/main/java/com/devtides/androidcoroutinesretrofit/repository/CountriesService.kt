package com.devtides.androidcoroutinesretrofit.repository

import com.devtides.androidcoroutinesretrofit.model.CountriesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountriesService {
    private val BASE_URL= "https://raw.githubusercontent.com/"

    fun getCountryService(): CountriesApi =
        Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountriesApi::class.java)

    suspend fun getCountries() = getCountryService().getCountries()

}
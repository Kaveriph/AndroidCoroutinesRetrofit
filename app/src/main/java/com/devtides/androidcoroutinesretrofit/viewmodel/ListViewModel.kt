package com.devtides.androidcoroutinesretrofit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.androidcoroutinesretrofit.model.Country
import com.devtides.androidcoroutinesretrofit.repository.CountriesService
import kotlinx.coroutines.*
import retrofit2.HttpException

class ListViewModel: ViewModel() {

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()
    val countryService = CountriesService.getCountryService()
    val job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler {
        coroutineContext, throwable -> run {
           onError("Exception: ${throwable.message}")
        }
    }
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        loading.value = true
        /*var job = CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            val response = countryService.getCountries()
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    countries.value = response.body()
                    countryLoadError.value = null
                    loading.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }*/
        coroutineScope.launch(exceptionHandler) {
            val countryResponse = coroutineScope.async(Dispatchers.IO) {
                countryService.getCountries()
            }
            val countryList = countryResponse.await()
            if(countryList.isSuccessful) {
                countries.postValue(countryList.body())
                countryLoadError.value = null
                loading.value = false
            }
        }
    }



    private fun onError(message: String) {
        countryLoadError.value = message
        loading.value = false
    }

    override fun onCleared() {
        job?.cancel()
    }

}
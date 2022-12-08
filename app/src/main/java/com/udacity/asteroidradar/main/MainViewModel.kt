package com.udacity.asteroidradar.main

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.NetworkPictureOfTheDay
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AstroidDatabase
import com.udacity.asteroidradar.database.DatabaseAstroid

import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.NetworkAsteroid
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import com.udacity.asteroidradar.repository.AstroidRepository
import com.udacity.asteroidradar.repository.RepositoryAstroid
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException
import javax.security.auth.callback.Callback

class MainViewModel(application: Application) : AndroidViewModel(application) {




    private val database = DatabaseAstroid.getInstance(application)
    private val repository = RepositoryAstroid(database)

    val playlist : LiveData<List<AstroidRepository>>
    get() = repository.astroids


    private val _navigateToSelectedProperty = MutableLiveData<AstroidRepository?>()

    val navigateToSelectedProperty: MutableLiveData<AstroidRepository?>
        get() = _navigateToSelectedProperty





    private val _PictureOfTheDayTitle = MutableStateFlow<String>("Picture of the Day")
    val PictureOfTheDayTitle: StateFlow<String> = _PictureOfTheDayTitle

    private val _PictureOfTheDayUrl = MutableStateFlow<String>("https://www.nasa.gov/sites/all/themes/custom/nasatwo/images/nasa-logo.svg")
    val PictureOfTheDayUrl: StateFlow<String> = _PictureOfTheDayUrl







    init {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                repository.refreshAstroids(RepositoryAstroid.AsteroidsFilter.SHOW_ALL)

                Log.i("live", playlist.toString())
                setPictureOfTheDay()

            }catch (e : Exception){

            }


        }



    }




    fun filterAsteroids(filter : RepositoryAstroid.AsteroidsFilter){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.refreshAstroids(filter)
            }
        }catch (e: Exception){

        }

    }


    fun displayPropertyDetails(asteroid : AstroidRepository) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    suspend fun setPictureOfTheDay() {
        viewModelScope.launch {

            NetworkPictureOfTheDay.reterofitservice.getPicDay(api_key = Constants.api_key)
                .enqueue(object : retrofit2.Callback<PictureOfDay> {
                    override fun onResponse(
                        call: Call<PictureOfDay>,
                        response: Response<PictureOfDay>
                    ) {
                        val res = response.body()
                        _PictureOfTheDayTitle.value = res?.title.toString()
                        _PictureOfTheDayUrl.value = res?.url.toString()
                        Log.i("picday",res?.url.toString())


                    }

                    override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                        Log.i("picdayerror", t.toString())
                    }


                })


        }

    }



}
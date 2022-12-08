package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.repository.AstroidRepository

class DetailViewModel(asteroid: AstroidRepository, application: Application) : AndroidViewModel(application) {


    private val _selectedProperty = MutableLiveData<AstroidRepository>()

    val selectedProperty: LiveData<AstroidRepository>
        get() = _selectedProperty

    init {
        _selectedProperty.value = asteroid
    }



}
package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)


interface PicDayService{
    //    @GET("apod?apod=6CJvb7aKCLVcSaS5pZqsTqvqRaI06KNZfuExYm41")
    @GET("apod")
    fun getPicDay(@Query("api_key") api_key: String) : Call<PictureOfDay>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitPictureOfDay = Retrofit.Builder()
    .baseUrl("https://api.nasa.gov/planetary/")
//    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

object NetworkPictureOfTheDay {
    // Configure retrofit to parse JSON and use coroutines
    val reterofitservice : PicDayService by lazy {
        retrofitPictureOfDay.create(PicDayService::class.java)
    }
}
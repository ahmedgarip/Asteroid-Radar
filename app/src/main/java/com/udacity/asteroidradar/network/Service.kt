package com.udacity.asteroidradar.network


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface AstroidService {

    //    @GET("feed?start_date=${days.first()}&end_date=&${days.last()}&api_key=6CJvb7aKCLVcSaS5pZqsTqvqRaI06KNZfuExYm41")
//    @GET("devbytes.json")
//    @GET("feed?start_date=2022-11-01&end_date=&2022-11-08&api_key=6CJvb7aKCLVcSaS5pZqsTqvqRaI06KNZfuExYm41")
    @GET("feed")
    fun getPlaylist(@Query("start_date") start_date:String,@Query("end_date")end_date: String,@Query("api_key")api_key:String): Call<ResponseBody>

}




private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.nasa.gov/neo/rest/v1/")
//        .baseUrl("https://devbytes.udacity.com/")
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .addConverterFactory(MoshiConverterFactory.create(moshi))

//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()
object Network {
    // Configure retrofit to parse JSON and use coroutines
    val reterofitservice : AstroidService by lazy {
        retrofit.create(AstroidService::class.java)
    }



}






//@JsonClass(generateAdapter = true)
//data class NetworkVideoContainer(val videos: List<NetworkVideo>)

/**
 * Videos represent a devbyte that can be played.
 */
//@JsonClass(generateAdapter = true)
//data class NetworkVideo(
//    val title: String,
//    val description: String,
//    val url: String,
//    val updated: String,
//    val thumbnail: String,
//    val closedCaptions: String?)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val codename: String,
    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @Json(name = "kilometers_per_second")
    val relativeVelocity: Double,
    @Json(name = "miss_distance")
    val distanceFromEarth: Double,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean)

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val networkAstroids: List<NetworkAsteroid>)



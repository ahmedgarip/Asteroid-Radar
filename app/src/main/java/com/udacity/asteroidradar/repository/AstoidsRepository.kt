package com.udacity.asteroidradar.repository

import android.app.Application
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.network.Network
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.*

@Parcelize
class AstroidRepository (val id: Long,
                              val codename: String,
                              val closeApproachDate: String,
                              val absoluteMagnitude: Double,
                              val estimatedDiameter: Double,
                              val relativeVelocity: Double,
                              val distanceFromEarth: Double,
                              val isPotentiallyHazardous: Boolean): Parcelable

class RepositoryAstroid (private val datbaseAstroid : DatabaseAstroid) {
//    private lateinit var jsonResult :JSONObject

    enum class AsteroidsFilter(val value: String) { SHOW_DAY("day"), SHOW_WEEK("week"), SHOW_ALL("all") }


    val astroids: LiveData<List<AstroidRepository>> =
        Transformations.map(datbaseAstroid.astroidDao.getAstroids()) {
            it.asRepoAstroids()
        }



    suspend fun refreshAstroids(filter : AsteroidsFilter) {


        val days = getNextSevenDaysFormattedDates()
        var start_date = days.first()
        var end_date = days.last()

        when(filter){
            AsteroidsFilter.SHOW_DAY -> {
                start_date  = days.first()
                end_date = days.first()
                datbaseAstroid.astroidDao.deleteAll()
                refreshNetwork(start_date,end_date)
            }
            AsteroidsFilter.SHOW_WEEK -> {
                start_date = days.first()
                end_date = days.last()
                datbaseAstroid.astroidDao.deleteAll()
                refreshNetwork(start_date,end_date)
            }
            else -> {
                start_date = days.first()
                end_date = days.last()
                refreshNetwork(start_date,end_date)

            }
        }






    }

    suspend fun refreshNetwork(start_date:String, end_date:String){
        val api_key = Constants.api_key

        withContext(Dispatchers.IO) {


            try {


                val call: Call<ResponseBody> = Network.reterofitservice.getPlaylist(
                    start_date ,
                    end_date ,
                    api_key
                )
                val res = call.awaitResponse()
                val json = JSONObject(res.body()!!.string())
                if (call.isExecuted){

                    Log.i("mynetwork", res.body()!!.string())
                    transferetodataobjects(json)
                }else{
                    Log.i("mynetwork", "no excuted")
                }
            }catch (e: HttpException){
                Log.i("mynetwork", e.toString())

            }
//            call.enqueue(object : retrofit2.Callback<ResponseBody> {
//                override fun equals(other: Any?): Boolean {
//                    return super.equals(other)
//                }
//
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//
//                  val   jsonResult :JSONObject = JSONObject(response.body()!!.string())
//
//
//
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Log.i("mynetwork", "failer" + t.toString())
//                }
//
//
//            })


        }

    }


    suspend fun insert(container: NetworkAstroidContainer){
        withContext(Dispatchers.IO){
            async { datbaseAstroid.astroidDao.insert(container.asteroids[0]) }
        }


    }

    suspend fun refreshNetwork(filter : AsteroidsFilter)  {




    }

     suspend fun transferetodataobjects(res : JSONObject) {

         withContext(Dispatchers.IO){
             //                    Log.i("mynetwork", response.body().toString())
             val asteroids = parseAsteroidsJsonResult(res)
             val astroidscontainer = NetworkAstroidContainer(res)
//
             Log.i("mynetwork", astroidscontainer.asteroids.toString())
//                    Log.i("mynetwork",astroids.)
//                    Log.i("mynetwork",astroids.last().toString())


             async { datbaseAstroid.astroidDao.insertAllAstroids(*astroidscontainer.asDatabaseModel()) }

         }




    }

}
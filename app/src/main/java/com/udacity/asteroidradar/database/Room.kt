package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid


@Dao
interface AstroidDao {
    @Query("select * from AstroidDatabase")
    fun getAstroids() : LiveData<List<AstroidDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAstroids(vararg astroids : AstroidDatabase)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(astroid : AstroidDatabase)

    @Query("delete from AstroidDatabase")
    fun deleteAll()
}

@Database(version = 2, entities = [AstroidDatabase::class], exportSchema = false)
abstract class DatabaseAstroid: RoomDatabase(){
    abstract val astroidDao : AstroidDao
    companion object {
        @Volatile
        private var Instance: DatabaseAstroid?  = null
        fun getInstance(context: Context): DatabaseAstroid{
            synchronized(this){
                var instance = Instance
                if (instance == null){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseAstroid::class.java,
                        "DatabaseAstroid"
                    ).fallbackToDestructiveMigration().build()



                }

                return instance
            }

        }
    }

}

//@Database(entities = [AstroidDatabase::class], version = 1)
//abstract class DatabaseAstroid : RoomDatabase() {
//    abstract val astroidDao: AstroidDao
//    private lateinit var INSTANCE: DatabaseAstroid
//    fun getInstance(context: Context): DatabaseAstroid {
//        synchronized(DatabaseAstroid::class.java) {
//            if (!::INSTANCE.isInitialized) {
//                INSTANCE = Room.databaseBuilder(
//                    context.applicationContext,
//                    DatabaseAstroid::class.java,
//                    "DatabaseAstroid"
//                ).build()
//            }
//        }
//        return INSTANCE
//    }
//}
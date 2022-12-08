package com.udacity.asteroidradar.database

import androidx.lifecycle.Transformations
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AstroidRepository
import org.json.JSONObject


@Entity
data class AstroidDatabase constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<AstroidDatabase>.asRepoAstroids() : List<AstroidRepository>{
    return map {
        AstroidRepository(
        id = it.id,
        codename= it.codename,
        closeApproachDate = it.closeApproachDate,
        absoluteMagnitude = it.absoluteMagnitude,
        estimatedDiameter = it.estimatedDiameter,
        relativeVelocity = it.relativeVelocity,
        distanceFromEarth = it.distanceFromEarth,
        isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

fun List<Asteroid>.asDatabaseModel(): Array<AstroidDatabase> {
    return map {
        AstroidDatabase (
            id = it.id,
            codename= it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}

data class NetworkAstroidContainer(val jsonResult: JSONObject){
    val dataAsteroids = parseAsteroidsJsonResult(jsonResult =  jsonResult)
    val asteroids : List<AstroidDatabase> = dataAsteroids.map{
        AstroidDatabase(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            distanceFromEarth = it.distanceFromEarth,
            estimatedDiameter = it.estimatedDiameter,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
            relativeVelocity = it.relativeVelocity
        )

    }
}

fun NetworkAstroidContainer.asDatabaseModel(): Array<AstroidDatabase> {
    return asteroids.map {
        AstroidDatabase (
            id = it.id,
            relativeVelocity = it.relativeVelocity,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
            estimatedDiameter = it.estimatedDiameter,
            distanceFromEarth = it.distanceFromEarth,
            closeApproachDate = it.closeApproachDate,
            codename = it.codename,
            absoluteMagnitude = it.absoluteMagnitude)
    }.toTypedArray()
}
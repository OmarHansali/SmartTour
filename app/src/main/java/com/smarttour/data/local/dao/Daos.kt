package com.smarttour.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smarttour.data.local.entity.PoiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {
    @Insert
    suspend fun insertPoi(poi: PoiEntity)

    @Insert
    suspend fun insertAll(pois: List<PoiEntity>)

    @Query("SELECT * FROM pois WHERE id = :poiId")
    fun getPoiById(poiId: String): Flow<PoiEntity?>

    @Query("SELECT * FROM pois ORDER BY rating DESC")
    fun getAllPois(): Flow<List<PoiEntity>>

    @Query("SELECT * FROM pois WHERE category = :category")
    fun getPoisByCategory(category: String): Flow<List<PoiEntity>>

    @Query("SELECT * FROM pois WHERE isFavorite = 1")
    fun getFavoritePois(): Flow<List<PoiEntity>>

    @Update
    suspend fun updatePoi(poi: PoiEntity)

    @Query("DELETE FROM pois WHERE id = :poiId")
    suspend fun deletePoi(poiId: String)
}

@Dao
interface TourDao {
    @Insert
    suspend fun insertTour(tour: com.smarttour.data.local.entity.TourEntity)

    @Query("SELECT * FROM tours WHERE id = :tourId")
    fun getTourById(tourId: String): Flow<com.smarttour.data.local.entity.TourEntity?>

    @Query("SELECT * FROM tours ORDER BY rating DESC")
    fun getAllTours(): Flow<List<com.smarttour.data.local.entity.TourEntity>>

    @Query("SELECT * FROM tours WHERE isSaved = 1")
    fun getSavedTours(): Flow<List<com.smarttour.data.local.entity.TourEntity>>
}

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: com.smarttour.data.local.entity.UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<com.smarttour.data.local.entity.UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): Flow<com.smarttour.data.local.entity.UserEntity?>

    @Update
    suspend fun updateUser(user: com.smarttour.data.local.entity.UserEntity)
}

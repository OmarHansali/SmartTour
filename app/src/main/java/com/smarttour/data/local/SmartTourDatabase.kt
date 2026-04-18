package com.smarttour.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smarttour.data.local.entity.PoiEntity
import com.smarttour.data.local.entity.TourEntity
import com.smarttour.data.local.entity.UserEntity
import com.smarttour.data.local.dao.PoiDao
import com.smarttour.data.local.dao.TourDao
import com.smarttour.data.local.dao.UserDao

@Database(
    entities = [PoiEntity::class, TourEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartTourDatabase : RoomDatabase() {
    abstract fun poiDao(): PoiDao
    abstract fun tourDao(): TourDao
    abstract fun userDao(): UserDao
}

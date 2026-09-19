package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BookingOrder
import com.example.data.model.CustomerProfile
import com.example.data.model.HelperPartner
import com.example.data.model.PartnerProfile

@Database(
  entities = [
    BookingOrder::class,
    HelperPartner::class,
    CustomerProfile::class,
    PartnerProfile::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun bookingDao(): BookingDao
  abstract fun helperDao(): HelperDao
  abstract fun profileDao(): ProfileDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "helperhub_database"
        )
        .fallbackToDestructiveMigration()
        .build()
        INSTANCE = instance
        instance
      }
    }
  }
}

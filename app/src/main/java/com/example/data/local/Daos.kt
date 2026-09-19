package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookingOrder
import com.example.data.model.CustomerProfile
import com.example.data.model.HelperPartner
import com.example.data.model.PartnerProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
  @Query("SELECT * FROM booking_orders ORDER BY createdAt DESC")
  fun getAllBookings(): Flow<List<BookingOrder>>

  @Query("SELECT * FROM booking_orders WHERE status = :status ORDER BY createdAt DESC")
  fun getBookingsByStatus(status: String): Flow<List<BookingOrder>>

  @Query("SELECT * FROM booking_orders WHERE id = :id LIMIT 1")
  suspend fun getBookingById(id: Long): BookingOrder?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBooking(booking: BookingOrder): Long

  @Query("UPDATE booking_orders SET status = :status WHERE id = :id")
  suspend fun updateStatus(id: Long, status: String)

  @Query("DELETE FROM booking_orders WHERE id = :id")
  suspend fun deleteBooking(id: Long)
}

@Dao
interface HelperDao {
  @Query("SELECT * FROM helpers ORDER BY rating DESC")
  fun getAllHelpers(): Flow<List<HelperPartner>>

  @Query("SELECT * FROM helpers WHERE isAvailable = 1 ORDER BY rating DESC")
  fun getAvailableHelpers(): Flow<List<HelperPartner>>

  @Query("SELECT * FROM helpers WHERE id = :id LIMIT 1")
  suspend fun getHelperById(id: String): HelperPartner?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(helpers: List<HelperPartner>)

  @Update
  suspend fun updateHelper(helper: HelperPartner)

  @Query("UPDATE helpers SET isAvailable = :isAvailable WHERE id = :id")
  suspend fun setAvailability(id: String, isAvailable: Boolean)
}

@Dao
interface ProfileDao {
  @Query("SELECT * FROM customer_profile WHERE id = 1 LIMIT 1")
  fun getCustomerProfile(): Flow<CustomerProfile?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCustomerProfile(profile: CustomerProfile)

  @Query("SELECT * FROM partner_profile WHERE id = 'current_partner' LIMIT 1")
  fun getPartnerProfile(): Flow<PartnerProfile?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun savePartnerProfile(profile: PartnerProfile)
}

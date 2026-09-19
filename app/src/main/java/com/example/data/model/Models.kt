package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Service Work Category available for customers to explore and book.
 */
data class WorkCategory(
  val id: String,
  val name: String,
  val iconName: String,
  val subtitle: String,
  val basePricePerHour: Int,
  val popularTasks: List<String>
)

/**
 * Service Partner / Helper registered on the platform.
 */
@Entity(tableName = "helpers")
data class HelperPartner(
  @PrimaryKey val id: String,
  val name: String,
  val tradeTitle: String,
  val rating: Float,
  val reviewCount: Int,
  val hourlyRate: Int,
  val isVerified: Boolean,
  val phone: String,
  val primaryCategory: String,
  val bio: String,
  val completedJobsCount: Int,
  val experienceYears: Int,
  val isAvailable: Boolean = true,
  val avatarColorHex: Long = 0xFF0D9488
)

/**
 * Status of a booking order between Customer and Partner.
 */
enum class OrderStatus(val displayName: String) {
  RECEIVED("Order Received"),
  ACCEPTED("Partner Confirmed"),
  IN_PROGRESS("Work In Progress"),
  COMPLETED("Completed"),
  CANCELLED("Cancelled")
}

/**
 * Booking Order placed by a customer and received by partner helpers.
 */
@Entity(tableName = "booking_orders")
data class BookingOrder(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val customerName: String,
  val customerPhone: String,
  val serviceAddress: String,
  val serviceCategory: String,
  val serviceTaskTitle: String,
  val helperId: String,
  val helperName: String,
  val scheduleDate: String,
  val timeSlot: String,
  val workNotes: String,
  val estimatedHours: Int,
  val hourlyRate: Int,
  val totalEstimatedPrice: Double,
  val status: String = OrderStatus.RECEIVED.name,
  val createdAt: Long = System.currentTimeMillis()
)

/**
 * Customer profile with completion status.
 */
@Entity(tableName = "customer_profile")
data class CustomerProfile(
  @PrimaryKey val id: Int = 1,
  val fullName: String = "Alex Rivera",
  val email: String = "alex.rivera@example.com",
  val phone: String = "+1 (555) 234-8901",
  val defaultAddress: String = "742 Evergreen Terrace, Suite 4B",
  val preferredPayment: String = "Credit Card ending in 4022",
  val isProfileCompleted: Boolean = true
)

/**
 * Partner / Helper profile with completion progress.
 */
@Entity(tableName = "partner_profile")
data class PartnerProfile(
  @PrimaryKey val id: String = "current_partner",
  val fullName: String = "Marcus Vance",
  val tradeTitle: String = "Master Electrician & Smart Home Pro",
  val email: String = "marcus.vance@helpers.com",
  val phone: String = "+1 (555) 891-2345",
  val bio: String = "Licensed master electrician with 9+ years of experience in residential diagnostics, circuit panels, lighting fixtures, and high-efficiency appliance installations.",
  val hourlyRate: Int = 55,
  val experienceYears: Int = 9,
  val serviceCategories: String = "Electrical Works, Appliance Repair, Home Tech",
  val isAvailableForJobs: Boolean = true,
  val verificationBadge: String = "Verified Pro",
  val completionPercentage: Int = 95
)

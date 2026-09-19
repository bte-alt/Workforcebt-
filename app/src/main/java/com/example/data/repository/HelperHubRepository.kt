package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.BookingOrder
import com.example.data.model.CustomerProfile
import com.example.data.model.HelperPartner
import com.example.data.model.OrderStatus
import com.example.data.model.PartnerProfile
import com.example.data.model.WorkCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class HelperHubRepository(
  private val database: AppDatabase,
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
  private val bookingDao = database.bookingDao()
  private val helperDao = database.helperDao()
  private val profileDao = database.profileDao()

  val allBookings: Flow<List<BookingOrder>> = bookingDao.getAllBookings()
  val allHelpers: Flow<List<HelperPartner>> = helperDao.getAllHelpers()
  val customerProfile: Flow<CustomerProfile?> = profileDao.getCustomerProfile()
  val partnerProfile: Flow<PartnerProfile?> = profileDao.getPartnerProfile()

  val defaultCategories: List<WorkCategory> = listOf(
    WorkCategory(
      id = "cleaning",
      name = "Home Cleaning",
      iconName = "cleaning",
      subtitle = "Deep clean, sanitization, kitchens & bathrooms",
      basePricePerHour = 38,
      popularTasks = listOf("Full Home Deep Clean", "Kitchen Sanitization", "Move-in / Move-out", "Bathroom Scrub")
    ),
    WorkCategory(
      id = "electrical",
      name = "Electrical Works",
      iconName = "electrical",
      subtitle = "Wiring, fixtures, circuit breakers, outlets",
      basePricePerHour = 55,
      popularTasks = listOf("Ceiling Fan Install", "Light Fixture Replacement", "Breaker Panel Diagnostic", "Smart Switch Setup")
    ),
    WorkCategory(
      id = "plumbing",
      name = "Plumbing Care",
      iconName = "plumbing",
      subtitle = "Pipe leaks, drains, faucets, water heaters",
      basePricePerHour = 50,
      popularTasks = listOf("Faucet & Sink Repair", "Drain Unclogging", "Toilet Replacement", "Water Line Diagnostic")
    ),
    WorkCategory(
      id = "handyman",
      name = "Carpentry & Fixes",
      iconName = "carpentry",
      subtitle = "Furniture assembly, door repair, shelving",
      basePricePerHour = 45,
      popularTasks = listOf("Flat-pack Furniture Assembly", "Door Alignment & Hinges", "Wall Shelf Mounting", "Drywall Patching")
    ),
    WorkCategory(
      id = "appliance",
      name = "Appliance Repair",
      iconName = "appliance",
      subtitle = "Washing machines, dryers, refrigerators, AC",
      basePricePerHour = 52,
      popularTasks = listOf("Washing Machine Fix", "Refrigerator Cooling Check", "AC Filter & Servicing", "Dishwasher Inspection")
    ),
    WorkCategory(
      id = "moving",
      name = "Moving & Helpers",
      iconName = "moving",
      subtitle = "Heavy lifting, van loading, box moving",
      basePricePerHour = 42,
      popularTasks = listOf("Heavy Sofa & Bed Moving", "Van Loading & Unloading", "Room Rearrangement", "Storage Organizing")
    ),
    WorkCategory(
      id = "painting",
      name = "Painting Care",
      iconName = "painting",
      subtitle = "Interior walls, trims, touchups & repairs",
      basePricePerHour = 40,
      popularTasks = listOf("Accent Wall Painting", "Single Room Refresh", "Door & Trim Coating", "Ceiling Stain Cover")
    ),
    WorkCategory(
      id = "garden",
      name = "Lawn & Garden",
      iconName = "garden",
      subtitle = "Mowing, yard cleanup, hedge trimming",
      basePricePerHour = 35,
      popularTasks = listOf("Lawn Mowing & Edging", "Hedge & Shrub Trimming", "Yard Debris Cleanup", "Garden Bed Mulching")
    )
  )

  suspend fun initializeSampleDataIfNeeded() = withContext(ioDispatcher) {
    val existingHelpers = helperDao.getAllHelpers().firstOrNull()
    if (existingHelpers.isNullOrEmpty()) {
      val initialHelpers = listOf(
        HelperPartner(
          id = "current_partner",
          name = "Marcus Vance",
          tradeTitle = "Master Electrician & Pro Technician",
          rating = 4.95f,
          reviewCount = 186,
          hourlyRate = 55,
          isVerified = true,
          phone = "+1 (555) 891-2345",
          primaryCategory = "Electrical Works",
          bio = "Licensed master electrician with 9+ years experience. Specializes in fixtures, panel upgrades, and smart home automation.",
          completedJobsCount = 214,
          experienceYears = 9,
          isAvailable = true,
          avatarColorHex = 0xFF0D9488
        ),
        HelperPartner(
          id = "helper_2",
          name = "Elena Rostova",
          tradeTitle = "Deep Cleaning & Sanitization Specialist",
          rating = 4.98f,
          reviewCount = 230,
          hourlyRate = 38,
          isVerified = true,
          phone = "+1 (555) 345-6789",
          primaryCategory = "Home Cleaning",
          bio = "Eco-friendly residential cleaning expert. Leaves homes pristine, spotless, and refreshed with allergen-safe solutions.",
          completedJobsCount = 312,
          experienceYears = 7,
          isAvailable = true,
          avatarColorHex = 0xFF0284C7
        ),
        HelperPartner(
          id = "helper_3",
          name = "David Chen",
          tradeTitle = "Certified Plumber & Leak Specialist",
          rating = 4.89f,
          reviewCount = 142,
          hourlyRate = 50,
          isVerified = true,
          phone = "+1 (555) 456-7890",
          primaryCategory = "Plumbing Care",
          bio = "Expert in pipe diagnostics, emergency leak containment, drain restoration, and water heater troubleshooting.",
          completedJobsCount = 175,
          experienceYears = 8,
          isAvailable = true,
          avatarColorHex = 0xFFD97706
        ),
        HelperPartner(
          id = "helper_4",
          name = "Sarah Jenkins",
          tradeTitle = "Furniture Assembly & Moving Helper",
          rating = 4.92f,
          reviewCount = 118,
          hourlyRate = 42,
          isVerified = true,
          phone = "+1 (555) 567-8901",
          primaryCategory = "Carpentry & Fixes",
          bio = "Precision assembler for IKEA, Wayfair, and custom gym gear. Fast, careful with delicate surfaces, and friendly.",
          completedJobsCount = 159,
          experienceYears = 5,
          isAvailable = true,
          avatarColorHex = 0xFF7C3AED
        ),
        HelperPartner(
          id = "helper_5",
          name = "Carlos Mendez",
          tradeTitle = "Finish Carpenter & Master Handyman",
          rating = 4.87f,
          reviewCount = 98,
          hourlyRate = 45,
          isVerified = true,
          phone = "+1 (555) 678-9012",
          primaryCategory = "Carpentry & Fixes",
          bio = "Master woodwork, door alignment, custom shelving, and interior maintenance specialist.",
          completedJobsCount = 120,
          experienceYears = 11,
          isAvailable = false,
          avatarColorHex = 0xFFE11D48
        ),
        HelperPartner(
          id = "helper_6",
          name = "Maya Patel",
          tradeTitle = "HVAC & Appliance Diagnostics Pro",
          rating = 4.93f,
          reviewCount = 164,
          hourlyRate = 52,
          isVerified = true,
          phone = "+1 (555) 789-0123",
          primaryCategory = "Appliance Repair",
          bio = "Certified technician for cooling units, refrigerators, washers, and residential ventilation.",
          completedJobsCount = 198,
          experienceYears = 6,
          isAvailable = true,
          avatarColorHex = 0xFF059669
        )
      )
      helperDao.insertAll(initialHelpers)
    }

    val existingCustomer = profileDao.getCustomerProfile().firstOrNull()
    if (existingCustomer == null) {
      profileDao.saveCustomerProfile(
        CustomerProfile(
          id = 1,
          fullName = "Alex Rivera",
          email = "alex.rivera@example.com",
          phone = "+1 (555) 234-8901",
          defaultAddress = "742 Evergreen Terrace, Apt 4B, Metro City",
          preferredPayment = "Visa •••• 4022",
          isProfileCompleted = true
        )
      )
    }

    val existingPartner = profileDao.getPartnerProfile().firstOrNull()
    if (existingPartner == null) {
      profileDao.savePartnerProfile(
        PartnerProfile(
          id = "current_partner",
          fullName = "Marcus Vance",
          tradeTitle = "Master Electrician & Pro Technician",
          email = "marcus.vance@helpers.com",
          phone = "+1 (555) 891-2345",
          bio = "Licensed master electrician with 9+ years experience. Specializes in fixtures, panel upgrades, and smart home automation.",
          hourlyRate = 55,
          experienceYears = 9,
          serviceCategories = "Electrical Works, Appliance Repair, Smart Home",
          isAvailableForJobs = true,
          verificationBadge = "Verified Pro",
          completionPercentage = 100
        )
      )
    }

    val existingBookings = bookingDao.getAllBookings().firstOrNull()
    if (existingBookings.isNullOrEmpty()) {
      val now = System.currentTimeMillis()
      val sampleOrders = listOf(
        BookingOrder(
          customerName = "Jordan Taylor",
          customerPhone = "+1 (555) 762-1100",
          serviceAddress = "1204 Pinecrest Blvd, Apt 12",
          serviceCategory = "Electrical Works",
          serviceTaskTitle = "Ceiling Fan Installation & Remote Wiring",
          helperId = "current_partner",
          helperName = "Marcus Vance",
          scheduleDate = "Today, 3:30 PM",
          timeSlot = "Afternoon (1:00 PM - 4:00 PM)",
          workNotes = "New fan in box. Need old fixture dismounted and new 52-inch fan installed on 9ft ceiling.",
          estimatedHours = 2,
          hourlyRate = 55,
          totalEstimatedPrice = 110.0,
          status = OrderStatus.RECEIVED.name,
          createdAt = now - (25 * 60 * 1000) // 25 mins ago
        ),
        BookingOrder(
          customerName = "Samantha Miller",
          customerPhone = "+1 (555) 883-9921",
          serviceAddress = "88 West End Ave, Suite 300",
          serviceCategory = "Electrical Works",
          serviceTaskTitle = "Circuit Breaker Tripping Diagnostic",
          helperId = "current_partner",
          helperName = "Marcus Vance",
          scheduleDate = "Tomorrow, 10:00 AM",
          timeSlot = "Morning (9:00 AM - 12:00 PM)",
          workNotes = "Kitchen outlet trips breaker whenever microwave and coffee machine run at same time.",
          estimatedHours = 2,
          hourlyRate = 55,
          totalEstimatedPrice = 110.0,
          status = OrderStatus.ACCEPTED.name,
          createdAt = now - (2 * 3600 * 1000) // 2 hours ago
        ),
        BookingOrder(
          customerName = "David Kim",
          customerPhone = "+1 (555) 441-7788",
          serviceAddress = "340 Riverside Dr, Unit 5",
          serviceCategory = "Home Cleaning",
          serviceTaskTitle = "Full 2-Bedroom Deep Clean & Sanitization",
          helperId = "helper_2",
          helperName = "Elena Rostova",
          scheduleDate = "Yesterday, 2:00 PM",
          timeSlot = "Afternoon (1:00 PM - 4:00 PM)",
          workNotes = "Move-out preparation. Baseboards, oven interior, and double bathroom sanitation.",
          estimatedHours = 4,
          hourlyRate = 38,
          totalEstimatedPrice = 152.0,
          status = OrderStatus.COMPLETED.name,
          createdAt = now - (28 * 3600 * 1000)
        )
      )
      for (order in sampleOrders) {
        bookingDao.insertBooking(order)
      }
    }
  }

  suspend fun createBooking(order: BookingOrder): Long = withContext(ioDispatcher) {
    bookingDao.insertBooking(order)
  }

  suspend fun updateOrderStatus(orderId: Long, newStatus: OrderStatus) = withContext(ioDispatcher) {
    bookingDao.updateStatus(orderId, newStatus.name)
  }

  suspend fun saveCustomerProfile(profile: CustomerProfile) = withContext(ioDispatcher) {
    profileDao.saveCustomerProfile(profile)
  }

  suspend fun savePartnerProfile(profile: PartnerProfile) = withContext(ioDispatcher) {
    profileDao.savePartnerProfile(profile)
  }

  suspend fun setPartnerAvailability(isAvailable: Boolean) = withContext(ioDispatcher) {
    val current = profileDao.getPartnerProfile().firstOrNull() ?: PartnerProfile()
    profileDao.savePartnerProfile(current.copy(isAvailableForJobs = isAvailable))
    helperDao.setAvailability("current_partner", isAvailable)
  }
}

package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BookingOrder
import com.example.data.model.CustomerProfile
import com.example.data.model.HelperPartner
import com.example.data.model.OrderStatus
import com.example.data.model.PartnerProfile
import com.example.data.model.WorkCategory
import com.example.data.repository.HelperHubRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppMode {
  CUSTOMER,
  PARTNER
}

enum class CustomerTab {
  WORKS,
  HELPERS,
  ORDERS,
  PROFILE
}

enum class PartnerTab {
  DASHBOARD,
  ORDERS_RECEIVED,
  PROFILE
}

enum class BookingStep {
  SELECT_SERVICE,
  SCHEDULE_LOCATION,
  REVIEW_CONFIRM
}

data class BookingDraft(
  val category: WorkCategory? = null,
  val taskTitle: String = "",
  val helper: HelperPartner? = null,
  val scheduleDate: String = "Today, 3:00 PM",
  val timeSlot: String = "Afternoon (1:00 PM - 4:00 PM)",
  val address: String = "742 Evergreen Terrace, Apt 4B",
  val customerName: String = "Alex Rivera",
  val customerPhone: String = "+1 (555) 234-8901",
  val notes: String = "",
  val estimatedHours: Int = 2
) {
  val hourlyRate: Int get() = helper?.hourlyRate ?: (category?.basePricePerHour ?: 45)
  val estimatedTotal: Double get() = (hourlyRate * estimatedHours).toDouble()
}

class HelperHubViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: HelperHubRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = HelperHubRepository(db)
    viewModelScope.launch {
      repository.initializeSampleDataIfNeeded()
    }
  }

  // App Role Mode: Customer vs Partner
  private val _currentMode = MutableStateFlow(AppMode.CUSTOMER)
  val currentMode: StateFlow<AppMode> = _currentMode.asStateFlow()

  // Active Tab
  private val _customerTab = MutableStateFlow(CustomerTab.WORKS)
  val customerTab: StateFlow<CustomerTab> = _customerTab.asStateFlow()

  private val _partnerTab = MutableStateFlow(PartnerTab.ORDERS_RECEIVED)
  val partnerTab: StateFlow<PartnerTab> = _partnerTab.asStateFlow()

  // Reactive Data from Room
  val allBookings: StateFlow<List<BookingOrder>> = repository.allBookings
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allHelpers: StateFlow<List<HelperPartner>> = repository.allHelpers
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val customerProfile: StateFlow<CustomerProfile?> = repository.customerProfile
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val partnerProfile: StateFlow<PartnerProfile?> = repository.partnerProfile
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val categories: List<WorkCategory> = repository.defaultCategories

  // Booking Flow State
  private val _isBookingOpen = MutableStateFlow(false)
  val isBookingOpen: StateFlow<Boolean> = _isBookingOpen.asStateFlow()

  private val _currentBookingStep = MutableStateFlow(BookingStep.SELECT_SERVICE)
  val currentBookingStep: StateFlow<BookingStep> = _currentBookingStep.asStateFlow()

  private val _bookingDraft = MutableStateFlow(BookingDraft())
  val bookingDraft: StateFlow<BookingDraft> = _bookingDraft.asStateFlow()

  // Partner Filter
  private val _partnerOrderFilter = MutableStateFlow<OrderStatus?>(null)
  val partnerOrderFilter: StateFlow<OrderStatus?> = _partnerOrderFilter.asStateFlow()

  // Search & Filters in Customer works
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // User notification events (Snackbars / Toasts)
  private val _userMessage = MutableSharedFlow<String>()
  val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

  fun setAppMode(mode: AppMode) {
    _currentMode.value = mode
    viewModelScope.launch {
      if (mode == AppMode.PARTNER) {
        _userMessage.emit("Switched to Partner / Helper Dashboard")
      } else {
        _userMessage.emit("Switched to Customer Services & Booking")
      }
    }
  }

  fun setCustomerTab(tab: CustomerTab) {
    _customerTab.value = tab
  }

  fun setPartnerTab(tab: PartnerTab) {
    _partnerTab.value = tab
  }

  fun setPartnerOrderFilter(filter: OrderStatus?) {
    _partnerOrderFilter.value = filter
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  // --- Booking Flow Actions ---
  fun startBookingForCategory(category: WorkCategory, defaultTask: String = "") {
    val helper = allHelpers.value.firstOrNull { it.primaryCategory == category.name && it.isAvailable }
      ?: allHelpers.value.firstOrNull { it.isAvailable }
    val profile = customerProfile.value

    _bookingDraft.value = BookingDraft(
      category = category,
      taskTitle = if (defaultTask.isNotBlank()) defaultTask else category.popularTasks.firstOrNull() ?: category.name,
      helper = helper,
      address = profile?.defaultAddress ?: "742 Evergreen Terrace, Apt 4B",
      customerName = profile?.fullName ?: "Alex Rivera",
      customerPhone = profile?.phone ?: "+1 (555) 234-8901",
      estimatedHours = 2
    )
    _currentBookingStep.value = BookingStep.SELECT_SERVICE
    _isBookingOpen.value = true
  }

  fun startBookingForHelper(helper: HelperPartner) {
    val category = categories.firstOrNull { it.name == helper.primaryCategory }
      ?: categories.first()
    val profile = customerProfile.value

    _bookingDraft.value = BookingDraft(
      category = category,
      taskTitle = category.popularTasks.firstOrNull() ?: category.name,
      helper = helper,
      address = profile?.defaultAddress ?: "742 Evergreen Terrace, Apt 4B",
      customerName = profile?.fullName ?: "Alex Rivera",
      customerPhone = profile?.phone ?: "+1 (555) 234-8901",
      estimatedHours = 2
    )
    _currentBookingStep.value = BookingStep.SELECT_SERVICE
    _isBookingOpen.value = true
  }

  fun updateBookingDraft(update: (BookingDraft) -> BookingDraft) {
    _bookingDraft.value = update(_bookingDraft.value)
  }

  fun nextBookingStep() {
    when (_currentBookingStep.value) {
      BookingStep.SELECT_SERVICE -> _currentBookingStep.value = BookingStep.SCHEDULE_LOCATION
      BookingStep.SCHEDULE_LOCATION -> _currentBookingStep.value = BookingStep.REVIEW_CONFIRM
      BookingStep.REVIEW_CONFIRM -> submitBookingOrder()
    }
  }

  fun previousBookingStep() {
    when (_currentBookingStep.value) {
      BookingStep.SELECT_SERVICE -> _isBookingOpen.value = false
      BookingStep.SCHEDULE_LOCATION -> _currentBookingStep.value = BookingStep.SELECT_SERVICE
      BookingStep.REVIEW_CONFIRM -> _currentBookingStep.value = BookingStep.SCHEDULE_LOCATION
    }
  }

  fun dismissBooking() {
    _isBookingOpen.value = false
  }

  fun submitBookingOrder() {
    val draft = _bookingDraft.value
    val order = BookingOrder(
      customerName = draft.customerName.ifBlank { "Alex Rivera" },
      customerPhone = draft.customerPhone.ifBlank { "+1 (555) 234-8901" },
      serviceAddress = draft.address.ifBlank { "742 Evergreen Terrace" },
      serviceCategory = draft.category?.name ?: "General Home Works",
      serviceTaskTitle = draft.taskTitle.ifBlank { "Requested Helper Task" },
      helperId = draft.helper?.id ?: "current_partner",
      helperName = draft.helper?.name ?: "Marcus Vance",
      scheduleDate = draft.scheduleDate,
      timeSlot = draft.timeSlot,
      workNotes = draft.notes,
      estimatedHours = draft.estimatedHours,
      hourlyRate = draft.hourlyRate,
      totalEstimatedPrice = draft.estimatedTotal,
      status = OrderStatus.RECEIVED.name
    )

    viewModelScope.launch {
      repository.createBooking(order)
      _isBookingOpen.value = false
      _customerTab.value = CustomerTab.ORDERS
      _userMessage.emit("Order Received! Helper Partner notified.")
    }
  }

  // --- Partner Order Handling Actions ---
  fun acceptOrder(orderId: Long) {
    viewModelScope.launch {
      repository.updateOrderStatus(orderId, OrderStatus.ACCEPTED)
      _userMessage.emit("Order #$orderId accepted! Customer notified.")
    }
  }

  fun startWorkOnOrder(orderId: Long) {
    viewModelScope.launch {
      repository.updateOrderStatus(orderId, OrderStatus.IN_PROGRESS)
      _userMessage.emit("Work started on Order #$orderId.")
    }
  }

  fun completeOrder(orderId: Long) {
    viewModelScope.launch {
      repository.updateOrderStatus(orderId, OrderStatus.COMPLETED)
      _userMessage.emit("Order #$orderId marked as Completed! Payment released.")
    }
  }

  fun cancelOrder(orderId: Long) {
    viewModelScope.launch {
      repository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
      _userMessage.emit("Order #$orderId cancelled.")
    }
  }

  fun updateCustomerProfile(profile: CustomerProfile) {
    viewModelScope.launch {
      repository.saveCustomerProfile(profile)
      _userMessage.emit("Customer profile updated successfully!")
    }
  }

  fun updatePartnerProfile(profile: PartnerProfile) {
    viewModelScope.launch {
      val percentage = calculatePartnerCompletion(profile)
      repository.savePartnerProfile(profile.copy(completionPercentage = percentage))
      _userMessage.emit("Partner profile saved ($percentage% complete)")
    }
  }

  fun togglePartnerAvailability(isAvailable: Boolean) {
    viewModelScope.launch {
      repository.setPartnerAvailability(isAvailable)
      val text = if (isAvailable) "You are now ONLINE & ready for job orders" else "You are now OFFLINE / Busy"
      _userMessage.emit(text)
    }
  }

  private fun calculatePartnerCompletion(p: PartnerProfile): Int {
    var score = 0
    if (p.fullName.isNotBlank()) score += 20
    if (p.tradeTitle.isNotBlank()) score += 20
    if (p.phone.isNotBlank()) score += 15
    if (p.bio.isNotBlank() && p.bio.length > 20) score += 25
    if (p.hourlyRate > 0) score += 10
    if (p.serviceCategories.isNotBlank()) score += 10
    return score.coerceAtMost(100)
  }
}

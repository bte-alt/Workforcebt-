package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.OrderStatus
import com.example.ui.AppMode
import com.example.ui.CustomerTab
import com.example.ui.HelperHubViewModel
import com.example.ui.PartnerTab
import com.example.ui.components.TopMarketplaceBar
import com.example.ui.customer.CustomerBookingSheet
import com.example.ui.customer.CustomerHelpersScreen
import com.example.ui.customer.CustomerOrdersScreen
import com.example.ui.customer.CustomerProfileScreen
import com.example.ui.customer.CustomerWorksScreen
import com.example.ui.partner.PartnerDashboardScreen
import com.example.ui.partner.PartnerOrdersReceivedScreen
import com.example.ui.partner.PartnerProfileScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        HelperHubApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelperHubApp(
  viewModel: HelperHubViewModel = viewModel()
) {
  val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
  val customerTab by viewModel.customerTab.collectAsStateWithLifecycle()
  val partnerTab by viewModel.partnerTab.collectAsStateWithLifecycle()

  val allBookings by viewModel.allBookings.collectAsStateWithLifecycle()
  val allHelpers by viewModel.allHelpers.collectAsStateWithLifecycle()
  val customerProfile by viewModel.customerProfile.collectAsStateWithLifecycle()
  val partnerProfile by viewModel.partnerProfile.collectAsStateWithLifecycle()
  val categories = viewModel.categories

  val isBookingOpen by viewModel.isBookingOpen.collectAsStateWithLifecycle()
  val currentBookingStep by viewModel.currentBookingStep.collectAsStateWithLifecycle()
  val bookingDraft by viewModel.bookingDraft.collectAsStateWithLifecycle()
  val partnerOrderFilter by viewModel.partnerOrderFilter.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }
  val bookingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  // Listen for ViewModel snackbar notifications
  LaunchedEffect(Unit) {
    viewModel.userMessage.collect { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  val pendingPartnerOrdersCount = allBookings.count { it.status == OrderStatus.RECEIVED.name }
  val activeCustomerOrdersCount = allBookings.count {
    it.status != OrderStatus.COMPLETED.name && it.status != OrderStatus.CANCELLED.name
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      TopMarketplaceBar(
        currentMode = currentMode,
        onModeSwitch = { newMode -> viewModel.setAppMode(newMode) },
        isPartnerOnline = partnerProfile?.isAvailableForJobs ?: true,
        onPartnerOnlineToggle = { isOnline -> viewModel.togglePartnerAvailability(isOnline) }
      )
    },
    bottomBar = {
      if (currentMode == AppMode.CUSTOMER) {
        CustomerBottomNavigation(
          selectedTab = customerTab,
          onTabSelected = { viewModel.setCustomerTab(it) },
          ordersBadgeCount = activeCustomerOrdersCount
        )
      } else {
        PartnerBottomNavigation(
          selectedTab = partnerTab,
          onTabSelected = { viewModel.setPartnerTab(it) },
          receivedOrdersCount = pendingPartnerOrdersCount
        )
      }
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = currentMode to (if (currentMode == AppMode.CUSTOMER) customerTab.name else partnerTab.name),
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "DashboardNavigation"
      ) { (mode, _) ->
        if (mode == AppMode.CUSTOMER) {
          when (customerTab) {
            CustomerTab.WORKS -> {
              CustomerWorksScreen(
                categories = categories,
                topHelpers = allHelpers,
                searchQuery = searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                onBookCategory = { category, task ->
                  viewModel.startBookingForCategory(category, task)
                },
                onBookHelper = { helper ->
                  viewModel.startBookingForHelper(helper)
                },
                onExploreAllHelpers = {
                  viewModel.setCustomerTab(CustomerTab.HELPERS)
                }
              )
            }
            CustomerTab.HELPERS -> {
              CustomerHelpersScreen(
                helpers = allHelpers,
                onBookHelper = { helper ->
                  viewModel.startBookingForHelper(helper)
                }
              )
            }
            CustomerTab.ORDERS -> {
              CustomerOrdersScreen(
                orders = allBookings,
                onCancelOrder = { orderId -> viewModel.cancelOrder(orderId) },
                onBookNewWork = { viewModel.setCustomerTab(CustomerTab.WORKS) }
              )
            }
            CustomerTab.PROFILE -> {
              CustomerProfileScreen(
                profile = customerProfile,
                onSaveProfile = { updated -> viewModel.updateCustomerProfile(updated) },
                onSwitchToPartner = { viewModel.setAppMode(AppMode.PARTNER) }
              )
            }
          }
        } else {
          // Partner Mode
          when (partnerTab) {
            PartnerTab.DASHBOARD -> {
              PartnerDashboardScreen(
                partnerProfile = partnerProfile,
                allBookings = allBookings,
                onGoToOrdersReceived = { viewModel.setPartnerTab(PartnerTab.ORDERS_RECEIVED) },
                onGoToProfile = { viewModel.setPartnerTab(PartnerTab.PROFILE) },
                onAcceptOrder = { orderId -> viewModel.acceptOrder(orderId) }
              )
            }
            PartnerTab.ORDERS_RECEIVED -> {
              PartnerOrdersReceivedScreen(
                orders = allBookings,
                activeFilter = partnerOrderFilter,
                onFilterSelected = { viewModel.setPartnerOrderFilter(it) },
                onAcceptOrder = { orderId -> viewModel.acceptOrder(orderId) },
                onStartWork = { orderId -> viewModel.startWorkOnOrder(orderId) },
                onCompleteOrder = { orderId -> viewModel.completeOrder(orderId) },
                onCancelOrder = { orderId -> viewModel.cancelOrder(orderId) }
              )
            }
            PartnerTab.PROFILE -> {
              PartnerProfileScreen(
                profile = partnerProfile,
                onSaveProfile = { updated -> viewModel.updatePartnerProfile(updated) },
                onToggleAvailability = { isAvailable -> viewModel.togglePartnerAvailability(isAvailable) }
              )
            }
          }
        }
      }

      // Booking Bottom Sheet
      if (isBookingOpen) {
        CustomerBookingSheet(
          isOpen = isBookingOpen,
          sheetState = bookingSheetState,
          currentStep = currentBookingStep,
          draft = bookingDraft,
          categories = categories,
          availableHelpers = allHelpers.filter { it.isAvailable },
          onUpdateDraft = { update -> viewModel.updateBookingDraft(update) },
          onNextStep = { viewModel.nextBookingStep() },
          onPreviousStep = { viewModel.previousBookingStep() },
          onDismiss = { viewModel.dismissBooking() }
        )
      }
    }
  }
}

@Composable
fun CustomerBottomNavigation(
  selectedTab: CustomerTab,
  onTabSelected: (CustomerTab) -> Unit,
  ordersBadgeCount: Int,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 4.dp
  ) {
    NavigationBarItem(
      selected = selectedTab == CustomerTab.WORKS,
      onClick = { onTabSelected(CustomerTab.WORKS) },
      icon = { Icon(Icons.Default.Handyman, contentDescription = "Works") },
      label = { Text("Works", fontWeight = if (selectedTab == CustomerTab.WORKS) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_customer_works")
    )

    NavigationBarItem(
      selected = selectedTab == CustomerTab.HELPERS,
      onClick = { onTabSelected(CustomerTab.HELPERS) },
      icon = { Icon(Icons.Default.Engineering, contentDescription = "Helpers") },
      label = { Text("Helpers", fontWeight = if (selectedTab == CustomerTab.HELPERS) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_customer_helpers")
    )

    NavigationBarItem(
      selected = selectedTab == CustomerTab.ORDERS,
      onClick = { onTabSelected(CustomerTab.ORDERS) },
      icon = {
        if (ordersBadgeCount > 0) {
          BadgedBox(badge = { Badge { Text("$ordersBadgeCount") } }) {
            Icon(Icons.Default.ReceiptLong, contentDescription = "Bookings")
          }
        } else {
          Icon(Icons.Default.ReceiptLong, contentDescription = "Bookings")
        }
      },
      label = { Text("Bookings", fontWeight = if (selectedTab == CustomerTab.ORDERS) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_customer_orders")
    )

    NavigationBarItem(
      selected = selectedTab == CustomerTab.PROFILE,
      onClick = { onTabSelected(CustomerTab.PROFILE) },
      icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
      label = { Text("Profile", fontWeight = if (selectedTab == CustomerTab.PROFILE) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_customer_profile")
    )
  }
}

@Composable
fun PartnerBottomNavigation(
  selectedTab: PartnerTab,
  onTabSelected: (PartnerTab) -> Unit,
  receivedOrdersCount: Int,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 4.dp
  ) {
    NavigationBarItem(
      selected = selectedTab == PartnerTab.DASHBOARD,
      onClick = { onTabSelected(PartnerTab.DASHBOARD) },
      icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
      label = { Text("Dashboard", fontWeight = if (selectedTab == PartnerTab.DASHBOARD) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.secondary,
        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
      ),
      modifier = Modifier.testTag("nav_partner_dashboard")
    )

    NavigationBarItem(
      selected = selectedTab == PartnerTab.ORDERS_RECEIVED,
      onClick = { onTabSelected(PartnerTab.ORDERS_RECEIVED) },
      icon = {
        if (receivedOrdersCount > 0) {
          BadgedBox(badge = { Badge { Text("$receivedOrdersCount") } }) {
            Icon(Icons.Default.Inbox, contentDescription = "Orders Received")
          }
        } else {
          Icon(Icons.Default.Inbox, contentDescription = "Orders Received")
        }
      },
      label = { Text("Order Received", fontWeight = if (selectedTab == PartnerTab.ORDERS_RECEIVED) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.secondary,
        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
      ),
      modifier = Modifier.testTag("nav_partner_orders_received")
    )

    NavigationBarItem(
      selected = selectedTab == PartnerTab.PROFILE,
      onClick = { onTabSelected(PartnerTab.PROFILE) },
      icon = { Icon(Icons.Default.Badge, contentDescription = "Complete Profile") },
      label = { Text("Profile", fontWeight = if (selectedTab == PartnerTab.PROFILE) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.secondary,
        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
      ),
      modifier = Modifier.testTag("nav_partner_profile")
    )
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

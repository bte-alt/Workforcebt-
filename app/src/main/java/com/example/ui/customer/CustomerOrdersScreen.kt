package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingOrder
import com.example.data.model.OrderStatus
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.OrderStatusTracker

@Composable
fun CustomerOrdersScreen(
  orders: List<BookingOrder>,
  onCancelOrder: (Long) -> Unit,
  onBookNewWork: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "My Bookings & Orders",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "Real-time tracker for work orders received by partners",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
            onClick = onBookNewWork,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.testTag("customer_new_booking_button")
          ) {
            Text("+ Book Work", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }

    if (orders.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              Icons.Default.ReceiptLong,
              contentDescription = null,
              modifier = Modifier.size(64.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No Bookings Placed Yet",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Choose a work category or helper to place your first booking.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onBookNewWork,
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Browse Works")
            }
          }
        }
      }
    } else {
      items(orders, key = { it.id }) { order ->
        CustomerOrderCard(
          order = order,
          onCancel = { onCancelOrder(order.id) }
        )
      }
    }
  }
}

@Composable
fun CustomerOrderCard(
  order: BookingOrder,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top header with status badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Order #${order.id}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = order.serviceTaskTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        OrderStatusBadge(statusString = order.status)
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "${order.serviceCategory} • Helper: ${order.helperName}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Dedicated Order Lifecycle Status Tracker with Progress Bar
      OrderStatusTracker(
        statusString = order.status,
        showStageDetails = true,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Timing & Location Summary
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = order.scheduleDate,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
          )
        }

        Text(
          text = "$${order.totalEstimatedPrice.toInt()}",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary
        )
      }

      // Expand / Collapse Details Button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded }
          .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isExpanded) "Hide details" else "View order details",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
        Icon(
          imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp)
        )
      }

      AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(8.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = order.serviceAddress, style = MaterialTheme.typography.bodySmall)
          }

          if (order.workNotes.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Notes: \"${order.workNotes}\"",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Duration: ${order.estimatedHours} hrs @ $${order.hourlyRate}/hr",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          if (order.status == OrderStatus.RECEIVED.name || order.status == OrderStatus.ACCEPTED.name) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
              onClick = onCancel,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Cancel Booking", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun OrderProgressTimeline(statusString: String) {
  val steps = listOf(
    OrderStatus.RECEIVED to "Received",
    OrderStatus.ACCEPTED to "Accepted",
    OrderStatus.IN_PROGRESS to "In Progress",
    OrderStatus.COMPLETED to "Completed"
  )

  val currentStatus = runCatching { OrderStatus.valueOf(statusString) }.getOrDefault(OrderStatus.RECEIVED)
  val currentIndex = steps.indexOfFirst { it.first == currentStatus }.let { if (it == -1) 0 else it }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
      .padding(horizontal = 8.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    steps.forEachIndexed { index, pair ->
      val isReached = index <= currentIndex
      val isCurrent = index == currentIndex

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(
              when {
                isCurrent -> MaterialTheme.colorScheme.primary
                isReached -> Color(0xFF10B981)
                else -> Color(0xFFCBD5E1)
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isReached && !isCurrent) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
          }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = pair.second,
          fontSize = 9.sp,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
          color = if (isReached) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }

      if (index < steps.size - 1) {
        Box(
          modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .padding(horizontal = 2.dp)
            .background(
              if (index < currentIndex) Color(0xFF10B981) else Color(0xFFCBD5E1)
            )
        )
      }
    }
  }
}

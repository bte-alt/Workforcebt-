package com.example.ui.partner

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingOrder
import com.example.data.model.OrderStatus
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.OrderStatusTracker

@Composable
fun PartnerOrdersReceivedScreen(
  orders: List<BookingOrder>,
  activeFilter: OrderStatus?,
  onFilterSelected: (OrderStatus?) -> Unit,
  onAcceptOrder: (Long) -> Unit,
  onStartWork: (Long) -> Unit,
  onCompleteOrder: (Long) -> Unit,
  onCancelOrder: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  val filteredOrders = if (activeFilter == null) {
    orders
  } else {
    orders.filter { it.status == activeFilter.name }
  }

  val receivedCount = orders.count { it.status == OrderStatus.RECEIVED.name }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    // Header
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
              text = "Orders Received",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "Incoming customer booking requests & jobs",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          if (receivedCount > 0) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFEF3C7))
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = "$receivedCount Pending",
                color = Color(0xFFB45309),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }
        }
      }
    }

    // Filter Chips Row
    item {
      val filterTabs = listOf(
        null to "All Orders (${orders.size})",
        OrderStatus.RECEIVED to "New Received (${orders.count { it.status == OrderStatus.RECEIVED.name }})",
        OrderStatus.ACCEPTED to "Accepted (${orders.count { it.status == OrderStatus.ACCEPTED.name }})",
        OrderStatus.IN_PROGRESS to "In Progress (${orders.count { it.status == OrderStatus.IN_PROGRESS.name }})",
        OrderStatus.COMPLETED to "Completed (${orders.count { it.status == OrderStatus.COMPLETED.name }})"
      )

      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filterTabs) { (status, label) ->
          val isSelected = activeFilter == status
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(
                if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
              )
              .clickable { onFilterSelected(status) }
              .padding(horizontal = 14.dp, vertical = 7.dp)
          ) {
            Text(
              text = label,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Empty state
    if (filteredOrders.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              Icons.Default.Inbox,
              contentDescription = null,
              modifier = Modifier.size(56.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "No Orders in this category",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Switch filter to 'All' or wait for new incoming requests.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(filteredOrders, key = { it.id }) { order ->
        PartnerOrderItemCard(
          order = order,
          onAccept = { onAcceptOrder(order.id) },
          onStart = { onStartWork(order.id) },
          onComplete = { onCompleteOrder(order.id) },
          onDecline = { onCancelOrder(order.id) },
          onCallCustomer = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.customerPhone}"))
            runCatching { context.startActivity(intent) }
          }
        )
      }
    }
  }
}

@Composable
fun PartnerOrderItemCard(
  order: BookingOrder,
  onAccept: () -> Unit,
  onStart: () -> Unit,
  onComplete: () -> Unit,
  onDecline: () -> Unit,
  onCallCustomer: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top header: Order ID, status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "ORDER #${order.id}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "• ${order.serviceCategory}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        OrderStatusBadge(statusString = order.status)
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = order.serviceTaskTitle,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Customer Info & Call Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = order.customerName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = order.customerPhone,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        OutlinedButton(
          onClick = onCallCustomer,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Call", fontSize = 12.sp)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Schedule & Location
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "${order.scheduleDate} • ${order.timeSlot}",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = order.serviceAddress,
          style = MaterialTheme.typography.bodySmall
        )
      }

      // Expandable Notes
      if (order.workNotes.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Instructions: \"${order.workNotes}\"",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Dedicated Order Lifecycle Status Tracker with Progress Bar
      OrderStatusTracker(
        statusString = order.status,
        showStageDetails = false,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      Spacer(modifier = Modifier.height(12.dp))

      // Price & Action Buttons based on order status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Total Payout",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "$${order.totalEstimatedPrice.toInt()}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
        }

        when (order.status) {
          OrderStatus.RECEIVED.name -> {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              OutlinedButton(
                onClick = onDecline,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("partner_decline_order_${order.id}")
              ) {
                Text("Decline", fontSize = 12.sp)
              }

              Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("partner_accept_order_${order.id}")
              ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Accept Order", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            }
          }

          OrderStatus.ACCEPTED.name -> {
            Button(
              onClick = onStart,
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
              modifier = Modifier.testTag("partner_start_work_${order.id}")
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Start Work", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }

          OrderStatus.IN_PROGRESS.name -> {
            Button(
              onClick = onComplete,
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
              modifier = Modifier.testTag("partner_complete_order_${order.id}")
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Mark Completed", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }

          OrderStatus.COMPLETED.name -> {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFDCFCE7))
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "✓ Completed & Settled",
                color = Color(0xFF15803D),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }

          else -> {
            Text(
              text = "Status: ${order.status}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

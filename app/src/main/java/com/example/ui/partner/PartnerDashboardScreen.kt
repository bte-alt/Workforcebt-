package com.example.ui.partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BookingOrder
import com.example.data.model.OrderStatus
import com.example.data.model.PartnerProfile
import com.example.ui.components.OrderStatusBadge

@Composable
fun PartnerDashboardScreen(
  partnerProfile: PartnerProfile?,
  allBookings: List<BookingOrder>,
  onGoToOrdersReceived: () -> Unit,
  onGoToProfile: () -> Unit,
  onAcceptOrder: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val profile = partnerProfile ?: PartnerProfile()
  val receivedOrders = allBookings.filter { it.status == OrderStatus.RECEIVED.name }
  val activeOrders = allBookings.filter { it.status == OrderStatus.ACCEPTED.name || it.status == OrderStatus.IN_PROGRESS.name }
  val completedOrders = allBookings.filter { it.status == OrderStatus.COMPLETED.name }

  val totalEarnings = completedOrders.sumOf { it.totalEstimatedPrice } + (activeOrders.size * profile.hourlyRate * 2)

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    // Partner Hero Banner Card
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.linearGradient(
              listOf(Color(0xFFD97706), Color(0xFFB45309))
            )
          )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xFFFEF3C7).copy(alpha = 0.25f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "PARTNER DASHBOARD",
                  color = Color(0xFFFEF3C7),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Welcome back, ${profile.fullName.split(" ").firstOrNull() ?: "Partner"}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "${profile.tradeTitle} • Rate: $${profile.hourlyRate}/hr",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFFBEB)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Image(
              painter = painterResource(id = R.drawable.partner_hero_banner),
              contentDescription = "Partner Pro Hero",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(86.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            )
          }
        }
      }
    }

    // Performance Stats Grid
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
      ) {
        Text(
          text = "Platform Performance",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          PartnerStatCard(
            title = "Total Earnings",
            value = "$${totalEarnings.toInt()}",
            icon = Icons.Default.AttachMoney,
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
          )
          PartnerStatCard(
            title = "New Orders",
            value = "${receivedOrders.size}",
            icon = Icons.Default.NotificationsActive,
            color = Color(0xFFD97706),
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          PartnerStatCard(
            title = "Active Jobs",
            value = "${activeOrders.size}",
            icon = Icons.Default.HourglassTop,
            color = Color(0xFF4F46E5),
            modifier = Modifier.weight(1f)
          )
          PartnerStatCard(
            title = "Completed",
            value = "${completedOrders.size + 148}",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF0D9488),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Profile Completion Alert Bar
    item {
      Spacer(modifier = Modifier.height(12.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .clickable { onGoToProfile() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Partner Profile Completion",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
              )
            }
            Text(
              text = "${profile.completionPercentage}%",
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary,
              style = MaterialTheme.typography.titleSmall
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          LinearProgressIndicator(
            progress = { profile.completionPercentage / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF0D9488),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Keep your hourly rate, trade skills, and service bio updated to win more job orders.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Live Orders Received Section Header
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Incoming Orders Received",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          if (receivedOrders.isNotEmpty()) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFEF4444))
                .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${receivedOrders.size} NEW",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            }
          }
        }

        Text(
          text = "View All",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .clickable { onGoToOrdersReceived() }
            .padding(4.dp)
        )
      }
    }

    // Orders Received Quick Feed
    if (receivedOrders.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text("No Pending Orders Received", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              "New bookings placed by customers in your categories will appear here automatically.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(receivedOrders.take(2)) { order ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = order.serviceTaskTitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
              )
              OrderStatusBadge(statusString = order.status)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "From: ${order.customerName} • ${order.scheduleDate}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Address: ${order.serviceAddress}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Payout: $${order.totalEstimatedPrice.toInt()}",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
              )

              Button(
                onClick = { onAcceptOrder(order.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text("Accept Order", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun PartnerStatCard(
  title: String,
  value: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(22.dp))
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
      }
    }
  }
}

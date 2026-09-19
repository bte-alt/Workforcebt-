package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus

data class OrderLifecycleStage(
  val status: OrderStatus,
  val title: String,
  val description: String,
  val icon: ImageVector,
  val targetProgress: Float
)

val ORDER_LIFECYCLE_STAGES = listOf(
  OrderLifecycleStage(
    status = OrderStatus.RECEIVED,
    title = "Received",
    description = "Order received & queued. Helper notified.",
    icon = Icons.Default.Inventory2,
    targetProgress = 0.15f
  ),
  OrderLifecycleStage(
    status = OrderStatus.ACCEPTED,
    title = "Confirmed",
    description = "Partner confirmed booking & schedule.",
    icon = Icons.Default.ThumbUp,
    targetProgress = 0.45f
  ),
  OrderLifecycleStage(
    status = OrderStatus.IN_PROGRESS,
    title = "In Progress",
    description = "Helper on site. Work actively underway.",
    icon = Icons.Default.Construction,
    targetProgress = 0.75f
  ),
  OrderLifecycleStage(
    status = OrderStatus.COMPLETED,
    title = "Completed",
    description = "Work finished, inspected & certified.",
    icon = Icons.Default.CheckCircle,
    targetProgress = 1.0f
  )
)

/**
 * Dedicated status tracker component for orders using a progress bar to visually
 * represent the lifecycle of a booking from 'received' to 'complete'.
 */
@Composable
fun OrderStatusTracker(
  statusString: String,
  modifier: Modifier = Modifier,
  showStageDetails: Boolean = true
) {
  val currentStatus = runCatching { OrderStatus.valueOf(statusString) }.getOrDefault(OrderStatus.RECEIVED)
  val isCancelled = currentStatus == OrderStatus.CANCELLED

  if (isCancelled) {
    CancelledTrackerView(modifier = modifier)
    return
  }

  val activeStageIndex = ORDER_LIFECYCLE_STAGES.indexOfFirst { it.status == currentStatus }.let {
    if (it == -1) 0 else it
  }
  val activeStage = ORDER_LIFECYCLE_STAGES[activeStageIndex]

  // Animate progress smoothly between lifecycle stages
  val animatedProgress by animateFloatAsState(
    targetValue = activeStage.targetProgress,
    animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
    label = "OrderLifecycleProgress"
  )

  // Gentle pulse on the active stage indicator
  val infiniteTransition = rememberInfiniteTransition(label = "TrackerPulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (currentStatus != OrderStatus.COMPLETED) 1.15f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "IndicatorPulse"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("order_status_tracker")
      .clip(RoundedCornerShape(14.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
      .padding(14.dp)
  ) {
    // Header Row: Status Label & Percentage Progress
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (currentStatus == OrderStatus.COMPLETED) Color(0xFF10B981) else MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Lifecycle: ${activeStage.title}",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Text(
        text = "${(animatedProgress * 100).toInt()}%",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.ExtraBold,
        color = if (currentStatus == OrderStatus.COMPLETED) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Animated Progress Bar Layer
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .testTag("order_progress_bar")
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(animatedProgress)
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(
            Brush.horizontalGradient(
              colors = when (currentStatus) {
                OrderStatus.COMPLETED -> listOf(Color(0xFF0D9488), Color(0xFF10B981))
                OrderStatus.IN_PROGRESS -> listOf(Color(0xFF0D9488), Color(0xFF4F46E5))
                else -> listOf(Color(0xFFD97706), Color(0xFF0D9488))
              }
            )
          )
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Step Checkpoints Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      ORDER_LIFECYCLE_STAGES.forEachIndexed { index, stage ->
        val isPassed = index < activeStageIndex
        val isCurrent = index == activeStageIndex

        val stageCircleBg by animateColorAsState(
          targetValue = when {
            isPassed -> Color(0xFF10B981)
            isCurrent -> if (stage.status == OrderStatus.COMPLETED) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
          },
          label = "StageColor"
        )

        val stageIconColor = when {
          isPassed || isCurrent -> Color.White
          else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        }

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.width(68.dp)
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .scale(if (isCurrent) pulseScale else 1f)
              .clip(CircleShape)
              .background(stageCircleBg),
            contentAlignment = Alignment.Center
          ) {
            if (isPassed) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "${stage.title} completed",
                tint = stageIconColor,
                modifier = Modifier.size(14.dp)
              )
            } else {
              Icon(
                imageVector = stage.icon,
                contentDescription = stage.title,
                tint = stageIconColor,
                modifier = Modifier.size(13.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = stage.title,
            fontSize = 10.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = if (isPassed || isCurrent) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
          )
        }
      }
    }

    // Contextual Description banner
    if (showStageDetails) {
      Spacer(modifier = Modifier.height(10.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = activeStage.icon,
            contentDescription = null,
            tint = if (currentStatus == OrderStatus.COMPLETED) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = activeStage.description,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
private fun CancelledTrackerView(modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFFEE2E2))
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      Icons.Default.Cancel,
      contentDescription = "Order Cancelled",
      tint = Color(0xFFDC2626),
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = "Booking Cancelled",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color(0xFF991B1B)
      )
      Text(
        text = "This order lifecycle was terminated. You can place a new booking anytime.",
        fontSize = 11.sp,
        color = Color(0xFFB91C1C)
      )
    }
  }
}

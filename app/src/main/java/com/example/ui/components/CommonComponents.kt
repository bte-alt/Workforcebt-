package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.AppMode

@Composable
fun TopMarketplaceBar(
  currentMode: AppMode,
  onModeSwitch: (AppMode) -> Unit,
  isPartnerOnline: Boolean,
  onPartnerOnlineToggle: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    shadowElevation = 2.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // App Identity Brand
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (currentMode == AppMode.CUSTOMER) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (currentMode == AppMode.CUSTOMER) Icons.Default.Person else Icons.Default.Engineering,
              contentDescription = "HelperHub Brand",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "HelperHub",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (currentMode == AppMode.CUSTOMER) "Customer Portal" else "Partner Pro Dashboard",
              style = MaterialTheme.typography.labelSmall,
              color = if (currentMode == AppMode.CUSTOMER) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        // Two-Side Switch Pill
        TwoSidedModePill(
          currentMode = currentMode,
          onModeSelected = onModeSwitch
        )
      }

      // Partner Status Strip when in Partner Mode
      if (currentMode == AppMode.PARTNER) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isPartnerOnline) Color(0xFF10B981) else Color(0xFFEF4444))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isPartnerOnline) "Online • Ready for Job Orders" else "Offline • Not receiving new orders",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(
            checked = isPartnerOnline,
            onCheckedChange = onPartnerOnlineToggle,
            modifier = Modifier.testTag("partner_availability_switch"),
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = Color(0xFF10B981)
            )
          )
        }
      }
    }
  }
}

@Composable
fun TwoSidedModePill(
  currentMode: AppMode,
  onModeSelected: (AppMode) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(24.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
      .padding(3.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      ModePillItem(
        title = "Customer",
        icon = Icons.Default.Person,
        isSelected = currentMode == AppMode.CUSTOMER,
        selectedColor = MaterialTheme.colorScheme.primary,
        onClick = { onModeSelected(AppMode.CUSTOMER) },
        testTag = "mode_customer_tab"
      )
      Spacer(modifier = Modifier.width(2.dp))
      ModePillItem(
        title = "Partner",
        icon = Icons.Default.Engineering,
        isSelected = currentMode == AppMode.PARTNER,
        selectedColor = MaterialTheme.colorScheme.secondary,
        onClick = { onModeSelected(AppMode.PARTNER) },
        testTag = "mode_partner_tab"
      )
    }
  }
}

@Composable
private fun ModePillItem(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  selectedColor: Color,
  onClick: () -> Unit,
  testTag: String
) {
  val backgroundColor by animateColorAsState(
    targetValue = if (isSelected) selectedColor else Color.Transparent,
    label = "pillBg"
  )
  val contentColor by animateColorAsState(
    targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "pillContent"
  )

  Row(
    modifier = Modifier
      .testTag(testTag)
      .clip(RoundedCornerShape(20.dp))
      .background(backgroundColor)
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = contentColor,
      modifier = Modifier.size(15.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = contentColor
    )
  }
}

@Composable
fun OrderStatusBadge(
  statusString: String,
  modifier: Modifier = Modifier
) {
  val status = runCatching { OrderStatus.valueOf(statusString) }.getOrDefault(OrderStatus.RECEIVED)
  val (badgeBg, badgeFg, icon) = when (status) {
    OrderStatus.RECEIVED -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), Icons.Default.PendingActions)
    OrderStatus.ACCEPTED -> Triple(Color(0xFFCCFBF1), Color(0xFF0F766E), Icons.Default.CheckCircle)
    OrderStatus.IN_PROGRESS -> Triple(Color(0xFFE0E7FF), Color(0xFF4338CA), Icons.Default.HourglassTop)
    OrderStatus.COMPLETED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.CheckCircle)
    OrderStatus.CANCELLED -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), Icons.Default.PendingActions)
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(badgeBg)
      .padding(horizontal = 8.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = status.displayName,
        tint = badgeFg,
        modifier = Modifier.size(13.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = status.displayName,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = badgeFg
      )
    }
  }
}

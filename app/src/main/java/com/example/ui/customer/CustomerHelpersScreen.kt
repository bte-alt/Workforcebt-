package com.example.ui.customer

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HelperPartner

@Composable
fun CustomerHelpersScreen(
  helpers: List<HelperPartner>,
  onBookHelper: (HelperPartner) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf<String?>("All") }

  val filterOptions = listOf("All", "Electrical Works", "Home Cleaning", "Plumbing Care", "Carpentry & Fixes", "Appliance Repair")

  val displayedHelpers = if (selectedFilter == null || selectedFilter == "All") {
    helpers
  } else {
    helpers.filter { it.primaryCategory.contains(selectedFilter!!, ignoreCase = true) }
  }

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
        Text(
          text = "Find Trusted Helpers",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Browse background-verified professionals ready for your job",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Filter Chips
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filterOptions) { filter ->
          val isSelected = selectedFilter == filter
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
              )
              .clickable { selectedFilter = filter }
              .padding(horizontal = 14.dp, vertical = 7.dp)
          ) {
            Text(
              text = filter,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Helpers Count
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Available Helpers (${displayedHelpers.size})",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Sorted by Highest Rated",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // List of Helpers
    items(displayedHelpers) { helper ->
      HelperListItem(
        helper = helper,
        onBook = { onBookHelper(helper) }
      )
    }
  }
}

@Composable
fun HelperListItem(
  helper: HelperPartner,
  onBook: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(Color(helper.avatarColorHex)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = helper.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = helper.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              if (helper.isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  Icons.Default.Verified,
                  contentDescription = "Verified Helper",
                  tint = Color(0xFF0284C7),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
            Text(
              text = helper.tradeTitle,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "${helper.rating}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = " (${helper.reviewCount} reviews) • ",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Icon(
                Icons.Default.Work,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "${helper.completedJobsCount} jobs",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        // Hourly Price Tag
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "$${helper.hourlyRate}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
          Text(
            text = "/ hour",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = helper.bio,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Availability Status
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(if (helper.isAvailable) Color(0xFF10B981) else Color(0xFFEF4444))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (helper.isAvailable) "Available for Booking" else "Currently Busy",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (helper.isAvailable) Color(0xFF047857) else Color(0xFFB91C1C)
          )
        }

        Button(
          onClick = onBook,
          enabled = helper.isAvailable,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("book_button_${helper.id}")
        ) {
          Text("Book Helper", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }
  }
}

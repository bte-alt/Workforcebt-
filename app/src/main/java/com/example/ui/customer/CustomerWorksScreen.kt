package com.example.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HelperPartner
import com.example.data.model.WorkCategory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerWorksScreen(
  categories: List<WorkCategory>,
  topHelpers: List<HelperPartner>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onBookCategory: (WorkCategory, String) -> Unit,
  onBookHelper: (HelperPartner) -> Unit,
  onExploreAllHelpers: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

  val filteredCategories = categories.filter { cat ->
    val matchesSearch = searchQuery.isBlank() ||
      cat.name.contains(searchQuery, ignoreCase = true) ||
      cat.subtitle.contains(searchQuery, ignoreCase = true) ||
      cat.popularTasks.any { it.contains(searchQuery, ignoreCase = true) }
    val matchesFilter = selectedCategoryFilter == null || cat.id == selectedCategoryFilter
    matchesSearch && matchesFilter
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    // Hero Banner Card
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.linearGradient(
              listOf(Color(0xFF0F766E), Color(0xFF115E59))
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
                  .background(Color(0xFFCCFBF1).copy(alpha = 0.2f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "VERIFIED LOCAL HELPERS",
                  color = Color(0xFFCCFBF1),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Book Skilled Help For Any Home Work",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Top-rated electricians, cleaners, plumbers & handymen ready today.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE6FFFA)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Banner Image
            Image(
              painter = painterResource(id = R.drawable.service_hero_banner),
              contentDescription = "Service Helpers Hero",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(92.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            )
          }
        }
      }
    }

    // Search Box
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Search works, wiring, cleaning, leak fix...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .testTag("customer_search_input"),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          focusedBorderColor = MaterialTheme.colorScheme.primary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        singleLine = true
      )
    }

    // Category Filter Chips Row
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          FilterChipPill(
            label = "All Works",
            isSelected = selectedCategoryFilter == null,
            onClick = { selectedCategoryFilter = null }
          )
        }
        items(categories) { cat ->
          FilterChipPill(
            label = cat.name,
            isSelected = selectedCategoryFilter == cat.id,
            onClick = {
              selectedCategoryFilter = if (selectedCategoryFilter == cat.id) null else cat.id
            }
          )
        }
      }
    }

    // Work Categories Title
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Explore Works & Services",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "${filteredCategories.size} Categories",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }

    // Categories List Cards
    items(filteredCategories) { category ->
      WorkCategoryCard(
        category = category,
        onBookCategory = { task -> onBookCategory(category, task) }
      )
    }

    // Featured Helpers Row
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Featured Helpers Available",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Verified background-checked partners",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Text(
          text = "View All",
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .clickable { onExploreAllHelpers() }
            .padding(4.dp)
        )
      }
    }

    // Horizontal top helpers list
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(topHelpers.take(4)) { helper ->
          FeaturedHelperCard(
            helper = helper,
            onBook = { onBookHelper(helper) }
          )
        }
      }
    }
  }
}

@Composable
private fun FilterChipPill(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .background(
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
      )
      .clickable(onClick = onClick)
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkCategoryCard(
  category: WorkCategory,
  onBookCategory: (taskTitle: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val icon = getCategoryIcon(category.iconName)

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
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = category.name,
              tint = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = category.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "From $${category.basePricePerHour}/hr",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }

        Button(
          onClick = { onBookCategory(category.popularTasks.firstOrNull() ?: category.name) },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.testTag("book_category_${category.id}")
        ) {
          Text("Book", fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(4.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = category.subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Popular Tasks:",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
      )

      Spacer(modifier = Modifier.height(4.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        category.popularTasks.forEach { task ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .clickable { onBookCategory(task) }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "+ $task",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun FeaturedHelperCard(
  helper: HelperPartner,
  onBook: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(220.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color(helper.avatarColorHex)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = helper.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = helper.name,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            if (helper.isVerified) {
              Spacer(modifier = Modifier.width(3.dp))
              Icon(
                Icons.Default.Verified,
                contentDescription = "Verified",
                tint = Color(0xFF0284C7),
                modifier = Modifier.size(14.dp)
              )
            }
          }
          Text(
            text = helper.primaryCategory,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${helper.rating}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = " (${helper.reviewCount})",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Text(
          text = "$${helper.hourlyRate}/hr",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Button(
        onClick = onBook,
        modifier = Modifier
          .fillMaxWidth()
          .height(36.dp)
          .testTag("book_helper_${helper.id}"),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
      ) {
        Text("Book Helper", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

fun getCategoryIcon(name: String): ImageVector {
  return when (name.lowercase()) {
    "cleaning" -> Icons.Default.CleaningServices
    "electrical" -> Icons.Default.Bolt
    "plumbing" -> Icons.Default.Plumbing
    "carpentry", "handyman" -> Icons.Default.Handyman
    "appliance" -> Icons.Default.Build
    "moving" -> Icons.Default.LocalShipping
    "painting" -> Icons.Default.FormatPaint
    "garden" -> Icons.Default.Yard
    else -> Icons.Default.Handyman
  }
}

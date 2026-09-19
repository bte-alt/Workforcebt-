package com.example.ui.partner

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PartnerProfile

@Composable
fun PartnerProfileScreen(
  profile: PartnerProfile?,
  onSaveProfile: (PartnerProfile) -> Unit,
  onToggleAvailability: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val current = profile ?: PartnerProfile()

  var fullName by remember(current) { mutableStateOf(current.fullName) }
  var tradeTitle by remember(current) { mutableStateOf(current.tradeTitle) }
  var email by remember(current) { mutableStateOf(current.email) }
  var phone by remember(current) { mutableStateOf(current.phone) }
  var bio by remember(current) { mutableStateOf(current.bio) }
  var hourlyRate by remember(current) { mutableIntStateOf(current.hourlyRate) }
  var experienceYears by remember(current) { mutableIntStateOf(current.experienceYears) }
  var serviceCategories by remember(current) { mutableStateOf(current.serviceCategories) }
  var isAvailable by remember(current) { mutableStateOf(current.isAvailableForJobs) }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
  ) {
    // Partner Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(62.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifBlank { "MV" },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = fullName.ifBlank { "Partner Profile" },
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
          }
          Text(
            text = tradeTitle.ifBlank { "Certified Partner Technician" },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "4.95 Rating • 214 Completed Works", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Availability Toggle Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (isAvailable) "Available for Work Orders" else "Set to Busy / Off Duty",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = if (isAvailable) Color(0xFF047857) else Color(0xFFB91C1C)
            )
            Text(
              text = if (isAvailable) "You appear in customer searches" else "Hidden from new incoming bookings",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(
            checked = isAvailable,
            onCheckedChange = {
              isAvailable = it
              onToggleAvailability(it)
            },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = Color(0xFF10B981)
            ),
            modifier = Modifier.testTag("partner_profile_availability_toggle")
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Profile Completion Progress Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Profile Completeness",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall
            )
            Text(
              text = "${current.completionPercentage}%",
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.secondary,
              style = MaterialTheme.typography.titleSmall
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          LinearProgressIndicator(
            progress = { current.completionPercentage / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Completed partner profiles receive 3.5x more booking orders from customers.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Complete Profile Form
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Complete Partner Profile",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Set your trade title, hourly rate, and expertise description.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("partner_name_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = tradeTitle,
            onValueChange = { tradeTitle = it },
            label = { Text("Trade Title / Specialty") },
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("partner_trade_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = if (hourlyRate > 0) "$hourlyRate" else "",
              onValueChange = { hourlyRate = it.toIntOrNull() ?: 0 },
              label = { Text("Hourly Rate ($)") },
              leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
              modifier = Modifier
                .weight(1f)
                .testTag("partner_rate_input"),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              shape = RoundedCornerShape(10.dp),
              singleLine = true
            )

            OutlinedTextField(
              value = if (experienceYears > 0) "$experienceYears" else "",
              onValueChange = { experienceYears = it.toIntOrNull() ?: 0 },
              label = { Text("Experience (Yrs)") },
              leadingIcon = { Icon(Icons.Default.Timeline, contentDescription = null) },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              shape = RoundedCornerShape(10.dp),
              singleLine = true
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = serviceCategories,
            onValueChange = { serviceCategories = it },
            label = { Text("Service Categories (Comma separated)") },
            leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number for Customer Contact") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Helper Bio & Experience Highlights") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("partner_bio_input"),
            shape = RoundedCornerShape(10.dp),
            minLines = 3,
            maxLines = 5
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              onSaveProfile(
                current.copy(
                  fullName = fullName,
                  tradeTitle = tradeTitle,
                  email = email,
                  phone = phone,
                  bio = bio,
                  hourlyRate = hourlyRate,
                  experienceYears = experienceYears,
                  serviceCategories = serviceCategories,
                  isAvailableForJobs = isAvailable
                )
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("partner_save_profile_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Partner Profile", fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Partner Verifications Badge List
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Verification & Credentials",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
          )
          Spacer(modifier = Modifier.height(10.dp))

          VerificationItem(title = "Government ID Checked", subtitle = "Driver's license verified")
          Spacer(modifier = Modifier.height(6.dp))
          VerificationItem(title = "Criminal Background Clearance", subtitle = "Annual background screening passed")
          Spacer(modifier = Modifier.height(6.dp))
          VerificationItem(title = "Trade Certification Validated", subtitle = "Electrical & technical license on file")
        }
      }
    }
  }
}

@Composable
private fun VerificationItem(title: String, subtitle: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
      Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

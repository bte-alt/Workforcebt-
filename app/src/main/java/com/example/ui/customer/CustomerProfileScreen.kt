package com.example.ui.customer

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.CustomerProfile
import com.example.ui.AppMode

@Composable
fun CustomerProfileScreen(
  profile: CustomerProfile?,
  onSaveProfile: (CustomerProfile) -> Unit,
  onSwitchToPartner: () -> Unit,
  modifier: Modifier = Modifier
) {
  val current = profile ?: CustomerProfile()

  var fullName by remember(current) { mutableStateOf(current.fullName) }
  var email by remember(current) { mutableStateOf(current.email) }
  var phone by remember(current) { mutableStateOf(current.phone) }
  var address by remember(current) { mutableStateOf(current.defaultAddress) }
  var payment by remember(current) { mutableStateOf(current.preferredPayment) }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifBlank { "AR" },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = fullName.ifBlank { "Customer Profile" },
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
          }
          Text(
            text = email.ifBlank { "Verified Customer Account" },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // Complete Profile Form Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Complete Customer Profile",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Keep your contact info and default address ready for fast bookings.",
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
              .testTag("customer_name_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("customer_phone_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Default Service Address") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("customer_default_address_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = payment,
            onValueChange = { payment = it },
            label = { Text("Preferred Payment Method") },
            leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              onSaveProfile(
                current.copy(
                  fullName = fullName,
                  email = email,
                  phone = phone,
                  defaultAddress = address,
                  preferredPayment = payment,
                  isProfileCompleted = true
                )
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("customer_save_profile_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Profile Changes", fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // Switch to Partner Callout
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Engineering, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Are you a skilled helper or service partner?",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Switch to the Partner Pro dashboard to view incoming customer orders, manage bookings, and earn.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedButton(
            onClick = onSwitchToPartner,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("switch_to_partner_profile_action")
          ) {
            Text("Open Partner Dashboard", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

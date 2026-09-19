package com.example.ui.customer

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HelperPartner
import com.example.data.model.WorkCategory
import com.example.ui.BookingDraft
import com.example.ui.BookingStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerBookingSheet(
  isOpen: Boolean,
  sheetState: SheetState,
  currentStep: BookingStep,
  draft: BookingDraft,
  categories: List<WorkCategory>,
  availableHelpers: List<HelperPartner>,
  onUpdateDraft: ((BookingDraft) -> BookingDraft) -> Unit,
  onNextStep: () -> Unit,
  onPreviousStep: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (!isOpen) return

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .padding(bottom = 28.dp)
    ) {
      // Header: Title, Step Counter, Close Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = when (currentStep) {
              BookingStep.SELECT_SERVICE -> "Step 1: Choose Work & Helper"
              BookingStep.SCHEDULE_LOCATION -> "Step 2: Schedule & Address"
              BookingStep.REVIEW_CONFIRM -> "Step 3: Review & Confirm"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = when (currentStep) {
              BookingStep.SELECT_SERVICE -> "Select service type and certified helper"
              BookingStep.SCHEDULE_LOCATION -> "Pick timing and location details"
              BookingStep.REVIEW_CONFIRM -> "Review pricing and place order"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(onClick = onDismiss, modifier = Modifier.testTag("booking_close_button")) {
          Icon(Icons.Default.Close, contentDescription = "Close Booking")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Progress Stepper Indicators
      BookingStepIndicator(currentStep = currentStep)

      Spacer(modifier = Modifier.height(16.dp))

      // Content for active step with scroll
      Box(
        modifier = Modifier
          .weight(1f, fill = false)
          .verticalScroll(rememberScrollState())
      ) {
        AnimatedContent(
          targetState = currentStep,
          label = "BookingStepAnimation"
        ) { step ->
          when (step) {
            BookingStep.SELECT_SERVICE -> {
              Step1SelectServiceContent(
                draft = draft,
                categories = categories,
                availableHelpers = availableHelpers,
                onUpdateDraft = onUpdateDraft
              )
            }
            BookingStep.SCHEDULE_LOCATION -> {
              Step2ScheduleLocationContent(
                draft = draft,
                onUpdateDraft = onUpdateDraft
              )
            }
            BookingStep.REVIEW_CONFIRM -> {
              Step3ReviewConfirmContent(
                draft = draft
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Bottom Action Controls: Back and "Next" / "Confirm Order"
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (currentStep != BookingStep.SELECT_SERVICE) {
          OutlinedButton(
            onClick = onPreviousStep,
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("booking_back_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back")
          }
        }

        Button(
          onClick = onNextStep,
          modifier = Modifier
            .weight(if (currentStep == BookingStep.SELECT_SERVICE) 2f else 1.5f)
            .height(48.dp)
            .testTag(if (currentStep == BookingStep.REVIEW_CONFIRM) "booking_confirm_button" else "booking_next_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
          )
        ) {
          Text(
            text = if (currentStep == BookingStep.REVIEW_CONFIRM) "Confirm Booking ($${draft.estimatedTotal.toInt()})" else "Next",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = if (currentStep == BookingStep.REVIEW_CONFIRM) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun BookingStepIndicator(
  currentStep: BookingStep,
  modifier: Modifier = Modifier
) {
  val steps = listOf(BookingStep.SELECT_SERVICE, BookingStep.SCHEDULE_LOCATION, BookingStep.REVIEW_CONFIRM)
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    steps.forEachIndexed { index, step ->
      val isActive = step == currentStep
      val isCompleted = step.ordinal < currentStep.ordinal

      Box(
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(
            when {
              isActive -> MaterialTheme.colorScheme.primary
              isCompleted -> Color(0xFF10B981)
              else -> MaterialTheme.colorScheme.surfaceVariant
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isCompleted) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        } else {
          Text(
            text = "${index + 1}",
            color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }

      if (index < steps.size - 1) {
        Box(
          modifier = Modifier
            .width(40.dp)
            .height(3.dp)
            .background(
              if (currentStep.ordinal > index) Color(0xFF10B981) else MaterialTheme.colorScheme.surfaceVariant
            )
        )
      }
    }
  }
}

@Composable
private fun Step1SelectServiceContent(
  draft: BookingDraft,
  categories: List<WorkCategory>,
  availableHelpers: List<HelperPartner>,
  onUpdateDraft: ((BookingDraft) -> BookingDraft) -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = "Service Work Category",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(categories) { cat ->
        val isSelected = draft.category?.id == cat.id
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
            .border(
              1.dp,
              if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
              RoundedCornerShape(12.dp)
            )
            .clickable {
              val suggestedTask = cat.popularTasks.firstOrNull() ?: cat.name
              val matchingHelper = availableHelpers.firstOrNull { it.primaryCategory == cat.name }
              onUpdateDraft { it.copy(category = cat, taskTitle = suggestedTask, helper = matchingHelper ?: it.helper) }
            }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = cat.name,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Specific Task Input
    Text(
      text = "Specific Task Title",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = draft.taskTitle,
      onValueChange = { newTitle -> onUpdateDraft { it.copy(taskTitle = newTitle) } },
      placeholder = { Text("e.g., Fix leaking faucet, Install ceiling fan...") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("booking_task_input"),
      shape = RoundedCornerShape(10.dp),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Select Helper
    Text(
      text = "Assign Helper Partner",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    val helpersToShow = if (draft.category != null) {
      availableHelpers.filter { it.primaryCategory.contains(draft.category.name, ignoreCase = true) }
        .ifEmpty { availableHelpers }
    } else availableHelpers

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      helpersToShow.take(3).forEach { helper ->
        val isSelected = draft.helper?.id == helper.id
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onUpdateDraft { it.copy(helper = helper) } },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
          ),
          border = if (isSelected) border(1.5.dp, MaterialTheme.colorScheme.primary) else border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Color(helper.avatarColorHex)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = helper.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(helper.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  if (helper.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(13.dp))
                  }
                }
                Text(
                  text = "${helper.tradeTitle} • ⭐ ${helper.rating}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "$${helper.hourlyRate}/hr",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
              )
              if (isSelected) {
                Text("Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun Step2ScheduleLocationContent(
  draft: BookingDraft,
  onUpdateDraft: ((BookingDraft) -> BookingDraft) -> Unit
) {
  val dateOptions = listOf("Today", "Tomorrow", "In 2 Days", "This Weekend")
  val timeSlotOptions = listOf(
    "Morning (9:00 AM - 12:00 PM)",
    "Afternoon (1:00 PM - 4:00 PM)",
    "Evening (5:00 PM - 8:00 PM)",
    "Urgent (Within 90 mins)"
  )

  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = "Select Preferred Date",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(dateOptions) { date ->
        val isSelected = draft.scheduleDate.startsWith(date)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onUpdateDraft { it.copy(scheduleDate = "$date, 2:00 PM") } }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = date,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Arrival Time Window",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      timeSlotOptions.forEach { slot ->
        val isSelected = draft.timeSlot == slot
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onUpdateDraft { it.copy(timeSlot = slot) } }
            .padding(horizontal = 12.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Default.Schedule,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = slot,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Service Address
    Text(
      text = "Service Address",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = draft.address,
      onValueChange = { newAddr -> onUpdateDraft { it.copy(address = newAddr) } },
      leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("booking_address_input"),
      shape = RoundedCornerShape(10.dp),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Estimated Hours Counter
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Estimated Work Hours", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("Can adjust based on actual time", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
          onClick = { if (draft.estimatedHours > 1) onUpdateDraft { it.copy(estimatedHours = it.estimatedHours - 1) } },
          shape = CircleShape,
          contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
          modifier = Modifier.size(34.dp)
        ) {
          Text("-", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Text(
          text = "${draft.estimatedHours} hrs",
          modifier = Modifier.padding(horizontal = 12.dp),
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
        OutlinedButton(
          onClick = { if (draft.estimatedHours < 8) onUpdateDraft { it.copy(estimatedHours = it.estimatedHours + 1) } },
          shape = CircleShape,
          contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
          modifier = Modifier.size(34.dp)
        ) {
          Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Work Notes
    Text("Special Instructions / Access Notes (Optional)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedTextField(
      value = draft.notes,
      onValueChange = { notes -> onUpdateDraft { it.copy(notes = notes) } },
      placeholder = { Text("e.g., Gate code 4490, please call on arrival...") },
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(10.dp),
      maxLines = 3
    )
  }
}

@Composable
private fun Step3ReviewConfirmContent(
  draft: BookingDraft
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Summary Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = draft.taskTitle,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = draft.category?.name ?: "Home Works",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(12.dp))

        // Helper Row
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Assigned Helper: ${draft.helper?.name ?: "Top Verified Pro"}",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
            Text(
              text = "Rate: $${draft.hourlyRate}/hour • ⭐ ${draft.helper?.rating ?: 4.9f}",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Date & Location Row
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "${draft.scheduleDate} (${draft.timeSlot})", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = draft.address, fontSize = 12.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Pricing Breakdown
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Price Estimate", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Helper Labor ($${draft.hourlyRate}/hr × ${draft.estimatedHours} hrs)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("$${draft.estimatedTotal.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("HelperHub Platform Guarantee", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("FREE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Total Estimated", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Text(
            text = "$${draft.estimatedTotal.toInt()}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Guarantee Note
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFCCFBF1).copy(alpha = 0.5f))
        .padding(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF0F766E), modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Your booking triggers an immediate Order Received alert to the helper partner. No charge until work is completed.",
        fontSize = 11.sp,
        color = Color(0xFF115E59),
        lineHeight = 15.sp
      )
    }
  }
}

private fun border(width: androidx.compose.ui.unit.Dp, color: Color) =
  androidx.compose.foundation.BorderStroke(width, color)

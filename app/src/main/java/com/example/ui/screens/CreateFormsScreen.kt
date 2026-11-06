package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.SolutionEntity
import com.example.ui.CreateType
import com.example.ui.EditItem
import com.example.ui.XaldoonViewModel
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.theme.EmeraldPrimary

@Composable
fun CreateHubScreen(
  viewModel: XaldoonViewModel,
  modifier: Modifier = Modifier
) {
  val activeCreateType by viewModel.activeCreateType.collectAsState()
  val editingItem by viewModel.editingItem.collectAsState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    when {
      editingItem != null -> {
        when (val item = editingItem!!) {
          is EditItem.EditNeed -> NeedForm(
            viewModel = viewModel,
            initialNeed = item.need,
            onDismiss = { viewModel.cancelEdit() }
          )
          is EditItem.EditSolution -> SolutionForm(
            viewModel = viewModel,
            initialSolution = item.solution,
            onDismiss = { viewModel.cancelEdit() }
          )
          is EditItem.EditService -> ServiceForm(
            viewModel = viewModel,
            initialService = item.service,
            onDismiss = { viewModel.cancelEdit() }
          )
          is EditItem.EditOpportunity -> OpportunityForm(
            viewModel = viewModel,
            initialOpportunity = item.opportunity,
            onDismiss = { viewModel.cancelEdit() }
          )
        }
      }
      activeCreateType != null -> {
        when (activeCreateType!!) {
          CreateType.NEED -> NeedForm(
            viewModel = viewModel,
            initialNeed = null,
            onDismiss = { viewModel.cancelCreate() }
          )
          CreateType.SOLUTION -> SolutionForm(
            viewModel = viewModel,
            initialSolution = null,
            onDismiss = { viewModel.cancelCreate() }
          )
          CreateType.SERVICE -> ServiceForm(
            viewModel = viewModel,
            initialService = null,
            onDismiss = { viewModel.cancelCreate() }
          )
          CreateType.OPPORTUNITY -> OpportunityForm(
            viewModel = viewModel,
            initialOpportunity = null,
            onDismiss = { viewModel.cancelCreate() }
          )
        }
      }
      else -> {
        CreateSelectionHub(
          onSelectType = { viewModel.startCreate(it) }
        )
      }
    }
  }
}

@Composable
private fun CreateSelectionHub(
  onSelectType: (CreateType) -> Unit
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = stringResource(R.string.create_hub_title),
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = stringResource(R.string.create_hub_subtitle),
      style = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 13.sp
      ),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    CreationOptionCard(
      title = stringResource(R.string.post_a_need),
      description = stringResource(R.string.post_a_need_desc),
      icon = Icons.Default.AddAlert,
      badge = "Challenge / Demand",
      color = EmeraldPrimary,
      testTag = "create_need_option_card",
      onClick = { onSelectType(CreateType.NEED) }
    )

    Spacer(modifier = Modifier.height(14.dp))

    CreationOptionCard(
      title = stringResource(R.string.offer_a_solution),
      description = stringResource(R.string.offer_a_solution_desc),
      icon = Icons.Default.Lightbulb,
      badge = "Innovation / Answer",
      color = Color(0xFFD97706),
      testTag = "create_solution_option_card",
      onClick = { onSelectType(CreateType.SOLUTION) }
    )

    Spacer(modifier = Modifier.height(14.dp))

    CreationOptionCard(
      title = stringResource(R.string.list_a_service),
      description = stringResource(R.string.list_a_service_desc),
      icon = Icons.Default.Work,
      badge = "Expertise / Freelance",
      color = Color(0xFF1D4ED8),
      testTag = "create_service_option_card",
      onClick = { onSelectType(CreateType.SERVICE) }
    )

    Spacer(modifier = Modifier.height(14.dp))

    CreationOptionCard(
      title = stringResource(R.string.publish_opportunity),
      description = stringResource(R.string.publish_opportunity_desc),
      icon = Icons.Default.BusinessCenter,
      badge = "Jobs / Partnerships",
      color = Color(0xFF7C3AED),
      testTag = "create_opportunity_option_card",
      onClick = { onSelectType(CreateType.OPPORTUNITY) }
    )

    Spacer(modifier = Modifier.height(20.dp))
  }
}

@Composable
private fun CreationOptionCard(
  title: String,
  description: String,
  icon: ImageVector,
  badge: String,
  color: Color,
  testTag: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag(testTag),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier.padding(18.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = color.copy(alpha = 0.1f)
        ) {
          Text(
            text = badge,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
          ),
          lineHeight = 16.sp
        )
      }
    }
  }
}

// ----------------- NEED FORM -----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NeedForm(
  viewModel: XaldoonViewModel,
  initialNeed: NeedEntity?,
  onDismiss: () -> Unit
) {
  var title by remember { mutableStateOf(initialNeed?.title ?: "") }
  var description by remember { mutableStateOf(initialNeed?.description ?: "") }
  var category by remember { mutableStateOf(initialNeed?.category ?: "Technology & Software") }
  var country by remember { mutableStateOf(initialNeed?.country ?: "Somalia") }
  var city by remember { mutableStateOf(initialNeed?.city ?: "Mogadishu") }
  var contactDetails by remember { mutableStateOf(initialNeed?.contactDetails ?: "") }
  var status by remember { mutableStateOf(initialNeed?.status ?: "Open") }

  var titleError by remember { mutableStateOf<String?>(null) }
  var descError by remember { mutableStateOf<String?>(null) }
  var showDeleteDialog by remember { mutableStateOf(false) }

  val categories = listOf(
    "Technology & Software",
    "Agriculture & Food Security",
    "Energy & Clean Water",
    "Healthcare & Medicine",
    "Business & Trade",
    "Education & Skills",
    "Logistics & Transport",
    "Community & Social Impact"
  )
  val statuses = listOf("Open", "In Progress", "Solved", "Closed")

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (initialNeed == null) stringResource(R.string.form_title_need) else stringResource(R.string.action_edit),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )

      if (initialNeed != null) {
        IconButton(
          onClick = { showDeleteDialog = true },
          modifier = Modifier.testTag("delete_need_btn")
        ) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
      value = title,
      onValueChange = {
        title = it
        if (it.isNotBlank()) titleError = null
      },
      label = { Text(stringResource(R.string.title_field)) },
      isError = titleError != null,
      supportingText = { if (titleError != null) Text(titleError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_need_title"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = description,
      onValueChange = {
        description = it
        if (it.length >= 10) descError = null
      },
      label = { Text(stringResource(R.string.description_field)) },
      isError = descError != null,
      supportingText = { if (descError != null) Text(descError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_need_desc"),
      minLines = 4,
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.category_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      categories.forEach { cat ->
        FilterChip(
          selected = category == cat,
          onClick = { category = cat },
          label = { Text(cat, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.status_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      statuses.forEach { st ->
        FilterChip(
          selected = status == st,
          onClick = { status = st },
          label = { Text(st, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      OutlinedTextField(
        value = country,
        onValueChange = { country = it },
        label = { Text(stringResource(R.string.country_label)) },
        modifier = Modifier.weight(1f).testTag("form_need_country"),
        shape = RoundedCornerShape(10.dp)
      )
      OutlinedTextField(
        value = city,
        onValueChange = { city = it },
        label = { Text(stringResource(R.string.city_label)) },
        modifier = Modifier.weight(1f).testTag("form_need_city"),
        shape = RoundedCornerShape(10.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = contactDetails,
      onValueChange = { contactDetails = it },
      label = { Text(stringResource(R.string.contact_field)) },
      placeholder = { Text("e.g. contact@example.com or +252-...") },
      modifier = Modifier.fillMaxWidth().testTag("form_need_contact"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.weight(1f).height(48.dp),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(stringResource(R.string.action_cancel))
      }

      Button(
        onClick = {
          if (title.isBlank()) {
            titleError = "Title is required"
            return@Button
          }
          if (description.trim().length < 10) {
            descError = "Description must be at least 10 characters"
            return@Button
          }

          viewModel.saveNeed(
            id = initialNeed?.id ?: 0L,
            title = title,
            description = description,
            category = category,
            country = country,
            city = city,
            contactDetails = contactDetails,
            status = status
          )
        },
        modifier = Modifier.weight(1f).height(48.dp).testTag("save_need_submit_btn"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text(stringResource(R.string.save_record), color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }

  if (showDeleteDialog && initialNeed != null) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.confirm_delete_title),
      body = stringResource(R.string.confirm_delete_body),
      onConfirm = {
        viewModel.deleteNeed(initialNeed)
        showDeleteDialog = false
      },
      onDismiss = { showDeleteDialog = false }
    )
  }
}

// ----------------- SOLUTION FORM -----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SolutionForm(
  viewModel: XaldoonViewModel,
  initialSolution: SolutionEntity?,
  onDismiss: () -> Unit
) {
  val needs by viewModel.allNeeds.collectAsState()

  var title by remember { mutableStateOf(initialSolution?.title ?: "") }
  var description by remember { mutableStateOf(initialSolution?.description ?: "") }
  var relatedNeedId by remember { mutableStateOf<Long?>(initialSolution?.relatedNeedId) }
  var skills by remember { mutableStateOf(initialSolution?.skills ?: "") }
  var price by remember { mutableStateOf(initialSolution?.price ?: "") }
  var status by remember { mutableStateOf(initialSolution?.status ?: "Offered") }

  var titleError by remember { mutableStateOf<String?>(null) }
  var descError by remember { mutableStateOf<String?>(null) }
  var showDeleteDialog by remember { mutableStateOf(false) }

  val statuses = listOf("Draft", "Offered", "Accepted", "Completed")
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (initialSolution == null) stringResource(R.string.form_title_solution) else stringResource(R.string.action_edit),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )

      if (initialSolution != null) {
        IconButton(
          onClick = { showDeleteDialog = true },
          modifier = Modifier.testTag("delete_solution_btn")
        ) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
      value = title,
      onValueChange = {
        title = it
        if (it.isNotBlank()) titleError = null
      },
      label = { Text(stringResource(R.string.title_field)) },
      isError = titleError != null,
      supportingText = { if (titleError != null) Text(titleError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_solution_title"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = description,
      onValueChange = {
        description = it
        if (it.length >= 10) descError = null
      },
      label = { Text(stringResource(R.string.description_field)) },
      isError = descError != null,
      supportingText = { if (descError != null) Text(descError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_solution_desc"),
      minLines = 4,
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Optional Related Need Link
    Text(
      text = stringResource(R.string.related_need_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))

    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      FilterChip(
        selected = relatedNeedId == null,
        onClick = { relatedNeedId = null },
        label = { Text("None / Independent") }
      )
      needs.take(4).forEach { n ->
        FilterChip(
          selected = relatedNeedId == n.id,
          onClick = { relatedNeedId = n.id },
          label = { Text(n.title.take(20) + "…", fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
      value = skills,
      onValueChange = { skills = it },
      label = { Text(stringResource(R.string.skills_label)) },
      placeholder = { Text("e.g. CAD, Mechanical, Renewable Energy") },
      modifier = Modifier.fillMaxWidth().testTag("form_solution_skills"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = price,
      onValueChange = { price = it },
      label = { Text(stringResource(R.string.price_field)) },
      placeholder = { Text("e.g. Free / Open Source or $50 blueprint") },
      modifier = Modifier.fillMaxWidth().testTag("form_solution_price"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.status_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      statuses.forEach { st ->
        FilterChip(
          selected = status == st,
          onClick = { status = st },
          label = { Text(st, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.weight(1f).height(48.dp),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(stringResource(R.string.action_cancel))
      }

      Button(
        onClick = {
          if (title.isBlank()) {
            titleError = "Title is required"
            return@Button
          }
          if (description.trim().length < 10) {
            descError = "Description must be at least 10 characters"
            return@Button
          }

          viewModel.saveSolution(
            id = initialSolution?.id ?: 0L,
            title = title,
            description = description,
            relatedNeedId = relatedNeedId,
            skills = skills,
            price = price,
            status = status
          )
        },
        modifier = Modifier.weight(1f).height(48.dp).testTag("save_solution_submit_btn"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text(stringResource(R.string.save_record), color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }

  if (showDeleteDialog && initialSolution != null) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.confirm_delete_title),
      body = stringResource(R.string.confirm_delete_body),
      onConfirm = {
        viewModel.deleteSolution(initialSolution)
        showDeleteDialog = false
      },
      onDismiss = { showDeleteDialog = false }
    )
  }
}

// ----------------- SERVICE FORM -----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServiceForm(
  viewModel: XaldoonViewModel,
  initialService: ServiceEntity?,
  onDismiss: () -> Unit
) {
  var title by remember { mutableStateOf(initialService?.title ?: "") }
  var description by remember { mutableStateOf(initialService?.description ?: "") }
  var category by remember { mutableStateOf(initialService?.category ?: "Technology & Software") }
  var skills by remember { mutableStateOf(initialService?.skills ?: "") }
  var price by remember { mutableStateOf(initialService?.price ?: "") }
  var country by remember { mutableStateOf(initialService?.country ?: "Somalia") }
  var city by remember { mutableStateOf(initialService?.city ?: "Mogadishu") }
  var availabilityStatus by remember { mutableStateOf(initialService?.availabilityStatus ?: "Available") }

  var titleError by remember { mutableStateOf<String?>(null) }
  var descError by remember { mutableStateOf<String?>(null) }
  var showDeleteDialog by remember { mutableStateOf(false) }

  val categories = listOf(
    "Technology & Software",
    "Agriculture & Food Security",
    "Energy & Clean Water",
    "Healthcare & Medicine",
    "Business & Trade",
    "Education & Skills",
    "Logistics & Transport"
  )
  val statuses = listOf("Available", "Busy", "Upon Request")
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (initialService == null) stringResource(R.string.form_title_service) else stringResource(R.string.action_edit),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )

      if (initialService != null) {
        IconButton(
          onClick = { showDeleteDialog = true },
          modifier = Modifier.testTag("delete_service_btn")
        ) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
      value = title,
      onValueChange = {
        title = it
        if (it.isNotBlank()) titleError = null
      },
      label = { Text(stringResource(R.string.title_field)) },
      isError = titleError != null,
      supportingText = { if (titleError != null) Text(titleError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_service_title"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = description,
      onValueChange = {
        description = it
        if (it.length >= 10) descError = null
      },
      label = { Text(stringResource(R.string.description_field)) },
      isError = descError != null,
      supportingText = { if (descError != null) Text(descError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_service_desc"),
      minLines = 3,
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.category_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      categories.forEach { cat ->
        FilterChip(
          selected = category == cat,
          onClick = { category = cat },
          label = { Text(cat, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
      value = skills,
      onValueChange = { skills = it },
      label = { Text(stringResource(R.string.skills_label)) },
      placeholder = { Text("e.g. Solar PV, Systems Engineering") },
      modifier = Modifier.fillMaxWidth().testTag("form_service_skills"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = price,
      onValueChange = { price = it },
      label = { Text(stringResource(R.string.price_field)) },
      placeholder = { Text("e.g. $40 / hour or Negotiable") },
      modifier = Modifier.fillMaxWidth().testTag("form_service_price"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      OutlinedTextField(
        value = country,
        onValueChange = { country = it },
        label = { Text(stringResource(R.string.country_label)) },
        modifier = Modifier.weight(1f).testTag("form_service_country"),
        shape = RoundedCornerShape(10.dp)
      )
      OutlinedTextField(
        value = city,
        onValueChange = { city = it },
        label = { Text(stringResource(R.string.city_label)) },
        modifier = Modifier.weight(1f).testTag("form_service_city"),
        shape = RoundedCornerShape(10.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.status_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      statuses.forEach { st ->
        FilterChip(
          selected = availabilityStatus == st,
          onClick = { availabilityStatus = st },
          label = { Text(st, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.weight(1f).height(48.dp),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(stringResource(R.string.action_cancel))
      }

      Button(
        onClick = {
          if (title.isBlank()) {
            titleError = "Title is required"
            return@Button
          }
          if (description.trim().length < 10) {
            descError = "Description must be at least 10 characters"
            return@Button
          }

          viewModel.saveService(
            id = initialService?.id ?: 0L,
            title = title,
            description = description,
            category = category,
            skills = skills,
            price = price,
            country = country,
            city = city,
            availabilityStatus = availabilityStatus
          )
        },
        modifier = Modifier.weight(1f).height(48.dp).testTag("save_service_submit_btn"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text(stringResource(R.string.save_record), color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }

  if (showDeleteDialog && initialService != null) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.confirm_delete_title),
      body = stringResource(R.string.confirm_delete_body),
      onConfirm = {
        viewModel.deleteService(initialService)
        showDeleteDialog = false
      },
      onDismiss = { showDeleteDialog = false }
    )
  }
}

// ----------------- OPPORTUNITY FORM -----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OpportunityForm(
  viewModel: XaldoonViewModel,
  initialOpportunity: OpportunityEntity?,
  onDismiss: () -> Unit
) {
  var title by remember { mutableStateOf(initialOpportunity?.title ?: "") }
  var type by remember { mutableStateOf(initialOpportunity?.type ?: "Job") }
  var category by remember { mutableStateOf(initialOpportunity?.category ?: "Technology & Software") }
  var organization by remember { mutableStateOf(initialOpportunity?.organization ?: "") }
  var description by remember { mutableStateOf(initialOpportunity?.description ?: "") }
  var requirements by remember { mutableStateOf(initialOpportunity?.requirements ?: "") }
  var country by remember { mutableStateOf(initialOpportunity?.country ?: "Somalia") }
  var city by remember { mutableStateOf(initialOpportunity?.city ?: "Mogadishu") }
  var deadline by remember { mutableStateOf(initialOpportunity?.deadline ?: "") }

  var titleError by remember { mutableStateOf<String?>(null) }
  var descError by remember { mutableStateOf<String?>(null) }
  var showDeleteDialog by remember { mutableStateOf(false) }

  val oppTypes = listOf(
    "Job", "Freelancing", "Business", "Partnership", "Education", "Internship", "Volunteering", "Investment", "Service"
  )
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (initialOpportunity == null) stringResource(R.string.form_title_opportunity) else stringResource(R.string.action_edit),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )

      if (initialOpportunity != null) {
        IconButton(
          onClick = { showDeleteDialog = true },
          modifier = Modifier.testTag("delete_opportunity_btn")
        ) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
      value = title,
      onValueChange = {
        title = it
        if (it.isNotBlank()) titleError = null
      },
      label = { Text(stringResource(R.string.title_field)) },
      isError = titleError != null,
      supportingText = { if (titleError != null) Text(titleError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_opp_title"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.opportunity_type_field),
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      oppTypes.forEach { t ->
        FilterChip(
          selected = type == t,
          onClick = { type = t },
          label = { Text(t, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
      value = organization,
      onValueChange = { organization = it },
      label = { Text(stringResource(R.string.organization_field)) },
      placeholder = { Text("e.g. Xaldoon Innovation Lab or Private Company") },
      modifier = Modifier.fillMaxWidth().testTag("form_opp_org"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = description,
      onValueChange = {
        description = it
        if (it.length >= 10) descError = null
      },
      label = { Text(stringResource(R.string.description_field)) },
      isError = descError != null,
      supportingText = { if (descError != null) Text(descError!!) },
      modifier = Modifier.fillMaxWidth().testTag("form_opp_desc"),
      minLines = 3,
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = requirements,
      onValueChange = { requirements = it },
      label = { Text(stringResource(R.string.requirements_field)) },
      placeholder = { Text("e.g. Skills, qualifications, timeline") },
      modifier = Modifier.fillMaxWidth().testTag("form_opp_requirements"),
      minLines = 2,
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      OutlinedTextField(
        value = country,
        onValueChange = { country = it },
        label = { Text(stringResource(R.string.country_label)) },
        modifier = Modifier.weight(1f).testTag("form_opp_country"),
        shape = RoundedCornerShape(10.dp)
      )
      OutlinedTextField(
        value = city,
        onValueChange = { city = it },
        label = { Text(stringResource(R.string.city_label)) },
        modifier = Modifier.weight(1f).testTag("form_opp_city"),
        shape = RoundedCornerShape(10.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = deadline,
      onValueChange = { deadline = it },
      label = { Text(stringResource(R.string.deadline_field)) },
      placeholder = { Text("e.g. Dec 31, 2026") },
      modifier = Modifier.fillMaxWidth().testTag("form_opp_deadline"),
      shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.weight(1f).height(48.dp),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(stringResource(R.string.action_cancel))
      }

      Button(
        onClick = {
          if (title.isBlank()) {
            titleError = "Title is required"
            return@Button
          }
          if (description.trim().length < 10) {
            descError = "Description must be at least 10 characters"
            return@Button
          }

          viewModel.saveOpportunity(
            id = initialOpportunity?.id ?: 0L,
            title = title,
            type = type,
            category = category,
            organization = organization,
            description = description,
            requirements = requirements,
            country = country,
            city = city,
            deadline = deadline
          )
        },
        modifier = Modifier.weight(1f).height(48.dp).testTag("save_opportunity_submit_btn"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text(stringResource(R.string.save_record), color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }

  if (showDeleteDialog && initialOpportunity != null) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.confirm_delete_title),
      body = stringResource(R.string.confirm_delete_body),
      onConfirm = {
        viewModel.deleteOpportunity(initialOpportunity)
        showDeleteDialog = false
      },
      onDismiss = { showDeleteDialog = false }
    )
  }
}

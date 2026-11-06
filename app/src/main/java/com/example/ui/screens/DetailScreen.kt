package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.CreateType
import com.example.ui.DetailItem
import com.example.ui.EditItem
import com.example.ui.XaldoonViewModel
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.DemoOrUserBadge
import com.example.ui.components.StatusPill
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
  detailItem: DetailItem,
  viewModel: XaldoonViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bookmarks by viewModel.bookmarks.collectAsState()
  val allSolutions by viewModel.allSolutions.collectAsState()
  var showDeleteDialog by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar for Detail Screen
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("detail_back_btn")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Text(
            text = when (detailItem) {
              is DetailItem.NeedDetail -> "Need Details"
              is DetailItem.SolutionDetail -> "Solution Details"
              is DetailItem.ServiceDetail -> "Service Details"
              is DetailItem.OpportunityDetail -> "Opportunity Details"
            },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          val (typeStr, targetId) = when (detailItem) {
            is DetailItem.NeedDetail -> Pair("NEED", detailItem.need.id)
            is DetailItem.SolutionDetail -> Pair("SOLUTION", detailItem.solution.id)
            is DetailItem.ServiceDetail -> Pair("SERVICE", detailItem.service.id)
            is DetailItem.OpportunityDetail -> Pair("OPPORTUNITY", detailItem.opportunity.id)
          }
          val isBookmarked = bookmarks.any { it.targetType == typeStr && it.targetId == targetId }

          IconButton(
            onClick = { viewModel.toggleBookmark(typeStr, targetId) },
            modifier = Modifier.testTag("detail_bookmark_btn")
          ) {
            Icon(
              imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Bookmark",
              tint = if (isBookmarked) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(
            onClick = {
              when (detailItem) {
                is DetailItem.NeedDetail -> viewModel.startEdit(EditItem.EditNeed(detailItem.need))
                is DetailItem.SolutionDetail -> viewModel.startEdit(EditItem.EditSolution(detailItem.solution))
                is DetailItem.ServiceDetail -> viewModel.startEdit(EditItem.EditService(detailItem.service))
                is DetailItem.OpportunityDetail -> viewModel.startEdit(EditItem.EditOpportunity(detailItem.opportunity))
              }
            },
            modifier = Modifier.testTag("detail_edit_btn")
          ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
          }

          IconButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.testTag("detail_delete_btn")
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
          }
        }
      }
    }

    // Scrollable Content
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(16.dp)
    ) {
      when (detailItem) {
        is DetailItem.NeedDetail -> {
          val need = detailItem.need
          val relatedSolutions = allSolutions.filter { it.relatedNeedId == need.id }

          // Badges Row
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primaryContainer
            ) {
              Text(
                text = need.category,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            DemoOrUserBadge(isDemo = need.isDemo)
            Spacer(modifier = Modifier.width(8.dp))
            StatusPill(status = need.status)
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = need.title,
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Posted by ${need.authorName}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
              )
              if (need.city.isNotBlank()) {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "${need.city}, ${need.country}",
                  style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Problem Statement & Description",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = need.description,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
            color = MaterialTheme.colorScheme.onSurface
          )

          if (need.contactDetails.isNotBlank()) {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Direct Contact Information",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = need.contactDetails,
                  style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(16.dp))

          // Connected Solutions
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Offered Solutions (${relatedSolutions.size})",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Button(
              onClick = { viewModel.startCreate(CreateType.SOLUTION) },
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Propose Solution", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          if (relatedSolutions.isEmpty()) {
            Text(
              text = "No solutions have been linked yet. Be the first innovator to propose an answer to this challenge!",
              style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          } else {
            relatedSolutions.forEach { sol ->
              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = sol.title,
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    StatusPill(status = sol.status)
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = sol.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                  )
                  if (sol.price != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "Cost: ${sol.price}",
                      style = MaterialTheme.typography.labelSmall.copy(color = AccentGold, fontWeight = FontWeight.Bold)
                    )
                  }
                }
              }
            }
          }
        }

        is DetailItem.SolutionDetail -> {
          val sol = detailItem.solution
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = AccentGold.copy(alpha = 0.15f)
            ) {
              Text(
                text = "SOLUTION",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = AccentGold
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            DemoOrUserBadge(isDemo = sol.isDemo)
            Spacer(modifier = Modifier.width(8.dp))
            StatusPill(status = sol.status)
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = sol.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("Offered by: ${sol.authorName}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
              if (sol.price != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Price / Terms: ${sol.price}", style = MaterialTheme.typography.labelMedium.copy(color = AccentGold, fontWeight = FontWeight.Bold))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text("Blueprint & Solution Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = sol.description, style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp))

          if (sol.skills.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Required Skills & Expertise", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              sol.skills.split(",").forEach { s ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = MaterialTheme.colorScheme.primaryContainer
                ) {
                  Text(
                    text = s.trim(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        is DetailItem.ServiceDetail -> {
          val srv = detailItem.service
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.secondaryContainer
            ) {
              Text(
                text = srv.category,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            DemoOrUserBadge(isDemo = srv.isDemo)
            Spacer(modifier = Modifier.width(8.dp))
            StatusPill(status = srv.availabilityStatus)
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = srv.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("Service Provider: ${srv.providerName}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
              if (srv.price != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rate: ${srv.price}", style = MaterialTheme.typography.labelMedium.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold))
              }
              if (srv.city.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Service Base: ${srv.city}, ${srv.country}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text("Service Scope & Capabilities", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = srv.description, style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp))

          if (srv.skills.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Verified Competencies", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              srv.skills.split(",").forEach { s ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = MaterialTheme.colorScheme.primaryContainer
                ) {
                  Text(
                    text = s.trim(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        is DetailItem.OpportunityDetail -> {
          val opp = detailItem.opportunity
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = EmeraldPrimary.copy(alpha = 0.15f)
            ) {
              Text(
                text = opp.type.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = EmeraldPrimary
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            DemoOrUserBadge(isDemo = opp.isDemo)
            if (opp.deadline.isNotBlank()) {
              Spacer(modifier = Modifier.width(8.dp))
              StatusPill(status = "Due: ${opp.deadline}")
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = opp.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("Organization: ${opp.organization}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
              if (opp.city.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Location: ${opp.city}, ${opp.country}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text("Opportunity Description", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = opp.description, style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp))

          if (opp.requirements.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Requirements & Eligibility", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = opp.requirements,
              style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }

  // Delete Confirmation Dialog
  if (showDeleteDialog) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.confirm_delete_title),
      body = stringResource(R.string.confirm_delete_body),
      onConfirm = {
        when (detailItem) {
          is DetailItem.NeedDetail -> viewModel.deleteNeed(detailItem.need)
          is DetailItem.SolutionDetail -> viewModel.deleteSolution(detailItem.solution)
          is DetailItem.ServiceDetail -> viewModel.deleteService(detailItem.service)
          is DetailItem.OpportunityDetail -> viewModel.deleteOpportunity(detailItem.opportunity)
        }
        showDeleteDialog = false
      },
      onDismiss = { showDeleteDialog = false }
    )
  }
}

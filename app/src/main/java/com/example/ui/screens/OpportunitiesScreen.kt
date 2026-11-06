package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.OpportunityEntity
import com.example.ui.DetailItem
import com.example.ui.XaldoonViewModel
import com.example.ui.components.EmptyStateView
import com.example.ui.components.UniversalItemCard
import com.example.ui.theme.EmeraldPrimary

@Composable
fun OpportunitiesScreen(
  viewModel: XaldoonViewModel,
  modifier: Modifier = Modifier
) {
  val opportunities by viewModel.allOpportunities.collectAsState()
  val bookmarks by viewModel.bookmarks.collectAsState()
  val typeFilter by viewModel.opportunityTypeFilter.collectAsState()
  val onlyBookmarked by viewModel.opportunitiesOnlyBookmarked.collectAsState()

  val oppTypes = listOf(
    "All", "Job", "Freelancing", "Business", "Partnership", "Education", "Internship", "Volunteering", "Investment", "Service"
  )

  val filteredOpportunities: List<OpportunityEntity> = remember(
    opportunities, typeFilter, onlyBookmarked, bookmarks
  ) {
    var list = opportunities

    if (!typeFilter.isNullOrBlank() && typeFilter != "All") {
      list = list.filter { it.type.equals(typeFilter, ignoreCase = true) }
    }

    if (onlyBookmarked) {
      val bookmarkedIds = bookmarks.filter { it.targetType == "OPPORTUNITY" }.map { it.targetId }
      list = list.filter { it.id in bookmarkedIds }
    }

    list.sortedByDescending { it.createdAt }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(R.string.all_opportunities),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )

          // Bookmarked filter button
          FilterChip(
            selected = onlyBookmarked,
            onClick = { viewModel.opportunitiesOnlyBookmarked.value = !onlyBookmarked },
            leadingIcon = {
              Icon(
                imageVector = if (onlyBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
              )
            },
            label = { Text("Saved", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldPrimary,
              selectedLabelColor = androidx.compose.ui.graphics.Color.White,
              selectedLeadingIconColor = androidx.compose.ui.graphics.Color.White
            ),
            modifier = Modifier.testTag("opps_saved_filter_chip")
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Type filter horizontal scroll
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          oppTypes.forEach { typeOption ->
            val isSelected = (typeOption == "All" && (typeFilter == null || typeFilter == "All")) ||
                (typeFilter == typeOption)
            FilterChip(
              selected = isSelected,
              onClick = {
                viewModel.opportunityTypeFilter.value = if (typeOption == "All") null else typeOption
              },
              label = {
                Text(
                  text = typeOption,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = EmeraldPrimary,
                selectedLabelColor = androidx.compose.ui.graphics.Color.White
              ),
              shape = RoundedCornerShape(18.dp)
            )
          }
        }
      }
    }

    // Results info row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${filteredOpportunities.size} opportunities open",
        style = MaterialTheme.typography.labelMedium.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold
        )
      )

      if (typeFilter != null || onlyBookmarked) {
        TextButton(
          onClick = {
            viewModel.opportunityTypeFilter.value = null
            viewModel.opportunitiesOnlyBookmarked.value = false
          }
        ) {
          Text(
            text = stringResource(R.string.reset_filters),
            style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
          )
        }
      }
    }

    if (filteredOpportunities.isEmpty()) {
      EmptyStateView(
        title = "No opportunities match",
        description = "Try resetting filters or post a new job, internship or partnership opportunity.",
        icon = Icons.Default.BusinessCenter,
        modifier = Modifier.padding(24.dp)
      )
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("opportunities_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredOpportunities) { opp ->
          val isBookmarked = bookmarks.any { it.targetType == "OPPORTUNITY" && it.targetId == opp.id }
          UniversalItemCard(
            title = opp.title,
            description = opp.description,
            categoryOrType = opp.type.uppercase(),
            authorOrProvider = opp.organization,
            status = opp.deadline.ifBlank { "Active" },
            location = if (opp.city.isNotBlank()) "${opp.city}, ${opp.country}" else null,
            isDemo = opp.isDemo,
            isBookmarked = isBookmarked,
            onBookmarkClick = { viewModel.toggleBookmark("OPPORTUNITY", opp.id) },
            onClick = { viewModel.openDetail(DetailItem.OpportunityDetail(opp)) }
          )
        }
      }
    }
  }
}

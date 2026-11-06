package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.ui.DetailItem
import com.example.ui.FilterContentType
import com.example.ui.SortDirection
import com.example.ui.XaldoonViewModel
import com.example.ui.components.EmptyStateView
import com.example.ui.components.UniversalItemCard
import com.example.ui.theme.EmeraldPrimary

sealed class ExploreRecord {
  data class Need(val item: NeedEntity) : ExploreRecord()
  data class Solution(val item: SolutionEntity) : ExploreRecord()
  data class Service(val item: ServiceEntity) : ExploreRecord()
  data class Opportunity(val item: OpportunityEntity) : ExploreRecord()
}

@Composable
fun ExploreScreen(
  viewModel: XaldoonViewModel,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQuery.collectAsState()
  val filterContentType by viewModel.filterContentType.collectAsState()
  val selectedCategory by viewModel.selectedCategoryFilter.collectAsState()
  val sortDirection by viewModel.sortDirection.collectAsState()

  val needs by viewModel.allNeeds.collectAsState()
  val solutions by viewModel.allSolutions.collectAsState()
  val services by viewModel.allServices.collectAsState()
  val opportunities by viewModel.allOpportunities.collectAsState()
  val categories by viewModel.categories.collectAsState()
  val bookmarks by viewModel.bookmarks.collectAsState()
  val currentLang by viewModel.appLanguage.collectAsState()

  var showFiltersDialog by remember { mutableStateOf(false) }

  // Filter & Search Logic
  val allRecords: List<ExploreRecord> = remember(
    needs, solutions, services, opportunities,
    filterContentType, searchQuery, selectedCategory, sortDirection
  ) {
    val list = mutableListOf<ExploreRecord>()

    if (filterContentType == FilterContentType.ALL || filterContentType == FilterContentType.NEEDS) {
      list.addAll(needs.map { ExploreRecord.Need(it) })
    }
    if (filterContentType == FilterContentType.ALL || filterContentType == FilterContentType.SOLUTIONS) {
      list.addAll(solutions.map { ExploreRecord.Solution(it) })
    }
    if (filterContentType == FilterContentType.ALL || filterContentType == FilterContentType.SERVICES) {
      list.addAll(services.map { ExploreRecord.Service(it) })
    }
    if (filterContentType == FilterContentType.ALL || filterContentType == FilterContentType.OPPORTUNITIES) {
      list.addAll(opportunities.map { ExploreRecord.Opportunity(it) })
    }

    // Filter by search query
    val query = searchQuery.trim().lowercase()
    val filteredByQuery = if (query.isEmpty()) {
      list
    } else {
      list.filter { rec ->
        when (rec) {
          is ExploreRecord.Need -> rec.item.title.lowercase().contains(query) || rec.item.description.lowercase().contains(query) || rec.item.category.lowercase().contains(query) || rec.item.city.lowercase().contains(query)
          is ExploreRecord.Solution -> rec.item.title.lowercase().contains(query) || rec.item.description.lowercase().contains(query) || rec.item.skills.lowercase().contains(query)
          is ExploreRecord.Service -> rec.item.title.lowercase().contains(query) || rec.item.description.lowercase().contains(query) || rec.item.skills.lowercase().contains(query) || rec.item.category.lowercase().contains(query)
          is ExploreRecord.Opportunity -> rec.item.title.lowercase().contains(query) || rec.item.description.lowercase().contains(query) || rec.item.organization.lowercase().contains(query) || rec.item.category.lowercase().contains(query)
        }
      }
    }

    // Filter by category
    val filteredByCategory = if (selectedCategory == null) {
      filteredByQuery
    } else {
      val catLower = selectedCategory!!.lowercase()
      filteredByQuery.filter { rec ->
        when (rec) {
          is ExploreRecord.Need -> rec.item.category.lowercase().contains(catLower)
          is ExploreRecord.Solution -> rec.item.skills.lowercase().contains(catLower) || rec.item.title.lowercase().contains(catLower)
          is ExploreRecord.Service -> rec.item.category.lowercase().contains(catLower)
          is ExploreRecord.Opportunity -> rec.item.category.lowercase().contains(catLower)
        }
      }
    }

    // Sort
    if (sortDirection == SortDirection.NEWEST) {
      filteredByCategory.sortedByDescending { rec ->
        when (rec) {
          is ExploreRecord.Need -> rec.item.createdAt
          is ExploreRecord.Solution -> rec.item.createdAt
          is ExploreRecord.Service -> rec.item.createdAt
          is ExploreRecord.Opportunity -> rec.item.createdAt
        }
      }
    } else {
      filteredByCategory.sortedBy { rec ->
        when (rec) {
          is ExploreRecord.Need -> rec.item.createdAt
          is ExploreRecord.Solution -> rec.item.createdAt
          is ExploreRecord.Service -> rec.item.createdAt
          is ExploreRecord.Opportunity -> rec.item.createdAt
        }
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Search Box & Sort Toggle
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.searchQuery.value = it },
        placeholder = { Text(stringResource(R.string.search_records)) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear search")
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("explore_search_input"),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = EmeraldPrimary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Content Type Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ContentTypeChip(
          label = stringResource(R.string.filter_all),
          isSelected = filterContentType == FilterContentType.ALL,
          onClick = { viewModel.filterContentType.value = FilterContentType.ALL }
        )
        ContentTypeChip(
          label = stringResource(R.string.filter_needs),
          isSelected = filterContentType == FilterContentType.NEEDS,
          onClick = { viewModel.filterContentType.value = FilterContentType.NEEDS }
        )
        ContentTypeChip(
          label = stringResource(R.string.filter_solutions),
          isSelected = filterContentType == FilterContentType.SOLUTIONS,
          onClick = { viewModel.filterContentType.value = FilterContentType.SOLUTIONS }
        )
        ContentTypeChip(
          label = stringResource(R.string.filter_services),
          isSelected = filterContentType == FilterContentType.SERVICES,
          onClick = { viewModel.filterContentType.value = FilterContentType.SERVICES }
        )
        ContentTypeChip(
          label = stringResource(R.string.filter_opportunities),
          isSelected = filterContentType == FilterContentType.OPPORTUNITIES,
          onClick = { viewModel.filterContentType.value = FilterContentType.OPPORTUNITIES }
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Category chips & Sort toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          if (selectedCategory != null) {
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier.testTag("active_category_filter")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = selectedCategory!!,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Remove category filter",
                  modifier = Modifier
                    .size(14.dp)
                    .clickable { viewModel.selectedCategoryFilter.value = null }
                )
              }
            }
          } else {
            categories.take(4).forEach { cat ->
              val catName = if (currentLang == "so") cat.nameSo else cat.nameEn
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.clickable {
                  viewModel.selectedCategoryFilter.value = catName
                }
              ) {
                Text(
                  text = catName,
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        // Sort Button
        TextButton(
          onClick = {
            viewModel.sortDirection.value = if (sortDirection == SortDirection.NEWEST) {
              SortDirection.OLDEST
            } else {
              SortDirection.NEWEST
            }
          },
          modifier = Modifier.testTag("sort_direction_btn")
        ) {
          Icon(Icons.Default.SwapVert, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = if (sortDirection == SortDirection.NEWEST) stringResource(R.string.sort_newest) else stringResource(R.string.sort_oldest),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // Results Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${allRecords.size} records found",
        style = MaterialTheme.typography.labelMedium.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold
        )
      )

      if (searchQuery.isNotEmpty() || selectedCategory != null) {
        TextButton(
          onClick = {
            viewModel.searchQuery.value = ""
            viewModel.selectedCategoryFilter.value = null
            viewModel.filterContentType.value = FilterContentType.ALL
          }
        ) {
          Text(
            text = stringResource(R.string.reset_filters),
            style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
          )
        }
      }
    }

    // List of Results
    if (allRecords.isEmpty()) {
      EmptyStateView(
        title = stringResource(R.string.no_search_results),
        description = stringResource(R.string.try_adjusting_filters),
        modifier = Modifier.padding(24.dp)
      )
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("explore_results_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(allRecords) { record ->
          when (record) {
            is ExploreRecord.Need -> {
              val isBookmarked = bookmarks.any { it.targetType == "NEED" && it.targetId == record.item.id }
              UniversalItemCard(
                title = record.item.title,
                description = record.item.description,
                categoryOrType = "NEED • ${record.item.category}",
                authorOrProvider = record.item.authorName,
                status = record.item.status,
                location = if (record.item.city.isNotBlank()) "${record.item.city}, ${record.item.country}" else null,
                isDemo = record.item.isDemo,
                isBookmarked = isBookmarked,
                onBookmarkClick = { viewModel.toggleBookmark("NEED", record.item.id) },
                onClick = { viewModel.openDetail(DetailItem.NeedDetail(record.item)) }
              )
            }
            is ExploreRecord.Solution -> {
              val isBookmarked = bookmarks.any { it.targetType == "SOLUTION" && it.targetId == record.item.id }
              UniversalItemCard(
                title = record.item.title,
                description = record.item.description,
                categoryOrType = "SOLUTION",
                authorOrProvider = record.item.authorName,
                status = record.item.status,
                location = record.item.price ?: record.item.skills,
                isDemo = record.item.isDemo,
                isBookmarked = isBookmarked,
                onBookmarkClick = { viewModel.toggleBookmark("SOLUTION", record.item.id) },
                onClick = { viewModel.openDetail(DetailItem.SolutionDetail(record.item)) }
              )
            }
            is ExploreRecord.Service -> {
              val isBookmarked = bookmarks.any { it.targetType == "SERVICE" && it.targetId == record.item.id }
              UniversalItemCard(
                title = record.item.title,
                description = record.item.description,
                categoryOrType = "SERVICE • ${record.item.category}",
                authorOrProvider = record.item.providerName,
                status = record.item.availabilityStatus,
                location = if (record.item.city.isNotBlank()) "${record.item.city}, ${record.item.country}" else record.item.price,
                isDemo = record.item.isDemo,
                isBookmarked = isBookmarked,
                onBookmarkClick = { viewModel.toggleBookmark("SERVICE", record.item.id) },
                onClick = { viewModel.openDetail(DetailItem.ServiceDetail(record.item)) }
              )
            }
            is ExploreRecord.Opportunity -> {
              val isBookmarked = bookmarks.any { it.targetType == "OPPORTUNITY" && it.targetId == record.item.id }
              UniversalItemCard(
                title = record.item.title,
                description = record.item.description,
                categoryOrType = "OPPORTUNITY • ${record.item.type}",
                authorOrProvider = record.item.organization,
                status = record.item.deadline.ifBlank { null },
                location = if (record.item.city.isNotBlank()) "${record.item.city}, ${record.item.country}" else null,
                isDemo = record.item.isDemo,
                isBookmarked = isBookmarked,
                onBookmarkClick = { viewModel.toggleBookmark("OPPORTUNITY", record.item.id) },
                onClick = { viewModel.openDetail(DetailItem.OpportunityDetail(record.item)) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ContentTypeChip(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  FilterChip(
    selected = isSelected,
    onClick = onClick,
    label = {
      Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
      )
    },
    colors = FilterChipDefaults.filterChipColors(
      selectedContainerColor = EmeraldPrimary,
      selectedLabelColor = androidx.compose.ui.graphics.Color.White
    ),
    shape = RoundedCornerShape(20.dp)
  )
}

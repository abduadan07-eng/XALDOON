package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.CategoryEntity
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.SolutionEntity
import com.example.ui.CreateType
import com.example.ui.DetailItem
import com.example.ui.FilterContentType
import com.example.ui.MainTab
import com.example.ui.XaldoonViewModel
import com.example.ui.components.DemoOrUserBadge
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StatusPill
import com.example.ui.components.UniversalItemCard
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimary

@Composable
fun HomeScreen(
  viewModel: XaldoonViewModel,
  modifier: Modifier = Modifier
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val needs by viewModel.allNeeds.collectAsState()
  val solutions by viewModel.allSolutions.collectAsState()
  val opportunities by viewModel.allOpportunities.collectAsState()
  val categories by viewModel.categories.collectAsState()
  val bookmarks by viewModel.bookmarks.collectAsState()
  val currentLang by viewModel.appLanguage.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("home_screen_lazy_column"),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // 1. Welcome Header Banner
    item {
      HeaderBanner(
        userName = userProfile?.fullName ?: "Problem Solver",
        userCity = userProfile?.city ?: "Global",
        onPostNeedClick = { viewModel.startCreate(CreateType.NEED) },
        onSearchClick = {
          viewModel.filterContentType.value = FilterContentType.ALL
          viewModel.setTab(MainTab.EXPLORE)
        }
      )
    }

    // 2. Categories Rail
    item {
      Spacer(modifier = Modifier.height(18.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = stringResource(R.string.categories_header),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
      Spacer(modifier = Modifier.height(10.dp))

      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(categories) { cat ->
          CategoryCard(
            category = cat,
            language = currentLang,
            onClick = {
              viewModel.selectedCategoryFilter.value = if (currentLang == "so") cat.nameSo else cat.nameEn
              viewModel.filterContentType.value = FilterContentType.ALL
              viewModel.setTab(MainTab.EXPLORE)
            }
          )
        }
      }
    }

    // 3. Recent Needs Section
    item {
      Spacer(modifier = Modifier.height(24.dp))
      SectionHeader(
        title = stringResource(R.string.recent_needs),
        onViewAllClick = {
          viewModel.filterContentType.value = FilterContentType.NEEDS
          viewModel.setTab(MainTab.EXPLORE)
        }
      )
      Spacer(modifier = Modifier.height(10.dp))
    }

    if (needs.isEmpty()) {
      item {
        EmptyStateView(
          title = stringResource(R.string.empty_home_title),
          description = stringResource(R.string.empty_home_desc),
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }
    } else {
      items(needs.take(3)) { need ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
          val isBookmarked = bookmarks.any { it.targetType == "NEED" && it.targetId == need.id }
          UniversalItemCard(
            title = need.title,
            description = need.description,
            categoryOrType = need.category,
            authorOrProvider = need.authorName,
            status = need.status,
            location = if (need.city.isNotBlank()) "${need.city}, ${need.country}" else null,
            isDemo = need.isDemo,
            isBookmarked = isBookmarked,
            onBookmarkClick = { viewModel.toggleBookmark("NEED", need.id) },
            onClick = { viewModel.openDetail(DetailItem.NeedDetail(need)) }
          )
        }
      }
    }

    // 4. Recent Solutions Section
    item {
      Spacer(modifier = Modifier.height(24.dp))
      SectionHeader(
        title = stringResource(R.string.recent_solutions),
        onViewAllClick = {
          viewModel.filterContentType.value = FilterContentType.SOLUTIONS
          viewModel.setTab(MainTab.EXPLORE)
        }
      )
      Spacer(modifier = Modifier.height(10.dp))
    }

    if (solutions.isEmpty()) {
      item {
        EmptyStateView(
          title = "No solutions yet",
          description = "Share your blueprint, methodology, or prototype with the community.",
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }
    } else {
      items(solutions.take(3)) { sol ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
          val isBookmarked = bookmarks.any { it.targetType == "SOLUTION" && it.targetId == sol.id }
          UniversalItemCard(
            title = sol.title,
            description = sol.description,
            categoryOrType = sol.skills.ifBlank { "Solution" },
            authorOrProvider = sol.authorName,
            status = sol.status,
            location = sol.price,
            isDemo = sol.isDemo,
            isBookmarked = isBookmarked,
            onBookmarkClick = { viewModel.toggleBookmark("SOLUTION", sol.id) },
            onClick = { viewModel.openDetail(DetailItem.SolutionDetail(sol)) }
          )
        }
      }
    }

    // 5. Featured Opportunities Section
    item {
      Spacer(modifier = Modifier.height(24.dp))
      SectionHeader(
        title = stringResource(R.string.featured_opportunities),
        onViewAllClick = {
          viewModel.setTab(MainTab.OPPORTUNITIES)
        }
      )
      Spacer(modifier = Modifier.height(10.dp))
    }

    if (opportunities.isNotEmpty()) {
      items(opportunities.take(2)) { opp ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
          val isBookmarked = bookmarks.any { it.targetType == "OPPORTUNITY" && it.targetId == opp.id }
          UniversalItemCard(
            title = opp.title,
            description = opp.description,
            categoryOrType = opp.type,
            authorOrProvider = opp.organization,
            status = opp.deadline.ifBlank { null },
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

@Composable
private fun HeaderBanner(
  userName: String,
  userCity: String,
  onPostNeedClick: () -> Unit,
  onSearchClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldPrimary),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "${stringResource(R.string.welcome_back)} $userName",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
          Text(
            text = "📍 $userCity • 100% Offline Database",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 12.sp
            ),
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Fake search bar that opens Explore
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .clickable { onSearchClick() }
          .testTag("home_quick_search_bar")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = stringResource(R.string.search_hint_home),
            color = Color.Gray,
            fontSize = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onPostNeedClick,
          modifier = Modifier
            .weight(1f)
            .testTag("home_post_need_btn"),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = EmeraldPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = stringResource(R.string.post_a_need),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

@Composable
private fun CategoryCard(
  category: CategoryEntity,
  language: String,
  onClick: () -> Unit
) {
  val name = if (language == "so") category.nameSo else category.nameEn
  Card(
    modifier = Modifier
      .width(140.dp)
      .clickable { onClick() }
      .testTag("category_card_${category.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = getCategoryIcon(category.iconName),
          contentDescription = null,
          tint = EmeraldPrimary,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = name,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          fontSize = 12.sp
        ),
        maxLines = 2,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
private fun SectionHeader(
  title: String,
  onViewAllClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    TextButton(
      onClick = onViewAllClick,
      modifier = Modifier.testTag("view_all_${title.take(5).lowercase()}")
    ) {
      Text(
        text = stringResource(R.string.view_all),
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = EmeraldPrimary
        )
      )
      Spacer(modifier = Modifier.width(2.dp))
      Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = null,
        tint = EmeraldPrimary,
        modifier = Modifier.size(14.dp)
      )
    }
  }
}

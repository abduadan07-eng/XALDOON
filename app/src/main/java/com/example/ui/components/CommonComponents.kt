package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MainTab
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusYellow

@Composable
fun XaldoonTopBar(
  modifier: Modifier = Modifier,
  currentLanguage: String,
  isDarkTheme: Boolean,
  onLanguageToggle: () -> Unit,
  onThemeToggle: () -> Unit
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(EmeraldPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "X",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp,
              color = MaterialTheme.colorScheme.primary
            )
          )
          Text(
            text = stringResource(R.string.tagline),
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Language Switcher Chip
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
          modifier = Modifier
            .testTag("language_toggle_btn")
            .clip(RoundedCornerShape(16.dp))
            .clickable { onLanguageToggle() }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (currentLanguage == "so") "SO" else "EN",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Dark Mode Toggle
        IconButton(
          onClick = onThemeToggle,
          modifier = Modifier.testTag("theme_toggle_btn")
        ) {
          Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.Nightlight,
            contentDescription = "Toggle Theme",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}

@Composable
fun XaldoonBottomNavigation(
  currentTab: MainTab,
  onTabSelected: (MainTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.testTag("bottom_navigation_bar"),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    NavigationBarItem(
      selected = currentTab == MainTab.HOME,
      onClick = { onTabSelected(MainTab.HOME) },
      icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.nav_home)) },
      label = { Text(stringResource(R.string.nav_home), fontSize = 11.sp, fontWeight = if (currentTab == MainTab.HOME) FontWeight.Bold else FontWeight.Normal) },
      modifier = Modifier.testTag("nav_tab_home"),
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.primary
      )
    )

    NavigationBarItem(
      selected = currentTab == MainTab.EXPLORE,
      onClick = { onTabSelected(MainTab.EXPLORE) },
      icon = { Icon(Icons.Default.Explore, contentDescription = stringResource(R.string.nav_explore)) },
      label = { Text(stringResource(R.string.nav_explore), fontSize = 11.sp, fontWeight = if (currentTab == MainTab.EXPLORE) FontWeight.Bold else FontWeight.Normal) },
      modifier = Modifier.testTag("nav_tab_explore"),
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.primary
      )
    )

    NavigationBarItem(
      selected = currentTab == MainTab.CREATE,
      onClick = { onTabSelected(MainTab.CREATE) },
      icon = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.nav_create)) },
      label = { Text(stringResource(R.string.nav_create), fontSize = 11.sp, fontWeight = if (currentTab == MainTab.CREATE) FontWeight.Bold else FontWeight.Normal) },
      modifier = Modifier.testTag("nav_tab_create"),
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.primary
      )
    )

    NavigationBarItem(
      selected = currentTab == MainTab.OPPORTUNITIES,
      onClick = { onTabSelected(MainTab.OPPORTUNITIES) },
      icon = { Icon(Icons.Default.BusinessCenter, contentDescription = stringResource(R.string.nav_opportunities)) },
      label = { Text(stringResource(R.string.nav_opportunities), fontSize = 11.sp, fontWeight = if (currentTab == MainTab.OPPORTUNITIES) FontWeight.Bold else FontWeight.Normal) },
      modifier = Modifier.testTag("nav_tab_opportunities"),
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.primary
      )
    )

    NavigationBarItem(
      selected = currentTab == MainTab.PROFILE,
      onClick = { onTabSelected(MainTab.PROFILE) },
      icon = { Icon(Icons.Default.Person, contentDescription = stringResource(R.string.nav_profile)) },
      label = { Text(stringResource(R.string.nav_profile), fontSize = 11.sp, fontWeight = if (currentTab == MainTab.PROFILE) FontWeight.Bold else FontWeight.Normal) },
      modifier = Modifier.testTag("nav_tab_profile"),
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.primary
      )
    )
  }
}

@Composable
fun DemoOrUserBadge(
  isDemo: Boolean,
  modifier: Modifier = Modifier
) {
  if (isDemo) {
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = AccentGold.copy(alpha = 0.12f),
      border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f)),
      modifier = modifier
    ) {
      Text(
        text = stringResource(R.string.badge_demo),
        color = AccentGold,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
      )
    }
  } else {
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = EmeraldPrimary.copy(alpha = 0.12f),
      border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)),
      modifier = modifier
    ) {
      Text(
        text = stringResource(R.string.badge_user),
        color = EmeraldPrimary,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
      )
    }
  }
}

@Composable
fun StatusPill(
  status: String,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor) = when (status.lowercase()) {
    "open", "available", "offered" -> Pair(StatusGreen.copy(alpha = 0.14f), StatusGreen)
    "in progress", "accepted", "busy" -> Pair(StatusYellow.copy(alpha = 0.16f), StatusYellow)
    "solved", "completed" -> Pair(Color(0xFF2563EB).copy(alpha = 0.14f), Color(0xFF2563EB))
    else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Text(
      text = status,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UniversalItemCard(
  title: String,
  description: String,
  categoryOrType: String,
  authorOrProvider: String,
  status: String?,
  location: String?,
  isDemo: Boolean,
  isBookmarked: Boolean,
  onBookmarkClick: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("item_card_${title.take(10).replace(" ", "_")}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Text(
              text = categoryOrType,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 10.sp
              ),
              modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))
          DemoOrUserBadge(isDemo = isDemo)
        }

        IconButton(
          onClick = onBookmarkClick,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "Bookmark",
            tint = if (isBookmarked) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 13.sp,
          lineHeight = 18.sp
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (!location.isNullOrBlank()) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = location,
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
          }

          Text(
            text = "• $authorOrProvider",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        if (!status.isNullOrBlank()) {
          StatusPill(status = status)
        }
      }
    }
  }
}

@Composable
fun EmptyStateView(
  title: String,
  description: String,
  icon: ImageVector = Icons.Default.Explore,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp)
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = description,
      style = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 13.sp
      ),
      lineHeight = 18.sp,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

@Composable
fun ConfirmDeleteDialog(
  title: String,
  body: String,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = title, fontWeight = FontWeight.Bold) },
    text = { Text(text = body) },
    confirmButton = {
      TextButton(
        onClick = onConfirm,
        modifier = Modifier.testTag("confirm_delete_btn")
      ) {
        Text(text = stringResource(R.string.action_delete), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(text = stringResource(R.string.action_cancel))
      }
    }
  )
}

fun getCategoryIcon(iconName: String): ImageVector {
  return when (iconName) {
    "terminal" -> Icons.Default.Terminal
    "agriculture" -> Icons.Default.Agriculture
    "bolt" -> Icons.Default.Bolt
    "medical_services" -> Icons.Default.MedicalServices
    "storefront" -> Icons.Default.Storefront
    "school" -> Icons.Default.School
    "local_shipping" -> Icons.Default.LocalShipping
    "groups" -> Icons.Default.Groups
    else -> Icons.Default.Work
  }
}

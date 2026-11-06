package com.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.DetailItem
import com.example.ui.MainTab
import com.example.ui.XaldoonViewModel
import com.example.ui.components.XaldoonBottomNavigation
import com.example.ui.components.XaldoonTopBar
import com.example.ui.screens.CreateHubScreen
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OpportunitiesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.XaldoonTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

  private val viewModel: XaldoonViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val userProfile by viewModel.userProfile.collectAsState()
      val appLanguage by viewModel.appLanguage.collectAsState()
      val darkModeOverride by viewModel.darkModeOverride.collectAsState()

      // Sync language preference from stored profile if present
      LaunchedEffect(userProfile) {
        userProfile?.preferredLanguage?.let { lang ->
          if (lang != appLanguage) {
            viewModel.setLanguage(lang)
          }
        }
        userProfile?.isDarkMode?.let { dark ->
          if (dark != darkModeOverride) {
            viewModel.setDarkMode(dark)
          }
        }
      }

      val isSystemDark = isSystemInDarkTheme()
      val isDarkTheme = darkModeOverride ?: isSystemDark

      // Runtime dynamic locale support for English and Somali without activity restart
      val currentContext = LocalContext.current
      val locale = Locale(appLanguage)
      Locale.setDefault(locale)
      val configuration = Configuration(currentContext.resources.configuration)
      configuration.setLocale(locale)
      val localizedContext = remember(appLanguage) {
        currentContext.createConfigurationContext(configuration)
      }

      CompositionLocalProvider(LocalContext provides localizedContext) {
        XaldoonTheme(darkTheme = isDarkTheme) {
          val isOnboarded = userProfile?.isOnboarded == true

          if (!isOnboarded) {
            OnboardingScreen(
              viewModel = viewModel,
              modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
            )
          } else {
            XaldoonMainApp(
              viewModel = viewModel,
              isDarkTheme = isDarkTheme,
              onThemeToggle = {
                val nextTheme = if (isDarkTheme) false else true
                viewModel.setDarkMode(nextTheme)
              }
            )
          }
        }
      }
    }
  }
}

@Composable
fun XaldoonMainApp(
  viewModel: XaldoonViewModel,
  isDarkTheme: Boolean,
  onThemeToggle: () -> Unit
) {
  val currentTab by viewModel.currentTab.collectAsState()
  val activeDetail by viewModel.activeDetail.collectAsState()
  val editingItem by viewModel.editingItem.collectAsState()
  val activeCreateType by viewModel.activeCreateType.collectAsState()
  val isEditingProfile by viewModel.isEditingProfile.collectAsState()
  val appLanguage by viewModel.appLanguage.collectAsState()
  val snackbarMessage by viewModel.snackbarMessage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(snackbarMessage) {
    snackbarMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearSnackbarMessage()
    }
  }

  // Handle device back press faithfully with backstack popping
  BackHandler(
    enabled = activeDetail != null || editingItem != null || activeCreateType != null || isEditingProfile || currentTab != MainTab.HOME
  ) {
    when {
      activeDetail != null -> viewModel.closeDetail()
      editingItem != null -> viewModel.cancelEdit()
      activeCreateType != null -> viewModel.cancelCreate()
      isEditingProfile -> viewModel.setEditingProfile(false)
      currentTab != MainTab.HOME -> viewModel.setTab(MainTab.HOME)
    }
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
    topBar = {
      if (activeDetail == null && editingItem == null) {
        XaldoonTopBar(
          currentLanguage = appLanguage,
          isDarkTheme = isDarkTheme,
          onLanguageToggle = {
            val nextLang = if (appLanguage == "en") "so" else "en"
            viewModel.setLanguage(nextLang)
          },
          onThemeToggle = onThemeToggle
        )
      }
    },
    bottomBar = {
      if (activeDetail == null && editingItem == null) {
        XaldoonBottomNavigation(
          currentTab = currentTab,
          onTabSelected = { viewModel.setTab(it) },
          modifier = Modifier.navigationBarsPadding()
        )
      }
    },
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState)
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      if (activeDetail != null) {
        DetailScreen(
          detailItem = activeDetail!!,
          viewModel = viewModel,
          onBack = { viewModel.closeDetail() }
        )
      } else {
        when (currentTab) {
          MainTab.HOME -> HomeScreen(viewModel = viewModel)
          MainTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
          MainTab.CREATE -> CreateHubScreen(viewModel = viewModel)
          MainTab.OPPORTUNITIES -> OpportunitiesScreen(viewModel = viewModel)
          MainTab.PROFILE -> ProfileScreen(viewModel = viewModel)
        }
      }
    }
  }
}

package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.XaldoonViewModel
import com.example.ui.theme.EmeraldPrimary

@Composable
fun OnboardingScreen(
  viewModel: XaldoonViewModel,
  modifier: Modifier = Modifier
) {
  var currentStep by remember { mutableStateOf(0) } // 0: Welcome & Language & Storage Info, 1: Profile setup
  var selectedLang by remember { mutableStateOf(viewModel.appLanguage.value) }

  var fullName by remember { mutableStateOf("") }
  var bio by remember { mutableStateOf("") }
  var country by remember { mutableStateOf("Somalia") }
  var city by remember { mutableStateOf("Mogadishu") }
  var skills by remember { mutableStateOf("") }
  var interests by remember { mutableStateOf("") }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(scrollState)
      .padding(horizontal = 24.dp, vertical = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // App Emblem / Logo
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(EmeraldPrimary),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "X",
        color = Color.White,
        fontWeight = FontWeight.Black,
        fontSize = 42.sp
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = stringResource(R.string.welcome_to_xaldoon),
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
      ),
      textAlign = TextAlign.Center
    )

    Text(
      text = stringResource(R.string.tagline),
      style = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
      ),
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(top = 4.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = stringResource(R.string.onboarding_subtitle),
      style = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 14.sp,
        lineHeight = 20.sp
      ),
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    if (currentStep == 0) {
      // Language Selection Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = stringResource(R.string.select_preferred_language),
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            LanguageChoiceButton(
              title = "English",
              subtitle = "International",
              isSelected = selectedLang == "en",
              onClick = {
                selectedLang = "en"
                viewModel.setLanguage("en")
              },
              modifier = Modifier
                .weight(1f)
                .testTag("lang_select_en")
            )

            LanguageChoiceButton(
              title = "Af-Soomaali",
              subtitle = "Hooyo",
              isSelected = selectedLang == "so",
              onClick = {
                selectedLang = "so"
                viewModel.setLanguage("so")
              },
              modifier = Modifier
                .weight(1f)
                .testTag("lang_select_so")
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Offline Storage Guarantee Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.Top
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Storage,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = stringResource(R.string.offline_notice_title),
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = stringResource(R.string.offline_notice_body),
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                lineHeight = 18.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      Button(
        onClick = { currentStep = 1 },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("onboarding_continue_btn"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text(
          text = stringResource(R.string.get_started),
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
      }
    } else {
      // Step 1: Local Profile Creation
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = stringResource(R.string.create_local_profile),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(R.string.full_name)) },
            placeholder = { Text(stringResource(R.string.enter_name_hint)) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_full_name"),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text(stringResource(R.string.short_bio)) },
            placeholder = { Text(stringResource(R.string.bio_hint)) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_bio"),
            maxLines = 2,
            shape = RoundedCornerShape(10.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = country,
              onValueChange = { country = it },
              label = { Text(stringResource(R.string.country_label)) },
              modifier = Modifier
                .weight(1f)
                .testTag("input_country"),
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
              value = city,
              onValueChange = { city = it },
              label = { Text(stringResource(R.string.city_label)) },
              modifier = Modifier
                .weight(1f)
                .testTag("input_city"),
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = skills,
            onValueChange = { skills = it },
            label = { Text(stringResource(R.string.skills_label)) },
            placeholder = { Text("e.g. Agriculture, Mobile Dev, Solar Energy") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_skills"),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = interests,
            onValueChange = { interests = it },
            label = { Text(stringResource(R.string.interests_label)) },
            placeholder = { Text("e.g. Clean Water, Youth Employment, Trade") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_interests"),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          viewModel.completeOnboarding(
            fullName = fullName,
            bio = bio,
            country = country,
            city = city,
            skills = skills,
            interests = interests
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("complete_setup_btn"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
      ) {
        Text(
          text = stringResource(R.string.complete_setup),
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = { currentStep = 0 },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text(stringResource(R.string.action_back))
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun LanguageChoiceButton(
  title: String,
  subtitle: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
    border = BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    ),
    modifier = modifier.clip(RoundedCornerShape(12.dp)),
    onClick = onClick
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = EmeraldPrimary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

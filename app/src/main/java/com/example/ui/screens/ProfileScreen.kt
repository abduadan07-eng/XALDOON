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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.UserStats
import com.example.ui.XaldoonViewModel
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
  viewModel: XaldoonViewModel,
  modifier: Modifier = Modifier
) {
  val profile by viewModel.userProfile.collectAsState()
  val stats by viewModel.userStats.collectAsState()
  val appLanguage by viewModel.appLanguage.collectAsState()
  val isEditingProfile by viewModel.isEditingProfile.collectAsState()

  var showClearDataDialog by remember { mutableStateOf(false) }
  var showResetProfileDialog by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(scrollState)
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (isEditingProfile) {
      EditProfileSection(
        viewModel = viewModel,
        onDismiss = { viewModel.setEditingProfile(false) }
      )
    } else {
      // 1. Profile Header Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("profile_header_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            IconButton(
              onClick = { viewModel.setEditingProfile(true) },
              modifier = Modifier.testTag("edit_profile_btn")
            ) {
              Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = EmeraldPrimary
              )
            }
          }

          // Avatar Box
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = profile?.fullName?.take(1)?.uppercase() ?: "X",
              color = Color.White,
              fontSize = 32.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = profile?.fullName ?: "Anonymous User",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          if (!profile?.bio.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = profile!!.bio,
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
              ),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${profile?.city ?: "Mogadishu"}, ${profile?.country ?: "Somalia"}",
              style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
          }

          // Skills Chips
          if (!profile?.skills.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier.fillMaxWidth()
            ) {
              profile!!.skills.split(",").forEach { skill ->
                val clean = skill.trim()
                if (clean.isNotBlank()) {
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = clean,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 10.sp
                      ),
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 2. Statistics Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = stringResource(R.string.my_contributions),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            StatMetric(
              label = stringResource(R.string.needs_count),
              count = stats.needsCount,
              icon = Icons.Default.Work,
              color = EmeraldPrimary
            )
            StatMetric(
              label = stringResource(R.string.solutions_count),
              count = stats.solutionsCount,
              icon = Icons.Default.Lightbulb,
              color = AccentGold
            )
            StatMetric(
              label = stringResource(R.string.services_count),
              count = stats.servicesCount,
              icon = Icons.Default.Work,
              color = Color(0xFF1D4ED8)
            )
            StatMetric(
              label = stringResource(R.string.saved_count),
              count = stats.bookmarksCount,
              icon = Icons.Default.Bookmark,
              color = Color(0xFF7C3AED)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3. Language & Settings
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = stringResource(R.string.language_settings),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { viewModel.setLanguage("en") },
              modifier = Modifier.weight(1f).testTag("profile_lang_en"),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (appLanguage == "en") MaterialTheme.colorScheme.primaryContainer else Color.Transparent
              ),
              border = BorderStroke(if (appLanguage == "en") 2.dp else 1.dp, EmeraldPrimary)
            ) {
              Text(
                text = "English",
                fontWeight = if (appLanguage == "en") FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            OutlinedButton(
              onClick = { viewModel.setLanguage("so") },
              modifier = Modifier.weight(1f).testTag("profile_lang_so"),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (appLanguage == "so") MaterialTheme.colorScheme.primaryContainer else Color.Transparent
              ),
              border = BorderStroke(if (appLanguage == "so") 2.dp else 1.dp, EmeraldPrimary)
            ) {
              Text(
                text = "Af-Soomaali",
                fontWeight = if (appLanguage == "so") FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 4. Data Transparency & Architecture Notice
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        ),
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
      ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.Default.Storage,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = stringResource(R.string.storage_info_title),
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = stringResource(R.string.storage_info_desc),
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 5. Future Company Roadmap Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
      ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.Default.CloudSync,
            contentDescription = null,
            tint = Color(0xFF2563EB),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = stringResource(R.string.future_roadmap_title),
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = stringResource(R.string.future_roadmap_desc),
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 6. Danger Zone
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = stringResource(R.string.danger_zone),
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.error
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = { showClearDataDialog = true },
            modifier = Modifier.fillMaxWidth().testTag("clear_user_records_btn"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = stringResource(R.string.clear_my_data),
              color = MaterialTheme.colorScheme.error,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = { showResetProfileDialog = true },
            modifier = Modifier.fillMaxWidth().testTag("reset_profile_btn"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(stringResource(R.string.reset_profile), color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Confirm Clear Data Dialog
  if (showClearDataDialog) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.confirm_delete_title),
      body = stringResource(R.string.confirm_clear_data),
      onConfirm = {
        viewModel.clearUserRecords()
        showClearDataDialog = false
      },
      onDismiss = { showClearDataDialog = false }
    )
  }

  // Confirm Reset Profile Dialog
  if (showResetProfileDialog) {
    ConfirmDeleteDialog(
      title = stringResource(R.string.reset_profile),
      body = "This will clear your local user profile. Continue?",
      onConfirm = {
        viewModel.resetAllProfileData()
        showResetProfileDialog = false
      },
      onDismiss = { showResetProfileDialog = false }
    )
  }
}

@Composable
private fun StatMetric(
  label: String,
  count: Int,
  icon: ImageVector,
  color: Color
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = count.toString(),
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Black,
        color = color
      )
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp
      )
    )
  }
}

@Composable
private fun EditProfileSection(
  viewModel: XaldoonViewModel,
  onDismiss: () -> Unit
) {
  val profile = viewModel.userProfile.value
  var fullName by remember { mutableStateOf(profile?.fullName ?: "") }
  var bio by remember { mutableStateOf(profile?.bio ?: "") }
  var country by remember { mutableStateOf(profile?.country ?: "Somalia") }
  var city by remember { mutableStateOf(profile?.city ?: "Mogadishu") }
  var skills by remember { mutableStateOf(profile?.skills ?: "") }
  var interests by remember { mutableStateOf(profile?.interests ?: "") }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text(
        text = stringResource(R.string.edit_profile),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(14.dp))

      OutlinedTextField(
        value = fullName,
        onValueChange = { fullName = it },
        label = { Text(stringResource(R.string.full_name)) },
        modifier = Modifier.fillMaxWidth().testTag("edit_full_name"),
        singleLine = true,
        shape = RoundedCornerShape(10.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = bio,
        onValueChange = { bio = it },
        label = { Text(stringResource(R.string.short_bio)) },
        modifier = Modifier.fillMaxWidth().testTag("edit_bio"),
        maxLines = 2,
        shape = RoundedCornerShape(10.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = country,
          onValueChange = { country = it },
          label = { Text(stringResource(R.string.country_label)) },
          modifier = Modifier.weight(1f).testTag("edit_country"),
          shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
          value = city,
          onValueChange = { city = it },
          label = { Text(stringResource(R.string.city_label)) },
          modifier = Modifier.weight(1f).testTag("edit_city"),
          shape = RoundedCornerShape(10.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = skills,
        onValueChange = { skills = it },
        label = { Text(stringResource(R.string.skills_label)) },
        modifier = Modifier.fillMaxWidth().testTag("edit_skills"),
        shape = RoundedCornerShape(10.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = interests,
        onValueChange = { interests = it },
        label = { Text(stringResource(R.string.interests_label)) },
        modifier = Modifier.fillMaxWidth().testTag("edit_interests"),
        shape = RoundedCornerShape(10.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

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
            viewModel.updateProfile(
              fullName = fullName,
              bio = bio,
              country = country,
              city = city,
              skills = skills,
              interests = interests
            )
          },
          modifier = Modifier.weight(1f).height(48.dp).testTag("save_profile_btn"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
        ) {
          Text(stringResource(R.string.save_record), color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

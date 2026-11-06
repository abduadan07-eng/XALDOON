package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.SolutionEntity
import com.example.data.model.UserProfile
import com.example.data.repository.XaldoonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
  HOME,
  EXPLORE,
  CREATE,
  OPPORTUNITIES,
  PROFILE
}

enum class FilterContentType {
  ALL,
  NEEDS,
  SOLUTIONS,
  SERVICES,
  OPPORTUNITIES
}

enum class SortDirection {
  NEWEST,
  OLDEST
}

sealed class DetailItem {
  data class NeedDetail(val need: NeedEntity) : DetailItem()
  data class SolutionDetail(val solution: SolutionEntity) : DetailItem()
  data class ServiceDetail(val service: ServiceEntity) : DetailItem()
  data class OpportunityDetail(val opportunity: OpportunityEntity) : DetailItem()
}

sealed class EditItem {
  data class EditNeed(val need: NeedEntity) : EditItem()
  data class EditSolution(val solution: SolutionEntity) : EditItem()
  data class EditService(val service: ServiceEntity) : EditItem()
  data class EditOpportunity(val opportunity: OpportunityEntity) : EditItem()
}

enum class CreateType {
  NEED,
  SOLUTION,
  SERVICE,
  OPPORTUNITY
}

data class UserStats(
  val needsCount: Int = 0,
  val solutionsCount: Int = 0,
  val servicesCount: Int = 0,
  val bookmarksCount: Int = 0
)

class XaldoonViewModel(application: Application) : AndroidViewModel(application) {

  private val database = AppDatabase.getDatabase(application, viewModelScope)
  val repository = XaldoonRepository(database)

  init {
    viewModelScope.launch {
      repository.ensureInitialized()
    }
  }

  // Navigation State
  private val _currentTab = MutableStateFlow(MainTab.HOME)
  val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

  private val _activeDetail = MutableStateFlow<DetailItem?>(null)
  val activeDetail: StateFlow<DetailItem?> = _activeDetail.asStateFlow()

  private val _editingItem = MutableStateFlow<EditItem?>(null)
  val editingItem: StateFlow<EditItem?> = _editingItem.asStateFlow()

  private val _activeCreateType = MutableStateFlow<CreateType?>(null)
  val activeCreateType: StateFlow<CreateType?> = _activeCreateType.asStateFlow()

  private val _isEditingProfile = MutableStateFlow(false)
  val isEditingProfile: StateFlow<Boolean> = _isEditingProfile.asStateFlow()

  // User Profile
  val userProfile: StateFlow<UserProfile?> = repository.userProfile
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Language & Theme
  private val _appLanguage = MutableStateFlow("en")
  val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

  private val _darkModeOverride = MutableStateFlow<Boolean?>(null)
  val darkModeOverride: StateFlow<Boolean?> = _darkModeOverride.asStateFlow()

  // Data streams
  val allNeeds: StateFlow<List<NeedEntity>> = repository.allNeeds
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allSolutions: StateFlow<List<SolutionEntity>> = repository.allSolutions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allServices: StateFlow<List<ServiceEntity>> = repository.allServices
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allOpportunities: StateFlow<List<OpportunityEntity>> = repository.allOpportunities
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // User Stats
  val userStats: StateFlow<UserStats> = combine(
    repository.userNeeds,
    repository.userSolutions,
    repository.userServices,
    repository.allBookmarks
  ) { needs, solutions, services, bMarks ->
    UserStats(
      needsCount = needs.size,
      solutionsCount = solutions.size,
      servicesCount = services.size,
      bookmarksCount = bMarks.size
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStats())

  // Explore Filter & Search State
  val searchQuery = MutableStateFlow("")
  val filterContentType = MutableStateFlow(FilterContentType.ALL)
  val selectedCategoryFilter = MutableStateFlow<String?>(null)
  val selectedLocationFilter = MutableStateFlow<String?>(null)
  val sortDirection = MutableStateFlow(SortDirection.NEWEST)

  // Opportunities Screen Filters
  val opportunityTypeFilter = MutableStateFlow<String?>(null)
  val opportunitiesOnlyBookmarked = MutableStateFlow(false)

  // Feedback Snackbar Message
  private val _snackbarMessage = MutableStateFlow<String?>(null)
  val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

  fun setTab(tab: MainTab) {
    _activeDetail.value = null
    _editingItem.value = null
    _activeCreateType.value = null
    _isEditingProfile.value = false
    _currentTab.value = tab
  }

  fun openDetail(item: DetailItem) {
    _activeDetail.value = item
  }

  fun closeDetail() {
    _activeDetail.value = null
  }

  fun startCreate(type: CreateType) {
    _editingItem.value = null
    _activeCreateType.value = type
  }

  fun cancelCreate() {
    _activeCreateType.value = null
  }

  fun startEdit(item: EditItem) {
    _activeDetail.value = null
    _editingItem.value = item
  }

  fun cancelEdit() {
    _editingItem.value = null
  }

  fun setEditingProfile(editing: Boolean) {
    _isEditingProfile.value = editing
  }

  fun clearSnackbarMessage() {
    _snackbarMessage.value = null
  }

  fun showFeedback(message: String) {
    _snackbarMessage.value = message
  }

  // Language and theme
  fun setLanguage(lang: String) {
    _appLanguage.value = lang
    viewModelScope.launch {
      val profile = userProfile.value
      if (profile != null) {
        repository.saveProfile(profile.copy(preferredLanguage = lang))
      }
    }
  }

  fun setDarkMode(enabled: Boolean?) {
    _darkModeOverride.value = enabled
    viewModelScope.launch {
      val profile = userProfile.value
      if (profile != null) {
        repository.saveProfile(profile.copy(isDarkMode = enabled))
      }
    }
  }

  // Profile operations
  fun completeOnboarding(
    fullName: String,
    bio: String,
    country: String,
    city: String,
    skills: String,
    interests: String
  ) {
    viewModelScope.launch {
      val profile = UserProfile(
        id = "current_user",
        fullName = fullName.ifBlank { "Xaldoon Member" },
        bio = bio,
        country = country.ifBlank { "Somalia" },
        city = city.ifBlank { "Mogadishu" },
        skills = skills,
        interests = interests,
        preferredLanguage = _appLanguage.value,
        isDarkMode = _darkModeOverride.value,
        isOnboarded = true
      )
      repository.saveProfile(profile)
      showFeedback("Profile created successfully!")
    }
  }

  fun updateProfile(
    fullName: String,
    bio: String,
    country: String,
    city: String,
    skills: String,
    interests: String
  ) {
    viewModelScope.launch {
      val current = userProfile.value ?: UserProfile(
        fullName = fullName,
        bio = bio,
        country = country,
        city = city,
        skills = skills,
        interests = interests,
        isOnboarded = true
      )
      repository.saveProfile(
        current.copy(
          fullName = fullName,
          bio = bio,
          country = country,
          city = city,
          skills = skills,
          interests = interests
        )
      )
      _isEditingProfile.value = false
      showFeedback("Profile updated successfully")
    }
  }

  // CRUD for Needs
  fun saveNeed(
    id: Long = 0,
    title: String,
    description: String,
    category: String,
    country: String,
    city: String,
    contactDetails: String,
    status: String
  ) {
    viewModelScope.launch {
      val author = userProfile.value?.fullName ?: "Local User"
      val need = NeedEntity(
        id = id,
        title = title.trim(),
        description = description.trim(),
        category = category,
        country = country.trim(),
        city = city.trim(),
        contactDetails = contactDetails.trim(),
        status = status,
        isDemo = false,
        authorName = author,
        createdAt = if (id == 0L) System.currentTimeMillis() else (allNeeds.value.find { it.id == id }?.createdAt ?: System.currentTimeMillis())
      )
      if (id == 0L) {
        repository.insertNeed(need)
        showFeedback("Need published successfully")
      } else {
        repository.updateNeed(need)
        showFeedback("Need updated successfully")
      }
      _activeCreateType.value = null
      _editingItem.value = null
    }
  }

  fun deleteNeed(need: NeedEntity) {
    viewModelScope.launch {
      repository.deleteNeed(need)
      _activeDetail.value = null
      _editingItem.value = null
      showFeedback("Need deleted")
    }
  }

  // CRUD for Solutions
  fun saveSolution(
    id: Long = 0,
    title: String,
    description: String,
    relatedNeedId: Long?,
    skills: String,
    price: String?,
    status: String
  ) {
    viewModelScope.launch {
      val author = userProfile.value?.fullName ?: "Local Innovator"
      val solution = SolutionEntity(
        id = id,
        title = title.trim(),
        description = description.trim(),
        relatedNeedId = relatedNeedId,
        skills = skills.trim(),
        price = price?.trim()?.ifBlank { null },
        status = status,
        isDemo = false,
        authorName = author,
        createdAt = if (id == 0L) System.currentTimeMillis() else (allSolutions.value.find { it.id == id }?.createdAt ?: System.currentTimeMillis())
      )
      if (id == 0L) {
        repository.insertSolution(solution)
        showFeedback("Solution offered successfully")
      } else {
        repository.updateSolution(solution)
        showFeedback("Solution updated successfully")
      }
      _activeCreateType.value = null
      _editingItem.value = null
    }
  }

  fun deleteSolution(solution: SolutionEntity) {
    viewModelScope.launch {
      repository.deleteSolution(solution)
      _activeDetail.value = null
      _editingItem.value = null
      showFeedback("Solution deleted")
    }
  }

  // CRUD for Services
  fun saveService(
    id: Long = 0,
    title: String,
    description: String,
    category: String,
    skills: String,
    price: String?,
    country: String,
    city: String,
    availabilityStatus: String
  ) {
    viewModelScope.launch {
      val provider = userProfile.value?.fullName ?: "Local Specialist"
      val service = ServiceEntity(
        id = id,
        title = title.trim(),
        description = description.trim(),
        category = category,
        skills = skills.trim(),
        price = price?.trim()?.ifBlank { null },
        country = country.trim(),
        city = city.trim(),
        availabilityStatus = availabilityStatus,
        isDemo = false,
        providerName = provider,
        createdAt = if (id == 0L) System.currentTimeMillis() else (allServices.value.find { it.id == id }?.createdAt ?: System.currentTimeMillis())
      )
      if (id == 0L) {
        repository.insertService(service)
        showFeedback("Service listed successfully")
      } else {
        repository.updateService(service)
        showFeedback("Service updated successfully")
      }
      _activeCreateType.value = null
      _editingItem.value = null
    }
  }

  fun deleteService(service: ServiceEntity) {
    viewModelScope.launch {
      repository.deleteService(service)
      _activeDetail.value = null
      _editingItem.value = null
      showFeedback("Service deleted")
    }
  }

  // CRUD for Opportunities
  fun saveOpportunity(
    id: Long = 0,
    title: String,
    type: String,
    category: String,
    organization: String,
    description: String,
    requirements: String,
    country: String,
    city: String,
    deadline: String
  ) {
    viewModelScope.launch {
      val opp = OpportunityEntity(
        id = id,
        title = title.trim(),
        type = type,
        category = category,
        organization = organization.trim().ifBlank { "Community Partner" },
        description = description.trim(),
        requirements = requirements.trim(),
        country = country.trim(),
        city = city.trim(),
        deadline = deadline.trim(),
        isDemo = false,
        createdAt = if (id == 0L) System.currentTimeMillis() else (allOpportunities.value.find { it.id == id }?.createdAt ?: System.currentTimeMillis())
      )
      if (id == 0L) {
        repository.insertOpportunity(opp)
        showFeedback("Opportunity published successfully")
      } else {
        repository.updateOpportunity(opp)
        showFeedback("Opportunity updated successfully")
      }
      _activeCreateType.value = null
      _editingItem.value = null
    }
  }

  fun deleteOpportunity(opportunity: OpportunityEntity) {
    viewModelScope.launch {
      repository.deleteOpportunity(opportunity)
      _activeDetail.value = null
      _editingItem.value = null
      showFeedback("Opportunity deleted")
    }
  }

  // Bookmarking
  fun toggleBookmark(type: String, id: Long) {
    viewModelScope.launch {
      repository.toggleBookmark(type, id)
    }
  }

  fun isBookmarked(type: String, id: Long): Boolean {
    return bookmarks.value.any { it.targetType == type && it.targetId == id }
  }

  // Clear data
  fun clearUserRecords() {
    viewModelScope.launch {
      repository.clearUserCreatedRecords()
      showFeedback("User created records cleared")
    }
  }

  fun resetAllProfileData() {
    viewModelScope.launch {
      repository.deleteProfile()
      showFeedback("Profile reset")
    }
  }
}

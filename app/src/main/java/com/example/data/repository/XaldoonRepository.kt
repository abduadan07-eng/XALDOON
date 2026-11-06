package com.example.data.repository

import com.example.data.AppDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.SolutionEntity
import com.example.data.model.UserProfile
import com.example.data.populateInitialData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class XaldoonRepository(private val database: AppDatabase) {

  // Profile
  val userProfile: Flow<UserProfile?> = database.userProfileDao().getUserProfileFlow()

  suspend fun saveProfile(profile: UserProfile) {
    database.userProfileDao().insertOrUpdateProfile(profile)
  }

  suspend fun deleteProfile() {
    database.userProfileDao().deleteProfile()
  }

  // Needs
  val allNeeds: Flow<List<NeedEntity>> = database.needDao().getAllNeeds()
  val userNeeds: Flow<List<NeedEntity>> = database.needDao().getUserNeeds()

  fun getNeed(id: Long): Flow<NeedEntity?> = database.needDao().getNeedById(id)

  suspend fun insertNeed(need: NeedEntity): Long = database.needDao().insertNeed(need)
  suspend fun updateNeed(need: NeedEntity) = database.needDao().updateNeed(need)
  suspend fun deleteNeed(need: NeedEntity) = database.needDao().deleteNeed(need)
  suspend fun deleteNeedById(id: Long) = database.needDao().deleteNeedById(id)

  // Solutions
  val allSolutions: Flow<List<SolutionEntity>> = database.solutionDao().getAllSolutions()
  val userSolutions: Flow<List<SolutionEntity>> = database.solutionDao().getUserSolutions()

  fun getSolution(id: Long): Flow<SolutionEntity?> = database.solutionDao().getSolutionById(id)
  fun getSolutionsForNeed(needId: Long): Flow<List<SolutionEntity>> = database.solutionDao().getSolutionsForNeed(needId)

  suspend fun insertSolution(solution: SolutionEntity): Long = database.solutionDao().insertSolution(solution)
  suspend fun updateSolution(solution: SolutionEntity) = database.solutionDao().updateSolution(solution)
  suspend fun deleteSolution(solution: SolutionEntity) = database.solutionDao().deleteSolution(solution)
  suspend fun deleteSolutionById(id: Long) = database.solutionDao().deleteSolutionById(id)

  // Services
  val allServices: Flow<List<ServiceEntity>> = database.serviceDao().getAllServices()
  val userServices: Flow<List<ServiceEntity>> = database.serviceDao().getUserServices()

  fun getService(id: Long): Flow<ServiceEntity?> = database.serviceDao().getServiceById(id)

  suspend fun insertService(service: ServiceEntity): Long = database.serviceDao().insertService(service)
  suspend fun updateService(service: ServiceEntity) = database.serviceDao().updateService(service)
  suspend fun deleteService(service: ServiceEntity) = database.serviceDao().deleteService(service)
  suspend fun deleteServiceById(id: Long) = database.serviceDao().deleteServiceById(id)

  // Opportunities
  val allOpportunities: Flow<List<OpportunityEntity>> = database.opportunityDao().getAllOpportunities()
  val userOpportunities: Flow<List<OpportunityEntity>> = database.opportunityDao().getUserOpportunities()

  fun getOpportunity(id: Long): Flow<OpportunityEntity?> = database.opportunityDao().getOpportunityById(id)

  suspend fun insertOpportunity(opportunity: OpportunityEntity): Long = database.opportunityDao().insertOpportunity(opportunity)
  suspend fun updateOpportunity(opportunity: OpportunityEntity) = database.opportunityDao().updateOpportunity(opportunity)
  suspend fun deleteOpportunity(opportunity: OpportunityEntity) = database.opportunityDao().deleteOpportunity(opportunity)
  suspend fun deleteOpportunityById(id: Long) = database.opportunityDao().deleteOpportunityById(id)

  // Bookmarks
  val allBookmarks: Flow<List<BookmarkEntity>> = database.bookmarkDao().getAllBookmarks()

  fun isBookmarked(targetType: String, targetId: Long): Flow<Boolean> {
    return database.bookmarkDao().isBookmarked(targetType, targetId)
  }

  suspend fun toggleBookmark(targetType: String, targetId: Long) {
    val currentlyBookmarked = database.bookmarkDao().isBookmarked(targetType, targetId).firstOrNull() ?: false
    if (currentlyBookmarked) {
      database.bookmarkDao().deleteBookmark(targetType, targetId)
    } else {
      database.bookmarkDao().insertBookmark(BookmarkEntity(targetType = targetType, targetId = targetId))
    }
  }

  // Categories & Skills
  val allCategories: Flow<List<CategoryEntity>> = database.categoryDao().getAllCategories()
  val allSkills: Flow<List<com.example.data.model.SkillEntity>> = database.skillDao().getAllSkills()

  // Seeding check
  suspend fun ensureInitialized() {
    val count = database.categoryDao().getCategoryCount()
    if (count == 0) {
      populateInitialData(database)
    }
  }

  // Delete all user created records
  suspend fun clearUserCreatedRecords() {
    database.needDao().deleteUserNeeds()
    database.solutionDao().deleteUserSolutions()
    database.serviceDao().deleteUserServices()
    database.opportunityDao().deleteUserOpportunities()
  }
}

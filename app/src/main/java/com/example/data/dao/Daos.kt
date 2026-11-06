package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.SkillEntity
import com.example.data.model.SolutionEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
  @Query("SELECT * FROM user_profile WHERE id = 'current_user' LIMIT 1")
  fun getUserProfileFlow(): Flow<UserProfile?>

  @Query("SELECT * FROM user_profile WHERE id = 'current_user' LIMIT 1")
  suspend fun getUserProfile(): UserProfile?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateProfile(profile: UserProfile)

  @Query("DELETE FROM user_profile WHERE id = 'current_user'")
  suspend fun deleteProfile()
}

@Dao
interface NeedDao {
  @Query("SELECT * FROM needs ORDER BY createdAt DESC")
  fun getAllNeeds(): Flow<List<NeedEntity>>

  @Query("SELECT * FROM needs WHERE id = :id LIMIT 1")
  fun getNeedById(id: Long): Flow<NeedEntity?>

  @Query("SELECT * FROM needs WHERE isDemo = 0 ORDER BY createdAt DESC")
  fun getUserNeeds(): Flow<List<NeedEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNeed(need: NeedEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(needs: List<NeedEntity>)

  @Update
  suspend fun updateNeed(need: NeedEntity)

  @Delete
  suspend fun deleteNeed(need: NeedEntity)

  @Query("DELETE FROM needs WHERE id = :id")
  suspend fun deleteNeedById(id: Long)

  @Query("DELETE FROM needs WHERE isDemo = 0")
  suspend fun deleteUserNeeds()
}

@Dao
interface SolutionDao {
  @Query("SELECT * FROM solutions ORDER BY createdAt DESC")
  fun getAllSolutions(): Flow<List<SolutionEntity>>

  @Query("SELECT * FROM solutions WHERE id = :id LIMIT 1")
  fun getSolutionById(id: Long): Flow<SolutionEntity?>

  @Query("SELECT * FROM solutions WHERE relatedNeedId = :needId ORDER BY createdAt DESC")
  fun getSolutionsForNeed(needId: Long): Flow<List<SolutionEntity>>

  @Query("SELECT * FROM solutions WHERE isDemo = 0 ORDER BY createdAt DESC")
  fun getUserSolutions(): Flow<List<SolutionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSolution(solution: SolutionEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(solutions: List<SolutionEntity>)

  @Update
  suspend fun updateSolution(solution: SolutionEntity)

  @Delete
  suspend fun deleteSolution(solution: SolutionEntity)

  @Query("DELETE FROM solutions WHERE id = :id")
  suspend fun deleteSolutionById(id: Long)

  @Query("DELETE FROM solutions WHERE isDemo = 0")
  suspend fun deleteUserSolutions()
}

@Dao
interface ServiceDao {
  @Query("SELECT * FROM services ORDER BY createdAt DESC")
  fun getAllServices(): Flow<List<ServiceEntity>>

  @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
  fun getServiceById(id: Long): Flow<ServiceEntity?>

  @Query("SELECT * FROM services WHERE isDemo = 0 ORDER BY createdAt DESC")
  fun getUserServices(): Flow<List<ServiceEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertService(service: ServiceEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(services: List<ServiceEntity>)

  @Update
  suspend fun updateService(service: ServiceEntity)

  @Delete
  suspend fun deleteService(service: ServiceEntity)

  @Query("DELETE FROM services WHERE id = :id")
  suspend fun deleteServiceById(id: Long)

  @Query("DELETE FROM services WHERE isDemo = 0")
  suspend fun deleteUserServices()
}

@Dao
interface OpportunityDao {
  @Query("SELECT * FROM opportunities ORDER BY createdAt DESC")
  fun getAllOpportunities(): Flow<List<OpportunityEntity>>

  @Query("SELECT * FROM opportunities WHERE id = :id LIMIT 1")
  fun getOpportunityById(id: Long): Flow<OpportunityEntity?>

  @Query("SELECT * FROM opportunities WHERE isDemo = 0 ORDER BY createdAt DESC")
  fun getUserOpportunities(): Flow<List<OpportunityEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOpportunity(opportunity: OpportunityEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(opportunities: List<OpportunityEntity>)

  @Update
  suspend fun updateOpportunity(opportunity: OpportunityEntity)

  @Delete
  suspend fun deleteOpportunity(opportunity: OpportunityEntity)

  @Query("DELETE FROM opportunities WHERE id = :id")
  suspend fun deleteOpportunityById(id: Long)

  @Query("DELETE FROM opportunities WHERE isDemo = 0")
  suspend fun deleteUserOpportunities()
}

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
  fun getAllBookmarks(): Flow<List<BookmarkEntity>>

  @Query("SELECT COUNT(*) > 0 FROM bookmarks WHERE targetType = :targetType AND targetId = :targetId")
  fun isBookmarked(targetType: String, targetId: Long): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBookmark(bookmark: BookmarkEntity)

  @Query("DELETE FROM bookmarks WHERE targetType = :targetType AND targetId = :targetId")
  suspend fun deleteBookmark(targetType: String, targetId: Long)

  @Query("DELETE FROM bookmarks")
  suspend fun clearAllBookmarks()
}

@Dao
interface CategoryDao {
  @Query("SELECT * FROM categories ORDER BY id ASC")
  fun getAllCategories(): Flow<List<CategoryEntity>>

  @Query("SELECT COUNT(*) FROM categories")
  suspend fun getCategoryCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCategories(categories: List<CategoryEntity>)
}

@Dao
interface SkillDao {
  @Query("SELECT * FROM skills ORDER BY name ASC")
  fun getAllSkills(): Flow<List<SkillEntity>>

  @Query("SELECT COUNT(*) FROM skills")
  suspend fun getSkillCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSkills(skills: List<SkillEntity>)
}

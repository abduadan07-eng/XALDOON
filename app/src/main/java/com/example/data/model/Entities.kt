package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey val id: String = "current_user",
  val fullName: String,
  val bio: String,
  val country: String = "Somalia",
  val city: String = "Mogadishu",
  val skills: String = "",
  val interests: String = "",
  val portfolioLinks: String = "",
  val preferredLanguage: String = "en", // "en" or "so"
  val isDarkMode: Boolean? = null, // null follows system
  val isOnboarded: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "needs")
data class NeedEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val description: String,
  val category: String,
  val country: String = "",
  val city: String = "",
  val contactDetails: String = "",
  val status: String = "Open", // Open, In Progress, Solved, Closed
  val isDemo: Boolean = false,
  val authorName: String = "Anonymous",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "solutions")
data class SolutionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val description: String,
  val relatedNeedId: Long? = null,
  val skills: String = "",
  val price: String? = null,
  val status: String = "Offered", // Draft, Offered, Accepted, Completed
  val isDemo: Boolean = false,
  val authorName: String = "Anonymous",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "services")
data class ServiceEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val description: String,
  val category: String,
  val skills: String = "",
  val price: String? = null,
  val country: String = "",
  val city: String = "",
  val availabilityStatus: String = "Available", // Available, Busy, Upon Request
  val isDemo: Boolean = false,
  val providerName: String = "Professional",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "opportunities")
data class OpportunityEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val type: String = "Job", // Job, Freelancing, Business, Partnership, Education, Internship, Volunteering, Investment, Service
  val category: String,
  val organization: String,
  val description: String,
  val requirements: String = "",
  val country: String = "",
  val city: String = "",
  val deadline: String = "",
  val isDemo: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val targetType: String, // NEED, SOLUTION, SERVICE, OPPORTUNITY
  val targetId: Long,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class CategoryEntity(
  @PrimaryKey val id: String,
  val nameEn: String,
  val nameSo: String,
  val iconName: String,
  val description: String = ""
)

@Entity(tableName = "skills")
data class SkillEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val category: String = "General"
)

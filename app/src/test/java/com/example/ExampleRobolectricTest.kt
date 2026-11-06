package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.SolutionEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  private lateinit var db: AppDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun appNameIsXaldoon() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("XALDOON", appName)
    val tagline = context.getString(R.string.tagline)
    assertEquals("Every Problem Deserves a Solution", tagline)
  }

  @Test
  fun testUserProfileLifecycle() = runBlocking {
    val userProfileDao = db.userProfileDao()
    val profile = UserProfile(
      id = "current_user",
      fullName = "Abdirahman Mohamed",
      bio = "Software Architect",
      country = "Somalia",
      city = "Mogadishu",
      skills = "Kotlin, Architecture, Clean Water",
      interests = "Social Impact",
      preferredLanguage = "so",
      isOnboarded = true
    )

    userProfileDao.insertOrUpdateProfile(profile)
    val fetched = userProfileDao.getUserProfile()
    assertNotNull(fetched)
    assertEquals("Abdirahman Mohamed", fetched?.fullName)
    assertEquals("so", fetched?.preferredLanguage)
    assertTrue(fetched?.isOnboarded == true)

    userProfileDao.deleteProfile()
    val afterDelete = userProfileDao.getUserProfile()
    assertEquals(null, afterDelete)
  }

  @Test
  fun testNeedEntityLifecycle() = runBlocking {
    val needDao = db.needDao()
    val need = NeedEntity(
      title = "Clean Water Filtration in Rural Towns",
      description = "Communities need low-cost ceramic water filters to eliminate waterborne pathogens.",
      category = "Energy & Clean Water",
      country = "Somalia",
      city = "Kismayo",
      contactDetails = "water@community.org",
      status = "Open",
      isDemo = false,
      authorName = "Community Leader"
    )

    val id = needDao.insertNeed(need)
    assertTrue(id > 0)

    val allNeeds = needDao.getAllNeeds().first()
    assertEquals(1, allNeeds.size)
    assertEquals("Clean Water Filtration in Rural Towns", allNeeds[0].title)

    // Update status to Solved
    needDao.updateNeed(allNeeds[0].copy(status = "Solved"))
    val updatedNeed = needDao.getNeedById(id).first()
    assertEquals("Solved", updatedNeed?.status)

    // Delete
    needDao.deleteNeedById(id)
    val afterDelete = needDao.getAllNeeds().first()
    assertEquals(0, afterDelete.size)
  }

  @Test
  fun testSolutionAndServicePersistence() = runBlocking {
    val solutionDao = db.solutionDao()
    val serviceDao = db.serviceDao()

    val solution = SolutionEntity(
      title = "Ceramic Pot Filter Mold Design",
      description = "Open source press mold blueprint producing 50 filters per day using local clay and sawdust.",
      skills = "Ceramics, Sanitation",
      price = "Free",
      status = "Offered",
      isDemo = false,
      authorName = "Tech Innovators"
    )
    val solId = solutionDao.insertSolution(solution)
    assertTrue(solId > 0)

    val service = ServiceEntity(
      title = "Off-grid Solar System Maintenance",
      description = "Inspection and diagnostics for inverters and battery storage banks.",
      category = "Energy & Clean Water",
      skills = "PV, Electrical",
      country = "Somalia",
      city = "Hargeisa",
      availabilityStatus = "Available",
      isDemo = false,
      providerName = "Solar Pro"
    )
    val srvId = serviceDao.insertService(service)
    assertTrue(srvId > 0)

    val solutions = solutionDao.getAllSolutions().first()
    val services = serviceDao.getAllServices().first()
    assertEquals(1, solutions.size)
    assertEquals(1, services.size)
  }

  @Test
  fun testOpportunityAndBookmark() = runBlocking {
    val oppDao = db.opportunityDao()
    val bookmarkDao = db.bookmarkDao()

    val opp = OpportunityEntity(
      title = "Junior Android Engineer",
      type = "Job",
      category = "Technology & Software",
      organization = "XALDOON Global",
      description = "Build offline-first Android apps for real-world impact.",
      country = "Somalia",
      city = "Mogadishu",
      deadline = "Dec 31, 2026",
      isDemo = false
    )
    val oppId = oppDao.insertOpportunity(opp)

    // Initially not bookmarked
    val initialBookmark = bookmarkDao.isBookmarked("OPPORTUNITY", oppId).first()
    assertFalse(initialBookmark)

    // Add bookmark
    bookmarkDao.insertBookmark(BookmarkEntity(targetType = "OPPORTUNITY", targetId = oppId))
    val bookmarkedState = bookmarkDao.isBookmarked("OPPORTUNITY", oppId).first()
    assertTrue(bookmarkedState)

    // Remove bookmark
    bookmarkDao.deleteBookmark("OPPORTUNITY", oppId)
    val removedState = bookmarkDao.isBookmarked("OPPORTUNITY", oppId).first()
    assertFalse(removedState)
  }
}

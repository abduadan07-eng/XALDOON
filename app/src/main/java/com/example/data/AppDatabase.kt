package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BookmarkDao
import com.example.data.dao.CategoryDao
import com.example.data.dao.NeedDao
import com.example.data.dao.OpportunityDao
import com.example.data.dao.ServiceDao
import com.example.data.dao.SkillDao
import com.example.data.dao.SolutionDao
import com.example.data.dao.UserProfileDao
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.NeedEntity
import com.example.data.model.OpportunityEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.SkillEntity
import com.example.data.model.SolutionEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    UserProfile::class,
    NeedEntity::class,
    SolutionEntity::class,
    ServiceEntity::class,
    OpportunityEntity::class,
    BookmarkEntity::class,
    CategoryEntity::class,
    SkillEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userProfileDao(): UserProfileDao
  abstract fun needDao(): NeedDao
  abstract fun solutionDao(): SolutionDao
  abstract fun serviceDao(): ServiceDao
  abstract fun opportunityDao(): OpportunityDao
  abstract fun bookmarkDao(): BookmarkDao
  abstract fun categoryDao(): CategoryDao
  abstract fun skillDao(): SkillDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "xaldoon_database"
        )
        .fallbackToDestructiveMigration()
        .addCallback(AppDatabaseCallback(scope))
        .build()
        INSTANCE = instance
        instance
      }
    }
  }

  private class AppDatabaseCallback(
    private val scope: CoroutineScope
  ) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)
      INSTANCE?.let { database ->
        scope.launch(Dispatchers.IO) {
          populateInitialData(database)
        }
      }
    }
  }
}

suspend fun populateInitialData(database: AppDatabase) {
  // Categories
  val categories = listOf(
    CategoryEntity("tech", "Technology & Software", "Farsamada & Software", "terminal", "Software development, electronics, IT systems"),
    CategoryEntity("agri", "Agriculture & Food Security", "Beeraha & Cuntada", "agriculture", "Crop preservation, soil science, irrigation"),
    CategoryEntity("energy", "Energy & Clean Water", "Tamarta & Biyaha", "bolt", "Solar power, desalination, water management"),
    CategoryEntity("health", "Healthcare & Medicine", "Caafimaadka", "medical_services", "Clinics, medical devices, emergency care"),
    CategoryEntity("business", "Business & Trade", "Ganacsiga & Maaliyadda", "storefront", "Retail, exports, fintech, investments"),
    CategoryEntity("education", "Education & Skills", "Waxbarashada & Xirfadaha", "school", "Vocational training, curriculum, mentoring"),
    CategoryEntity("logistics", "Logistics & Transport", "Gaadiidka & Kaabayaasha", "local_shipping", "Supply chains, warehousing, freight"),
    CategoryEntity("community", "Community & Social Impact", "Bulshada & Iskaashiga", "groups", "Youth empowerment, civic cooperation, NGO work")
  )
  database.categoryDao().insertCategories(categories)

  // Demo Needs (Clearly labeled isDemo = true)
  val demoNeeds = listOf(
    NeedEntity(
      id = 1,
      title = "Solar-Powered Water Desalination for Coastal Cooperatives",
      description = "Small-scale coastal fishing and farming cooperatives need affordable, durable solar water distillation units capable of replacing high-cost diesel pumps.",
      category = "Energy & Clean Water",
      country = "Somalia",
      city = "Berbera",
      contactDetails = "coastal.coop@demo.xaldoon",
      status = "Open",
      isDemo = true,
      authorName = "Coastal Fishermen Union"
    ),
    NeedEntity(
      id = 2,
      title = "Cold-Chain Storage for Perishable Mango & Banana Harvests",
      description = "Agricultural producers experience 35% crop spoilage during peak harvest seasons due to lack of grid electricity. Seeking low-power evaporative cooling solutions.",
      category = "Agriculture & Food Security",
      country = "Somalia",
      city = "Afgooye",
      contactDetails = "shabelle.farmers@demo.xaldoon",
      status = "In Progress",
      isDemo = true,
      authorName = "Shabelle Valley Growers"
    ),
    NeedEntity(
      id = 3,
      title = "Offline Health Record Toolkit for Mobile Clinics",
      description = "Mobile health teams operating in pastoralist zones need an offline-first patient record system on tablets that syncs peer-to-peer or when reaching district centers.",
      category = "Healthcare & Medicine",
      country = "Somalia",
      city = "Baidoa",
      contactDetails = "mobile.clinic.lead@demo.xaldoon",
      status = "Open",
      isDemo = true,
      authorName = "Bay Region Health Mission"
    )
  )
  database.needDao().insertAll(demoNeeds)

  // Demo Solutions (Clearly labeled isDemo = true)
  val demoSolutions = listOf(
    SolutionEntity(
      id = 1,
      title = "Modular Passive Solar Distillation Blueprint",
      description = "Open blueprint utilizing local tempered glass and food-grade heat absorbers. Produces 18 liters/day of pure water per module with zero electricity.",
      relatedNeedId = 1,
      skills = "Thermal Dynamics, CAD, Hardware Engineering",
      price = "Free / Open Source",
      status = "Offered",
      isDemo = true,
      authorName = "Clean Water Labs"
    ),
    SolutionEntity(
      id = 2,
      title = "Zero-Electricity Charcoal Evaporative Cooling Chamber",
      description = "Simple double-walled brick chamber packed with wet charcoal that drops internal temperature by 12°C, doubling produce shelf life for smallholders.",
      relatedNeedId = 2,
      skills = "Post-Harvest Agrotechnology, Masonry",
      price = "$45 complete setup",
      status = "Accepted",
      isDemo = true,
      authorName = "AgriCool Innovations"
    )
  )
  database.solutionDao().insertAll(demoSolutions)

  // Demo Services (Clearly labeled isDemo = true)
  val demoServices = listOf(
    ServiceEntity(
      id = 1,
      title = "Renewable Solar PV Installation & Efficiency Auditing",
      description = "Complete commercial and farm solar setup, inverter optimization, and smart battery storage maintenance for reliable off-grid power.",
      category = "Energy & Clean Water",
      skills = "PV Sizing, Inverter Maintenance, Electrical Safety",
      price = "$50 / consultation",
      country = "Somalia",
      city = "Mogadishu",
      availabilityStatus = "Available",
      isDemo = true,
      providerName = "Sahal Energy Solutions"
    ),
    ServiceEntity(
      id = 2,
      title = "Offline-First Mobile & Android Systems Engineering",
      description = "Designing resilient native Android systems that operate seamlessly without cellular reception, tailored for logistics, field teams, and commerce.",
      category = "Technology & Software",
      skills = "Kotlin, Jetpack Compose, SQLite/Room, Architecture",
      price = "$30 / hr",
      country = "Somalia",
      city = "Hargeisa",
      availabilityStatus = "Available",
      isDemo = true,
      providerName = "Horn Tech Architects"
    )
  )
  database.serviceDao().insertAll(demoServices)

  // Demo Opportunities (Clearly labeled isDemo = true)
  val demoOpportunities = listOf(
    OpportunityEntity(
      id = 1,
      title = "Field Renewable Energy Specialist",
      type = "Job",
      category = "Energy & Clean Water",
      organization = "Horseed Green Grid",
      description = "Supervise solar micro-grid installations across rural towns, inspect battery banks, and train community technicians.",
      requirements = "Degree or diploma in Electrical Engineering, 2+ years field solar experience, willingness to travel.",
      country = "Somalia",
      city = "Garowe",
      deadline = "Nov 30, 2026",
      isDemo = true
    ),
    OpportunityEntity(
      id = 2,
      title = "Youth Innovation & Problem-Solving Fellowship 2026",
      type = "Internship",
      category = "Technology & Software",
      organization = "XALDOON Global Labs",
      description = "6-month funded program pairing ambitious problem-solvers with real industry and community challenges.",
      requirements = "Passion for coding, problem-solving, entrepreneurship, or engineering. Open to students and recent graduates.",
      country = "Somalia",
      city = "Mogadishu",
      deadline = "Dec 15, 2026",
      isDemo = true
    ),
    OpportunityEntity(
      id = 3,
      title = "Sesame Processing Expansion Co-Investment",
      type = "Investment",
      category = "Agriculture & Food Security",
      organization = "Jubba Agro Ventures",
      description = "Co-investment opportunity to establish an industrial cold-press sesame oil extraction facility serving regional export markets.",
      requirements = "Accredited investors or agribusiness operators with machinery distribution networks.",
      country = "Somalia",
      city = "Kismayo",
      deadline = "Jan 20, 2027",
      isDemo = true
    )
  )
  database.opportunityDao().insertAll(demoOpportunities)
}

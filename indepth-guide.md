# 📜 the Arbiter (ผู้ชี้นำ) — Complete In-Depth File & Codebase Guide
## `indepth-guide.md`

Welcome to the comprehensive, line-by-line architectural breakdown of **the Arbiter** (`Final_project`). This guide walks through **every single file** in the project, detailing its purpose, architecture, and what each block/line of code accomplishes.

---

# 📑 Table of Contents

1. [Architectural Overview & Core Philosophy](#1-architectural-overview--core-philosophy)
2. [Project Root & Build Configuration](#2-project-root--build-configuration)
   - [`build.gradle.kts` (Root)](#21-buildgradlekts-root)
   - [`settings.gradle.kts`](#22-settingsgradlekts)
   - [`gradle.properties`](#23-gradleproperties)
   - [`gradle/libs.versions.toml`](#24-gradlelibsversionstoml)
   - [`gradle/wrapper/gradle-wrapper.properties`](#25-gradlewrappergradle-wrapperproperties)
   - [`local.properties`](#26-localproperties)
   - [`.gitignore`](#27-gitignore)
3. [App Module Configuration & Manifest](#3-app-module-configuration--manifest)
   - [`app/build.gradle.kts`](#31-appbuildgradlekts)
   - [`app/src/main/AndroidManifest.xml`](#32-appsourcemainandroidmanifestxml)
   - [`app/src/main/keepRules/rules.keep`](#33-appsourcemainkeeprulesruleskeep)
4. [App Resources (`res/`)](#4-app-resources-res)
   - [`res/values/strings.xml` (English Defaults)](#41-resvaluesstringsxml-english)
   - [`res/values-th/strings.xml` (Thai Localization)](#42-resvalues-thstringsxml-thai)
   - [`res/values/colors.xml` & `res/values/themes.xml`](#43-resvaluescolorsxml--resvaluesthemesxml)
   - [`res/xml/backup_rules.xml` & `data_extraction_rules.xml`](#44-resxmlbackup_rulesxml--data_extraction_rulesxml)
5. [Application Entry Point & Context](#5-application-entry-point--context)
   - [`ArbiterApplication.kt`](#51-arbiterapplicationkt)
   - [`MainActivity.kt`](#52-mainactivitykt)
6. [Data Layer — Room Local Persistence](#6-data-layer--room-local-persistence)
   - [`AppDatabase.kt`](#61-appdatabasekt)
   - [`Converters.kt`](#62-converterskt)
   - [`CampaignEntity.kt`](#63-campaignentitykt)
   - [`SessionLogEntity.kt`](#64-sessionlogentitykt)
   - [`CharacterEntity.kt`](#65-characterentitykt)
   - [`UserSettingsEntity.kt`](#66-usersettingsentitykt)
   - [`CampaignDao.kt`](#67-campaigndaokt)
   - [`SessionLogDao.kt`](#68-sessionlogdaokt)
   - [`CharacterDao.kt`](#69-characterdaokt)
   - [`UserSettingsDao.kt`](#610-usersettingsdaokt)
7. [Data Layer — Remote API & AI Models](#7-data-layer--remote-api--ai-models)
   - [`RetrofitClient.kt`](#71-retrofitclientkt)
   - [`Dnd5eApiService.kt`](#72-dnd5eapiservicekt)
   - [`MonsterListDto.kt`](#73-monsterlistdtokt)
   - [`MonsterDetailDto.kt`](#74-monsterdetaildtokt)
   - [`AiModels.kt`](#75-aimodelskt)
8. [Data Layer — Repositories](#8-data-layer--repositories)
   - [`CampaignRepository.kt`](#81-campaignrepositorykt)
   - [`SessionRepository.kt`](#82-sessionrepositorykt)
   - [`CharacterRepository.kt`](#83-characterrepositorykt)
   - [`UserSettingsRepository.kt`](#84-usersettingsrepositorykt)
   - [`DndRepository.kt`](#85-dndrepositorykt)
   - [`AiRepository.kt`](#86-airepositorykt)
9. [UI Layer — Design System, Theme & Components](#9-ui-layer--design-system-theme--components)
   - [`Color.kt`](#91-colorkt)
   - [`Type.kt`](#92-typekt)
   - [`Theme.kt`](#93-themett)
   - [`GrimoireComponents.kt`](#94-grimoirecomponentskt)
   - [`LocaleHelper.kt`](#95-localehelperkt)
10. [UI Layer — Navigation, ViewModels & Screens](#10-ui-layer--navigation-viewmodels--screens)
    - [`ArbiterNavigation.kt`](#101-arbiternavigationkt)
    - [`CampaignViewModel.kt`](#102-campaignviewmodelkt)
    - [`CampaignScreen.kt`](#103-campaignscreenkt)
    - [`SessionArchitectViewModel.kt`](#104-sessionarchitectviewmodelkt)
    - [`SessionArchitectScreen.kt`](#105-sessionarchitectscreenkt)
    - [`DramatisPersonaeViewModel.kt`](#106-dramatispersonaeviewmodelkt)
    - [`DramatisPersonaeScreen.kt`](#107-dramatispersonaescreenkt)
    - [`CompendiumViewModel.kt`](#108-compendiumviewmodelkt)
    - [`CompendiumScreen.kt`](#109-compendiumscreenkt)
11. [Testing Suite](#11-testing-suite)
    - [`ExampleUnitTest.kt`](#111-exampleunittestkt)
    - [`ExampleInstrumentedTest.kt`](#112-exampleinstrumentedtestkt)

---

# 1. Architectural Overview & Core Philosophy

**the Arbiter** is engineered as a modern, reactive, offline-first Android companion application for Tabletop Role-Playing Game (TTRPG) Dungeon Masters running **Dungeons & Dragons 5th Edition (D&D 5e)**.

### The Problem It Solves
Traditional generative AI tools write dialogue, describe tavern rooms, or invent random plot twists. While fun, this often exacerbates the DM's cognitive load by generating generic content that ignores game mechanics.

### The Arbiter's Advisory Sparring Philosophy
Rather than writing the adventure for the DM, **the Arbiter** acts as a senior game design consultant. It interrogates the DM's pre-session ideas using two foundational lenses:
1. **Game Theory Lens:**
   - **Dominant Strategy Check:** Does one spell, action, or path trivialize the others, stripping players of genuine choices?
   - **Zero-Sum vs. Non-Zero-Sum Stakes:** Are players trapped in Prisoner's Dilemmas, trade-offs, or asymmetric payoffs?
   - **Nash Equilibria & Information Asymmetry:** What crucial information is hidden by antagonists or known only to the DM, and how can player leverage be earned?
2. **Player Psychology Lens:**
   - **Dramatic Pacing Curves:** Are tension cycles earned and followed by relief, or is the scenario emotionally exhausting?
   - **Bartle Archetypes Analysis:** How will Achievers (gamers seeking stats/leveling), Explorers (seeking secrets/lore), Socializers (seeking roleplay/alliances), and Killers (seeking tactical combat mastery) react?
   - **Agency Protection:** Does the scenario artificially railroad the party into a predetermined outcome, or are there contingency branches?

---

# 2. Project Root & Build Configuration

### 2.1 `build.gradle.kts` (Root)
**File Location:** `/Final_project/build.gradle.kts`

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
```

#### Line-by-Line Breakdown:
- **Lines 1–2 (`plugins {`):** Declares top-level Gradle plugins shared across all sub-modules without applying them directly to the root project.
- **Line 3 (`alias(libs.plugins.android.application) apply false`):** Declares Android Application Plugin via the Gradle Version Catalog (`libs.versions.toml`). `apply false` prevents execution in root project.
- **Line 4 (`alias(libs.plugins.kotlin.compose) apply false`):** Declares official Kotlin Compose Compiler Gradle Plugin (`org.jetbrains.kotlin.plugin.compose`), introduced in Kotlin 2.0+ to replace legacy Compose compiler extensions.
- **Line 5 (`alias(libs.plugins.ksp) apply false`):** Declares Kotlin Symbol Processing (KSP) plugin used for annotation processing by Room database code generation.
- **Line 6 (`}`):** Closes root plugin block.

---

### 2.2 `settings.gradle.kts`
**File Location:** `/Final_project/settings.gradle.kts`

#### Code & Breakdown:
- **Lines 1–13 (`pluginManagement`):** Defines where Gradle retrieves build plugins.
  - `google { content { includeGroupByRegex("com\\.android.*"); ... } }`: Directs Android, Google, and AndroidX dependencies to Google's Maven repository with regex filtering to speed up resolution.
  - `mavenCentral()`: Resolves open-source third-party plugins.
  - `gradlePluginPortal()`: Resolves core Gradle plugins.
- **Lines 14–16 (`plugins`):** Registers `org.gradle.toolchains.foojay-resolver-convention` v1.0.0, enabling automatic downloading and configuration of JDK toolchains (JDK 17/21).
- **Lines 17–23 (`dependencyResolutionManagement`):**
  - `repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)`: Strict Gradle security mode; prevents individual modules from defining custom repositories, ensuring all dependencies come from verified sources.
  - `repositories { google(); mavenCentral() }`: Sets default repositories for all project libraries.
- **Line 25 (`rootProject.name = "Final_project"`):** Defines project name.
- **Line 26 (`include(":app")`):** Registers the `:app` module for compilation.

---

### 2.3 `gradle.properties`
**File Location:** `/Final_project/gradle.properties`

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.configuration-cache=true
kotlin.code.style=official
org.gradle.java.home=/home/copter/.jdks/jbr-21.0.11
android.disallowKotlinSourceSets=false
android.sync.suppressAgpWarnings=UNSUPPORTED_PROJECT_OPTION_USE
```

#### Line-by-Line Breakdown:
- **Line 2 (`org.gradle.jvmargs`):** Allocates up to 2048MB heap memory to the Gradle daemon and enforces UTF-8 character encoding to support Thai and Unicode scripts seamlessly.
- **Line 3 (`org.gradle.configuration-cache=true`):** Caches Gradle's configuration phase, radically accelerating subsequent builds.
- **Line 4 (`kotlin.code.style=official`):** Enforces standard Kotlin code formatting rules.
- **Line 5 (`org.gradle.java.home`):** Explicitly points the build system to JDK 21 runtime.
- **Lines 6–7:** Configures AGP and Kotlin source set compatibility flags to prevent build warnings.

---

### 2.4 `gradle/libs.versions.toml`
**File Location:** `/Final_project/gradle/libs.versions.toml`

Central Version Catalog defining dependencies and plugin versions:

- **`[versions]`:**
  - `agp = "9.4.1"`: Android Gradle Plugin version.
  - `kotlin = "2.2.10"` & `ksp = "2.2.10-2.0.2"`: Modern Kotlin compiler and matching KSP binary.
  - `composeBom = "2026.02.01"`: Jetpack Compose Bill of Materials ensuring all Compose libraries share tested compatible versions.
  - `room = "2.7.2"`: Modern Room Database supporting SQLite Coroutines and KSP.
  - `retrofit = "2.11.0"` & `okhttp = "4.12.0"`: REST API client and HTTP engine.
  - `generativeAi = "0.9.0"`: Google's official Gemini Android SDK (`com.google.ai.client.generativeai`).
  - `coroutines = "1.9.0"`: Kotlin Coroutines Android runtime.
- **`[libraries]`:** Maps alias identifiers (e.g. `androidx-room-runtime`, `retrofit`, `google-generativeai`) to group, artifact name, and version reference.
- **`[plugins]`:** Maps plugin identifiers (`android-application`, `kotlin-compose`, `ksp`).

---

### 2.5 `gradle/wrapper/gradle-wrapper.properties`
**File Location:** `/Final_project/gradle/wrapper/gradle-wrapper.properties`

Defines the Gradle wrapper binary distribution:
- `distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip`: Guarantees reproducible builds using Gradle 8.13 across different machines.

---

### 2.6 `local.properties`
**File Location:** `/Final_project/local.properties`

Machine-specific configuration:
- `sdk.dir=/home/copter/Android/Sdk`: Absolute path to Android SDK platform tools and build-tools.
- Optional key: `gemini.api.key=...`: Can store default developer API key if baked in at compile time.

---

### 2.7 `.gitignore`
**File Location:** `/Final_project/.gitignore`

Excludes build artifacts, caches, IDE generated files (`.gradle`, `/build`, `*.iml`, `local.properties`) from version control.

---

# 3. App Module Configuration & Manifest

### 3.1 `app/build.gradle.kts`
**File Location:** `/Final_project/app/build.gradle.kts`

#### Code Breakdown:
- **Lines 1–5 (`plugins`):** Applies `android.application`, `kotlin.compose`, and `ksp`.
- **Lines 10–16 (`localProperties` extraction):**
  ```kotlin
  val localProperties = Properties().apply {
      val localFile = rootProject.file("local.properties")
      if (localFile.exists()) {
          load(FileInputStream(localFile))
      }
  }
  val geminiApiKey: String = localProperties.getProperty("gemini.api.key") ?: ""
  ```
  Safely loads `gemini.api.key` from `local.properties` without exposing secret credentials to version control.
- **Lines 18–33 (`android { defaultConfig }`):**
  - `namespace = "com.example.final_project"`: Package namespace.
  - `minSdk = 29` (Android 10): Guarantees modern SQLite, cryptography, and Material3 styling.
  - `targetSdk = 37` & `compileSdk = 37`: Targets latest Android SDK.
  - `buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")`: Generates a static constant `BuildConfig.GEMINI_API_KEY` for compile-time fallback key extraction.
- **Lines 43–46 (`compileOptions`):** Sets Java 17 compatibility.
- **Lines 47–50 (`buildFeatures`):** Enables `compose = true` and `buildConfig = true`.
- **Lines 53–92 (`dependencies`):**
  - Compose BOM, Material3, Extended Icons, Navigation Compose.
  - Room runtime, Room KTX, KSP compiler.
  - Retrofit, OkHttp, Gson converter, HttpLoggingInterceptor.
  - Google Generative AI SDK (`libs.google.generativeai`).
  - Unit testing (`junit`) and Compose instrumentation testing libraries.

---

### 3.2 `app/src/main/AndroidManifest.xml`
**File Location:** `/Final_project/app/src/main/AndroidManifest.xml`

#### Line-by-Line Breakdown:
- **Line 5 (`<uses-permission android:name="android.permission.INTERNET" />`):** Mandatory for querying the D&D 5e SRD REST API and communicating with Google Gemini AI.
- **Line 6 (`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`):** Enables network availability detection.
- **Line 9 (`android:name=".ArbiterApplication"`):** Hooks custom `ArbiterApplication` subclass into the Android process lifecycle.
- **Lines 18–29 (`<activity android:name=".MainActivity" ...`):**
  - `android:windowSoftInputMode="adjustResize"`: Critical for UX; ensures the screen and encounter drafts smoothly resize when the virtual keyboard pops up without obscuring the input fields.
  - `<intent-filter>`: Designates `MainActivity` as the primary launcher activity (`MAIN` + `LAUNCHER`).

---

### 3.3 `app/src/main/keepRules/rules.keep`
**File Location:** `/Final_project/app/src/main/keepRules/rules.keep`

R8/ProGuard configuration file to prevent code-shrinker removal of critical classes during release builds.

---

# 4. App Resources (`res/`)

### 4.1 `res/values/strings.xml` (English)
**File Location:** `/Final_project/app/src/main/res/values/strings.xml`

Contains English text tokens for all screens, dialogs, buttons, and error messages:
- **Branding:** `app_name` ("the Arbiter"), `app_tagline` ("Game Theory & Player Psychology Advisory Co-pilot").
- **Navigation:** `nav_campaigns`, `nav_session_architect`, `nav_dramatis_personae`, `nav_grand_compendium`.
- **Screen 1 (Campaigns):** Realm metrics, API key settings, model selection descriptions.
- **Screen 2 (Session Architect):** "Consult the Oracle", "Player Psychology & Dramatic Pacing", "Game Theory & Choice Architecture", "Echoes of the Past".
- **Screen 3 (Dramatis Personae):** "Dread Boss", "Notable NPC", "Minimax Tactics & Non-TPK Win Conditions", "Shadow-Self & Psychological Flaws", "Tit-for-Tat Dynamics".
- **Screen 4 (Compendium):** Search hints, stat badges (CR, AC, HP, actions, traits).

### 4.2 `res/values-th/strings.xml` (Thai Localization)
**File Location:** `/Final_project/app/src/main/res/values-th/strings.xml`

Provides 100% complete Thai translations matching every single key in `values/strings.xml`. Includes tailored fantasy and TTRPG phrasing (e.g. `ผู้ชี้นำ`, `บันทึกแคมเปญ`, `กลยุทธ์ Minimax`, `จุดสมดุลแนช (Nash)`, `คลังข้อมูล SRD สากล`).

### 4.3 `res/values/colors.xml` & `res/values/themes.xml`
- `colors.xml`: Base system colors (`black`, `white`, legacy theme colors).
- `themes.xml`: XML style `Theme.Final_project` extending `Theme.Material.Light.NoActionBar` to hand over full UI window control to Jetpack Compose.

### 4.4 `res/xml/backup_rules.xml` & `data_extraction_rules.xml`
Android 12+ data backup rules controlling what data can be backed up to Google Cloud.

---

# 5. Application Entry Point & Context

### 5.1 `ArbiterApplication.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ArbiterApplication.kt`

```kotlin
package com.example.final_project

import android.app.Application
import com.example.final_project.data.local.AppDatabase
import com.example.final_project.data.repository.CampaignRepository
import com.example.final_project.data.repository.CharacterRepository
import com.example.final_project.data.repository.DndRepository
import com.example.final_project.data.repository.SessionRepository
import com.example.final_project.data.repository.UserSettingsRepository

class ArbiterApplication : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    val campaignRepository: CampaignRepository by lazy {
        CampaignRepository(database.campaignDao())
    }

    val sessionRepository: SessionRepository by lazy {
        SessionRepository(database.sessionLogDao())
    }

    val characterRepository: CharacterRepository by lazy {
        CharacterRepository(database.characterDao())
    }

    val userSettingsRepository: UserSettingsRepository by lazy {
        UserSettingsRepository(database.userSettingsDao())
    }

    val dndRepository: DndRepository by lazy {
        DndRepository()
    }

    val aiRepository: com.example.final_project.data.repository.AiRepository by lazy {
        com.example.final_project.data.repository.AiRepository(userSettingsRepository)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ArbiterApplication
            private set
    }
}
```

#### Line-by-Line Breakdown:
- **Lines 15–16:** Defines `ArbiterApplication` extending Android's `Application` class, acting as the centralized **Manual Dependency Injection Container** (Service Locator).
- **Lines 17–19 (`database`):** Lazily initializes the singleton `AppDatabase` when first accessed.
- **Lines 21–43 (Repositories):** Lazily instantiates all repositories (`campaignRepository`, `sessionRepository`, `characterRepository`, `userSettingsRepository`, `dndRepository`, `aiRepository`), injecting required DAOs or configurations.
- **Lines 45–48 (`onCreate`):** Invoked when app boots; saves `instance = this`.
- **Lines 50–53 (`companion object`):** Exposes `ArbiterApplication.instance` with private setter for safe static access if required.

---

### 5.2 `MainActivity.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/MainActivity.kt`

```kotlin
package com.example.final_project

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.final_project.data.local.entity.AppLanguage
import com.example.final_project.ui.navigation.ArbiterMainScreen
import com.example.final_project.ui.theme.ArbiterTheme
import com.example.final_project.ui.util.LocaleHelper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ArbiterApplication
        val userSettingsRepo = app.userSettingsRepository

        setContent {
            val settings by userSettingsRepo.settingsFlow.collectAsState(initial = null)
            val preferredLanguage = settings?.preferredLanguage ?: AppLanguage.EN

            // Dynamically apply runtime locale to Compose Configuration and LocalContext
            val currentLocale = LocaleHelper.getLocaleForLanguage(preferredLanguage)
            val currentConfig = LocalConfiguration.current
            val updatedConfig = Configuration(currentConfig).apply {
                setLocale(currentLocale)
            }
            val localizedContext = LocalContext.current.createConfigurationContext(updatedConfig)

            CompositionLocalProvider(
                LocalConfiguration provides updatedConfig,
                LocalContext provides localizedContext
            ) {
                ArbiterTheme {
                    ArbiterMainScreen()
                }
            }
        }
    }
}
```

#### Line-by-Line Breakdown:
- **Lines 18–21 (`MainActivity : ComponentActivity()`):** Main activity host.
- **Line 22 (`enableEdgeToEdge()`):** Activates Android edge-to-edge drawing under status bars and navigation bars.
- **Lines 24–25:** Retrieves `userSettingsRepository` from `ArbiterApplication`.
- **Lines 27–47 (`setContent`):**
  - **Line 28:** Collects user settings as StateFlow (`settingsFlow`).
  - **Line 29:** Determines current language (`EN` or `TH`).
  - **Lines 32–37:** Dynamically creates a localized Android `Configuration` and `Context` based on the chosen language without restarting the Activity.
  - **Lines 39–42 (`CompositionLocalProvider`):** Overrides `LocalConfiguration` and `LocalContext` with the localized context.
  - **Lines 43–45:** Wraps the root UI with `ArbiterTheme` and renders `ArbiterMainScreen()`.

---

# 6. Data Layer — Room Local Persistence

### 6.1 `AppDatabase.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/AppDatabase.kt`

```kotlin
@Database(
    entities = [
        CampaignEntity::class,
        SessionLogEntity::class,
        CharacterEntity::class,
        UserSettingsEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun campaignDao(): CampaignDao
    abstract fun sessionLogDao(): SessionLogDao
    abstract fun characterDao(): CharacterDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "arbiter_grimoire.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

#### Line-by-Line Breakdown:
- **Lines 21–30 (`@Database`):** Registers 4 database tables (`CampaignEntity`, `SessionLogEntity`, `CharacterEntity`, `UserSettingsEntity`) at `version = 2`.
- **Line 31 (`@TypeConverters(Converters::class)`):** Registers custom Room type converters for Enums (`CharacterType`, `AppLanguage`).
- **Lines 34–37 (DAO Accessors):** Declares abstract methods returning respective DAOs.
- **Lines 39–56 (`companion object`):** Thread-safe Double-Checked Locking singleton pattern via `@Volatile private var INSTANCE` and `synchronized(this)`.
  - Database file name: `"arbiter_grimoire.db"`.
  - `fallbackToDestructiveMigration(dropAllTables = true)`: Ensures schema updates prevent app crashes during local development.

---

### 6.2 `Converters.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/entity/Converters.kt`

Defines enums and conversion logic:
- **`enum class CharacterType { BOSS, NPC }`:** Distinguishes between Dread Bosses and Notable NPCs.
- **`enum class AppLanguage { EN, TH }`:** Supports runtime language switching between English and Thai.
- **`class Converters`:**
  - `fromCharacterType(value: CharacterType?): String`: Serializes Enum to String name.
  - `toCharacterType(value: String?): CharacterType`: Deserializes String to Enum, defaulting safely to `CharacterType.NPC` on parsing errors.
  - `fromAppLanguage(value: AppLanguage?): String`: Serializes language Enum to String.
  - `toAppLanguage(value: String?): AppLanguage`: Deserializes String to Enum, defaulting safely to `AppLanguage.EN`.

---

### 6.3 `CampaignEntity.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/entity/CampaignEntity.kt`

```kotlin
@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val partyLevel: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
```
- Represents a campaign realm.
- Fields: `id` (Auto-generated primary key), `title`, `description` (world lore), `partyLevel` (1–20), `createdAt` (epoch timestamp).

---

### 6.4 `SessionLogEntity.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/entity/SessionLogEntity.kt`

```kotlin
@Entity(
    tableName = "session_logs",
    foreignKeys = [
        ForeignKey(
            entity = CampaignEntity::class,
            parentColumns = ["id"],
            childColumns = ["campaignId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["campaignId"])
    ]
)
data class SessionLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val campaignId: Long,
    val sessionNumber: Int,
    val pastEventsSummary: String = "",
    val currentDraft: String = "",
    val aiPacingFeedback: String = "",
    val aiDilemmaFeedback: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
```
- Stores prep notes and AI feedback for a session.
- Has a foreign key linking to `campaigns.id` with `onDelete = ForeignKey.CASCADE` (deleting a campaign automatically purges all its sessions).
- Indexed on `campaignId` for fast SQL queries.

---

### 6.5 `CharacterEntity.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/entity/CharacterEntity.kt`

```kotlin
@Entity(
    tableName = "characters",
    foreignKeys = [
        ForeignKey(
            entity = CampaignEntity::class,
            parentColumns = ["id"],
            childColumns = ["campaignId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["campaignId"])
    ]
)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val campaignId: Long,
    val name: String,
    val characterType: CharacterType = CharacterType.NPC,
    val rawBackstoryDraft: String = "",
    val attachedMonsterIndex: String? = null,
    val aiPsychologyFeedback: String = "",
    val aiGameTheoryFeedback: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
```
- Stores Dread Boss or NPC profiles within a campaign.
- Links to SRD monsters via `attachedMonsterIndex` (e.g. `"lich"`, `"adult-red-dragon"`).
- Stores role-specific AI outputs: `aiGameTheoryFeedback` (Minimax for Boss / Tit-for-Tat for NPC) and `aiPsychologyFeedback` (Shadow-self for Boss / Emotional leverage for NPC).

---

### 6.6 `UserSettingsEntity.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/entity/UserSettingsEntity.kt`

```kotlin
@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Long = 1L,
    val preferredLanguage: AppLanguage = AppLanguage.EN,
    val apiKey: String = "",
    val selectedModel: String = "gemini-2.0-flash"
)
```
- Singleton configuration row (fixed `id = 1L`).
- Persists user preferences: `preferredLanguage`, `apiKey`, and `selectedModel`.

---

### 6.7 `CampaignDao.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/dao/CampaignDao.kt`

Data Access Object for campaigns:
- `getAllCampaigns(): Flow<List<CampaignEntity>>`: Emits live reactive campaign list sorted descending by creation time.
- `getCampaignById(id: Long): Flow<CampaignEntity?>`: Reactive single campaign observer.
- `getCampaignByIdSync(id: Long): CampaignEntity?`: Suspend function for one-shot retrieval inside coroutines.
- `insertCampaign(campaign: CampaignEntity): Long`: Inserts or replaces a campaign.
- `updateCampaign(campaign)` & `deleteCampaign(campaign)`: Standard CRUD.
- `deleteCampaignById(id: Long): Int`: Deletes campaign by ID.
- `getSessionCount(campaignId: Long): Flow<Int>`: Live count of sessions in a realm.
- `getBossCount(campaignId: Long): Flow<Int>`: Live count of boss characters.
- `getNpcCount(campaignId: Long): Flow<Int>`: Live count of NPC characters.

---

### 6.8 `SessionLogDao.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/dao/SessionLogDao.kt`

DAO for session logs:
- `getSessionsForCampaign(campaignId)`: Flow of all sessions in ascending order.
- `getLatestSessions(campaignId, limit)`: Flow of recent sessions (used for "Echoes of the Past" context).
- `getNextSessionNumber(campaignId)`: Computes `MAX(sessionNumber) + 1` (defaults to 1 if no sessions exist).
- `insertSession(session)`: Saves session draft and feedback.
- `deleteSessionById(id)`: Deletes session.

---

### 6.9 `CharacterDao.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/dao/CharacterDao.kt`

DAO for characters:
- `getCharactersForCampaign(campaignId)`: Flow of all characters in a realm.
- `getCharactersByType(campaignId, type)`: Flow filtered by `BOSS` or `NPC`.
- `getCharacterById(id)` & `getCharacterByIdSync(id)`: ID-based queries.
- `insertCharacter`, `updateCharacter`, `deleteCharacterById`.

---

### 6.10 `UserSettingsDao.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/local/dao/UserSettingsDao.kt`

DAO for user settings:
- `getSettings(): Flow<UserSettingsEntity?>`: Emits live setting changes.
- `getSettingsSync(): UserSettingsEntity?`: Synchronous one-shot query.
- `insertOrUpdate(settings: UserSettingsEntity)`: Atomic insert/replace of singleton row.
- `updateApiKey(apiKey)` & `updateLanguage(language)`: Dedicated field updates.

---

# 7. Data Layer — Remote API & AI Models

### 7.1 `RetrofitClient.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/remote/RetrofitClient.kt`

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://www.dnd5eapi.co/api/2014/"

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    private val gson by lazy {
        GsonBuilder().create()
    }

    val dnd5eApi: Dnd5eApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Dnd5eApiService::class.java)
    }
}
```

#### Line-by-Line Breakdown:
- **Line 16 (`BASE_URL`):** Points to official D&D 5e SRD REST endpoint (`https://www.dnd5eapi.co/api/2014/`).
- **Lines 18–22 (`loggingInterceptor`):** Configures HTTP request/response logging for debugging network calls.
- **Lines 24–32 (`okHttpClient`):** Sets 15-second connect/read timeouts and enables SSL/HTTP redirect following.
- **Lines 34–37 (`gson`):** Creates JSON serialization engine.
- **Lines 39–46 (`dnd5eApi`):** Builds Retrofit service implementing `Dnd5eApiService`.

---

### 7.2 `Dnd5eApiService.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/remote/api/Dnd5eApiService.kt`

```kotlin
interface Dnd5eApiService {
    @GET("monsters")
    suspend fun getMonsters(
        @Query("name") name: String? = null
    ): MonsterListResponse

    @GET("monsters/{index}")
    suspend fun getMonsterDetail(
        @Path("index") index: String
    ): MonsterDetailDto
}
```
- `getMonsters`: Queries SRD index list with optional name filter.
- `getMonsterDetail`: Queries full statblock by creature slug (e.g., `"lich"`).

---

### 7.3 `MonsterListDto.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/remote/dto/MonsterListDto.kt`

Data Transfer Objects for SRD list responses:
- `data class MonsterItemDto`:
  - `index`: Unique slug identifier (e.g. `"goblin"`).
  - `name`: Display name (e.g. `"Goblin"`).
  - `url`: API endpoint resource URL.
- `data class MonsterListResponse`:
  - `count`: Total count of creatures.
  - `results`: List of `MonsterItemDto`.

---

### 7.4 `MonsterDetailDto.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/remote/dto/MonsterDetailDto.kt`

Data Transfer Objects for complete monster statblocks:
- `data class ArmorClassDto`: Type (e.g., `"natural"`) and numeric value.
- `data class MonsterAbilityDto`: Name and description of actions, traits, or legendary reactions.
- `data class MonsterDetailDto`:
  - Attributes: `name`, `size`, `type`, `alignment`, `armorClass`, `hitPoints`, `hitDice`, `challengeRating`, `specialAbilities`, `actions`, `legendaryActions`.
  - Computed property `displayArmorClass`: Extracts first AC value, defaulting safely to 10.
  - Computed property `displayChallengeRating`: Formats numeric CR into clean string (e.g., `0.25` or `17`).

---

### 7.5 `AiModels.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/remote/ai/AiModels.kt`

Clean data classes representing structured AI analysis outputs:
- `SessionAiAnalysis(pacingFeedback, dilemmaFeedback)`
- `BossAiAnalysis(gameTheoryFeedback, psychologyFeedback)`
- `NpcAiAnalysis(gameTheoryFeedback, psychologyFeedback)`

---

# 8. Data Layer — Repositories

### 8.1 `CampaignRepository.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/repository/CampaignRepository.kt`

Mediates access to `CampaignDao`, exposing:
- `allCampaigns: Flow<List<CampaignEntity>>`
- `getCampaignById(id)`, `getCampaignByIdSync(id)`
- `insertCampaign`, `updateCampaign`, `deleteCampaign`, `deleteCampaignById`
- Real-time stat streams: `getSessionCount(id)`, `getBossCount(id)`, `getNpcCount(id)`

---

### 8.2 `SessionRepository.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/repository/SessionRepository.kt`

Handles session persistence and recap history:
- `getSessionsForCampaign(id)`: All sessions in chronicle order.
- `getLatestSessions(id, limit)`: Last $N$ sessions for AI context injection.
- `getNextSessionNumber(id)`: Pre-computes sequential session number.
- `saveSession`, `updateSession`, `deleteSessionById`.

---

### 8.3 `CharacterRepository.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/repository/CharacterRepository.kt`

Handles boss and NPC persistence:
- `getCharactersForCampaign(id)`: All characters in realm.
- `getCharactersByType(id, type)`: Filtered characters (`BOSS` vs `NPC`).
- `saveCharacter`, `deleteCharacterById`.

---

### 8.4 `UserSettingsRepository.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/repository/UserSettingsRepository.kt`

```kotlin
class UserSettingsRepository(
    private val userSettingsDao: UserSettingsDao
) {
    val settingsFlow: Flow<UserSettingsEntity?> = userSettingsDao.getSettings()

    suspend fun getSettingsSync(): UserSettingsEntity {
        val existing = userSettingsDao.getSettingsSync()
        val buildKey = com.example.final_project.BuildConfig.GEMINI_API_KEY
        if (existing == null) {
            val defaultSettings = UserSettingsEntity(apiKey = buildKey)
            userSettingsDao.insertOrUpdate(defaultSettings)
            return defaultSettings
        }
        if (existing.apiKey.isBlank() && buildKey.isNotBlank()) {
            val updated = existing.copy(apiKey = buildKey)
            userSettingsDao.insertOrUpdate(updated)
            return updated
        }
        return existing
    }

    suspend fun saveSettings(apiKey: String, model: String) {
        val current = getSettingsSync()
        userSettingsDao.insertOrUpdate(
            current.copy(
                apiKey = apiKey.trim(),
                selectedModel = model.trim()
            )
        )
    }

    suspend fun saveApiKey(apiKey: String) {
        val current = getSettingsSync()
        userSettingsDao.insertOrUpdate(current.copy(apiKey = apiKey.trim()))
    }

    suspend fun saveModel(model: String) {
        val current = getSettingsSync()
        userSettingsDao.insertOrUpdate(current.copy(selectedModel = model.trim()))
    }

    suspend fun saveLanguage(language: AppLanguage) {
        val current = getSettingsSync()
        userSettingsDao.insertOrUpdate(current.copy(preferredLanguage = language))
    }
}
```

#### Line-by-Line Breakdown:
- **Line 14 (`settingsFlow`):** Live Flow emitting user preferences.
- **Lines 16–30 (`getSettingsSync()`):** Ensures a singleton record always exists. If the database has no key but `BuildConfig.GEMINI_API_KEY` was supplied at build time, it auto-binds the build key.
- **Lines 32–40 (`saveSettings`):** **Critical atomic save method**. Prevents race conditions where saving the API key and model concurrently could cause one coroutine to overwrite the other with an empty string.
- **Lines 42–55:** Granular update functions for `apiKey`, `model`, and `language`.

---

### 8.5 `DndRepository.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/repository/DndRepository.kt`

```kotlin
class DndRepository(
    private val api: Dnd5eApiService = RetrofitClient.dnd5eApi
) {
    // In-memory cache of monster index list
    private var cachedMonsterList: List<MonsterItemDto>? = null
    private val detailCache = mutableMapOf<String, MonsterDetailDto>()

    suspend fun getMonsterList(forceRefresh: Boolean = false): Result<List<MonsterItemDto>> =
        withContext(Dispatchers.IO) {
            try {
                if (!forceRefresh && !cachedMonsterList.isNullOrEmpty()) {
                    return@withContext Result.success(cachedMonsterList!!)
                }
                val response = api.getMonsters()
                cachedMonsterList = response.results
                Result.success(response.results)
            } catch (e: Exception) {
                if (!cachedMonsterList.isNullOrEmpty()) {
                    Result.success(cachedMonsterList!!)
                } else {
                    Result.failure(e)
                }
            }
        }

    suspend fun getMonsterDetail(index: String): Result<MonsterDetailDto> =
        withContext(Dispatchers.IO) {
            try {
                detailCache[index]?.let { return@withContext Result.success(it) }
                val detail = api.getMonsterDetail(index)
                detailCache[index] = detail
                Result.success(detail)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun filterMonsters(query: String): List<MonsterItemDto> =
        withContext(Dispatchers.Default) {
            val list = cachedMonsterList ?: getMonsterList().getOrNull() ?: emptyList()
            if (query.isBlank()) {
                list
            } else {
                list.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
}
```

#### Line-by-Line Breakdown:
- **Lines 18–19 (Two-Tier In-Memory Cache):**
  - `cachedMonsterList`: Caches 300+ monster summaries in RAM after first fetch.
  - `detailCache`: Caches full monster statblocks by index key to prevent redundant network calls.
- **Lines 21–38 (`getMonsterList`):** Runs on `Dispatchers.IO`. Checks cache first; if network fails but cache exists, falls back gracefully without showing an error to the user.
- **Lines 40–50 (`getMonsterDetail`):** Fetches statblock on `Dispatchers.IO`, caching result in `detailCache`.
- **Lines 52–60 (`filterMonsters`):** Executes in-memory case-insensitive search filtering on `Dispatchers.Default` (CPU pool), keeping UI scrolling smooth.

---

### 8.6 `AiRepository.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/data/repository/AiRepository.kt`

This is the intellectual brain of the application.

#### 1. Constants & Model Initialization (Lines 23–45):
- `DEFAULT_MODEL = "gemini-3.8-flash"`.
- `getGenerativeModel()`: Fetches user API key from `UserSettingsRepository`, validates it is non-blank, and instantiates Google's `GenerativeModel`.

#### 2. Multi-Stage Fallback & 503 Mitigation Engine (Lines 47–136):
```kotlin
private suspend fun generateContentWithFallback(prompt: String, language: AppLanguage): String {
    val (primaryModel, primaryModelName) = getGenerativeModel()
    // Stage 1: Try user's selected model
    try {
        val response = primaryModel.generateContent(prompt)
        if (!response.text.isNullOrBlank()) return response.text!!
    } catch (e: Exception) {
        if (!isHighDemandOrServerError(e)) throw formatGenericException(e, language)
    }

    // Stage 2: 1-second pause & retry primary model
    kotlinx.coroutines.delay(1000L)
    try {
        val response = primaryModel.generateContent(prompt)
        if (!response.text.isNullOrBlank()) return response.text!!
    } catch (_: Exception) {}

    // Stage 3: Automatic Fallback Chain across high-throughput models
    val fallbackCandidates = listOf(
        "gemini-3.8-flash",
        "gemini-2.0-flash-lite",
        "gemini-2.0-flash",
        "gemini-1.5-flash",
        "gemini-1.5-flash-8b"
    ).filter { it != primaryModelName }

    for (fallbackName in fallbackCandidates) {
        try {
            val (fallbackModel, _) = getGenerativeModel(fallbackName)
            val response = fallbackModel.generateContent(prompt)
            if (!response.text.isNullOrBlank()) return response.text!!
        } catch (_: Exception) {}
    }
    throw createFriendlyHighDemandException(primaryModelName, language)
}
```
- **Why this exists:** Free-tier Gemini keys often hit HTTP 503 (High Demand), 429 (Resource Exhausted), or `MissingFieldException` on overloaded endpoints. This engine automatically navigates through low-latency flash models to recover without crashing or annoying the user.

#### 3. Prompt Construction & Delimiter Separation (Lines 141–344):
- **`analyzeSessionDraft(...)`:** Injects campaign realm, party level, prior session recaps, and upcoming draft. Enforces delimiter tags:
  ```
  <<<PSYCHOLOGY_START>>>
  [Tension vs Relief, Bartle Archetypes, Agency Stress-Test]
  <<<PSYCHOLOGY_END>>>
  <<<GAME_THEORY_START>>>
  [Dominant Strategy Check, Zero-Sum Stakes, Nash Equilibrium]
  <<<GAME_THEORY_END>>>
  ```
- **`analyzeBoss(...)`:** Analyzes Minimax tactics, Non-TPK win conditions (ritual completion, grim compromises), action economy, shadow-self, and hubris.
- **`analyzeNpc(...)`:** Analyzes Tit-for-Tat dynamics, asymmetric withheld information, emotional leverage, and quid-pro-quo boundaries.
- **`extractSection(...)`:** Substring index parsing extracting content between tags reliably without brittle JSON parsing errors.

---

# 9. UI Layer — Design System, Theme & Components

### 9.1 `Color.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/theme/Color.kt`

Defines the bespoke **Medieval Grimoire & Ancient Tome Palette**:
- **Crimson Accents:** `DeepCrimson` (`#780016`), `CrimsonBright` (`#A31D31`), `CrimsonContainer` (`#380811`).
- **Arcane Gold Accents:** `ArcaneGold` (`#C5A059`), `ArcaneGoldBright` (`#E2C47D`), `ArcaneGoldDark` (`#8A6E35`).
- **Dark Leather Surfaces:** `DarkLeatherDeep` (`#141210`), `DarkLeatherSurface` (`#28231E`), `DarkLeatherCard` (`#332D26`), `DarkLeatherBorder` (`#4C4339`).
- **Parchment Manuscripts:** `ParchmentLight` (`#FAF6EE`), `ParchmentAged` (`#F1E7D0`), `AncientInk` (`#1A1815`).
- **Rune Accents:** `RuneSuccessGreen` (`#4E8D52`), `RuneErrorBlood` (`#B3261E`), `RuneArcaneCyan` (`#4AA3BA`).

---

### 9.2 `Type.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/theme/Type.kt`

Configures the **Grimoire Typography System**:
- `displayLarge`, `headlineLarge`, `headlineMedium`, `titleLarge`, `titleMedium`: Styled with `FontFamily.Serif` for medieval grimoire immersion.
- `bodyLarge`, `bodyMedium`, `bodySmall`: Styled with `FontFamily.Default` for clean readability when reading long AI analytical feedback cards.

---

### 9.3 `Theme.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/theme/Theme.kt`

- Constructs `DarkGrimoireColorScheme` (Dark Theme) and `LightParchmentColorScheme` (Light Theme).
- Applies `WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars` to ensure status bar text and icons match theme contrast.
- Provides `MaterialTheme(colorScheme, typography, content)`.

---

### 9.4 `GrimoireComponents.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/components/GrimoireComponents.kt`

Reusable UI primitives styled for the grimoire aesthetic:
- **`GrimoireCard`:** Elevated Material3 Card with 12.dp rounded corners, dark leather background, and gold/crimson border stroke.
- **`GrimoireButton`:** Primary gold-styled action button with loading spinner (`CircularProgressIndicator`) and optional crimson mode (`isCrimson = true`).
- **`GrimoireOutlinedButton`:** Secondary button with 1.dp gold border and serif typography.
- **`RuneBadge`:** Compact stat badge displaying labels and values (e.g., `CR: 17`, `AC: 19`, `HP: 256`) with custom accent colors.
- **`GrimoireTextField`:** Custom OutlinedTextField styled with gold focused borders, serif labels, and deep leather text container background.

---

### 9.5 `LocaleHelper.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/util/LocaleHelper.kt`

- `getLocaleForLanguage(language)`: Maps `AppLanguage.TH` $\rightarrow$ `Locale("th")` and `AppLanguage.EN` $\rightarrow$ `Locale.ENGLISH`.
- `applyLocale(context, language)`: Creates an updated `Configuration` with the specified locale for configuration contexts.

---

# 10. UI Layer — Navigation, ViewModels & Screens

### 10.1 `ArbiterNavigation.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/navigation/ArbiterNavigation.kt`

#### Line-by-Line Breakdown:
- **Lines 48–53 (`sealed class Screen`):**
  - `Campaigns` (`"campaigns"`)
  - `SessionArchitect` (`"session_architect"`)
  - `DramatisPersonae` (`"dramatis_personae"`)
  - `GrandCompendium` (`"grand_compendium"`)
- **Lines 63–90 (`ArbiterMainScreen`):**
  - Initializes `rememberNavController()`.
  - Instantiates the 4 ViewModels via custom ViewModel Factories injecting repositories from `ArbiterApplication`.
- **Lines 95–133 (`bottomBar`):** Renders Material3 `NavigationBar` with leather background and golden indicators. Navigates using `popUpTo(findStartDestination().id) { saveState = true }` and `launchSingleTop = true` to preserve backstack state.
- **Lines 135–185 (`NavHost`):** Sets up composable destinations and deep linking:
  - Route `"dramatis_personae?monsterIndex={monsterIndex}"`: Allows passing an attached monster slug from the Grand Compendium directly into Dramatis Personae.

---

### 10.2 `CampaignViewModel.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/viewmodel/CampaignViewModel.kt`

- **`campaigns: StateFlow<List<CampaignEntity>>`:** Exposes reactive list of all campaigns.
- **`userSettings: StateFlow<UserSettingsEntity?>`:** Exposes active language and API key.
- **`campaignStats: StateFlow<Map<Long, CampaignStats>>`:** Tracks live counts of sessions, bosses, and NPCs for each campaign card.
- **`createCampaign(title, description, partyLevel)`:** Inserts campaign into Room DB.
- **`deleteCampaign(id)`:** Purges campaign and all cascade-related data.
- **`saveSettings(key, model)`:** Updates user API key and model atomically.
- **`toggleLanguage()`:** Flips language between English and Thai.

---

### 10.3 `CampaignScreen.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/screens/CampaignScreen.kt`

- **Header Controls:** Displays title, subtitle, Gemini API Key dialog button (`Icons.Default.Key`), and Language switcher button (`Icons.Default.Language`).
- **Key Status Banner:** Prominently displays whether the Oracle Key is bound or missing.
- **Campaign Cards (`CampaignItemCard`):** Displays Realm title, lore description, party level badge, and real-time statistics (sessions, bosses, NPCs).
- **Navigation Launchers:** Two action buttons per card: "Session Architect" and "Dramatis Personae".
- **Dialogs:**
  - `NewCampaignDialog`: Sliders for party level (1–20) and text fields for title and world lore.
  - `ApiKeyDialog`: Allows pasting Gemini API key, selecting model via `LazyRow` chip selectors (`gemini-3.8-flash`, `gemini-2.0-flash`, `gemini-1.5-flash`), or typing a custom model.
  - Delete confirmation dialog.

---

### 10.4 `SessionArchitectViewModel.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/viewmodel/SessionArchitectViewModel.kt`

- **`selectCampaign(id)`:** Loads recent session echoes (limit 3), all campaign sessions, and computes next session number.
- **`consultOracle()`:** Collects past echoes as context, invokes `aiRepository.analyzeSessionDraft(...)`, and streams result into `aiPacingFeedback` and `aiDilemmaFeedback`.
- **`saveSession()`:** Commits the draft, past recap, and AI feedback into Room database as a sealed session.
- **`loadSessionForEdit(session)`:** Populates draft editor with previously saved session data for editing.

---

### 10.5 `SessionArchitectScreen.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/screens/SessionArchitectScreen.kt`

Four comprehensive sections:
1. **Realm Selector & Status Banners:** Dropdown menu selecting active campaign, plus error/success banners.
2. **Echoes of the Past:** Collapsible accordion displaying summaries of previous sessions to give the DM narrative context.
3. **Session Drafting Studio:** Large text areas for past event recap and upcoming encounter outlines, accompanied by the primary "Consult the Oracle" button.
4. **AI Feedback Showcase & Save:**
   - Gold-bordered card for **Player Psychology & Dramatic Pacing** (Tension/relief cycles, Bartle archetypes, agency checks).
   - Crimson-bordered card for **Game Theory & Choice Architecture** (Nash equilibria, dominant strategies, dilemma matrices).
   - "Seal into Tome" button persisting session to Room DB.

---

### 10.6 `DramatisPersonaeViewModel.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/viewmodel/DramatisPersonaeViewModel.kt`

- Manages character profiles (`BOSS` vs `NPC`).
- `attachMonster(index)`: Queries `DndRepository` for monster stats and links it to character draft.
- `detachMonster()`: Unlinks attached statblock.
- `consultAi()`: Dispatches to `aiRepository.analyzeBoss` (Minimax & win conditions) or `aiRepository.analyzeNpc` (Tit-for-Tat & quid-pro-quo) based on active tab.
- `saveCharacter()`: Commits character entity to Room DB.
- `loadCharacterForEdit(character)`: Re-loads character and attached monster for editing.

---

### 10.7 `DramatisPersonaeScreen.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/screens/DramatisPersonaeScreen.kt`

- **Role Switcher Tab (`PrimaryTabRow`):** Smooth toggle between **Dread Boss** (Crimson theme) and **Notable NPC** (Gold theme).
- **Statblock Linker:** Allows attaching D&D 5e SRD monsters (e.g. Lich, Adult Red Dragon, Mind Flayer, Gladiator) displaying live CR, AC, and HP badges.
- **Role-Specific AI Analysis:**
  - Boss Mode: Calls `Analyze Minimax & Win Conditions`.
  - NPC Mode: Calls `Analyze Tit-for-Tat & Quid-Pro-Quo`.
- **Feedback Cards:** Renders tactical and psychological deconstructions.
- **Inscribed Personages List:** Displays all recorded characters in the campaign with quick edit and delete actions.

---

### 10.8 `CompendiumViewModel.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/viewmodel/CompendiumViewModel.kt`

- `loadMonsters()`: Fetches entire 300+ monster index from D&D 5e SRD REST API.
- Debounced Search Pipeline:
  ```kotlin
  _searchQuery
      .debounce(300)
      .distinctUntilChanged()
      .collect { query -> filterMonsters(query) }
  ```
  Waits 300ms after user finishes typing before executing in-memory search, preventing UI jank.
- `selectMonster(index)`: Fetches full statblock details for modal view.
- `clearSelectedMonster()`: Clears detail view to return to search list.

---

### 10.9 `CompendiumScreen.kt`
**File Location:** `/Final_project/app/src/main/java/com/example/final_project/ui/screens/CompendiumScreen.kt`

- **Live Debounced Search Bar:** Search creatures by name with instant clear button and loading indicator.
- **Monster List:** Scrollable list displaying creature names with chevron arrows.
- **Monster Detail View Modal (`MonsterDetailView`):**
  - Header: Name, size, creature type, and alignment.
  - Stat Badges: CR badge (Crimson), AC badge (Gold), HP badge (Green).
  - Special Traits Section: Displays abilities like Magic Resistance or Innate Spellcasting.
  - Actions Section: Displays multiattacks, weapon strikes, and legendary actions.
  - **"Export to Dramatis Personae" Action:** One-tap action routing directly to Dramatis Personae screen with this monster pre-attached!

---

# 11. Testing Suite

### 11.1 `ExampleUnitTest.kt`
**File Location:** `/Final_project/app/src/test/java/com/example/final_project/ExampleUnitTest.kt`

Local JVM unit test executing on host machine via JUnit 4:
- Verifies basic local unit testing environment correctness (`assertEquals(4, 2 + 2)`).

### 11.2 `ExampleInstrumentedTest.kt`
**File Location:** `/Final_project/app/src/androidTest/java/com/example/final_project/ExampleInstrumentedTest.kt`

On-device Android instrumented test:
- Uses `InstrumentationRegistry.getInstrumentation().targetContext`.
- Verifies application context package name matches `"com.example.final_project"`.

---

# 12. Summary Checklist of Every File

| # | File Path | Category | Core Functionality |
| :--- | :--- | :--- | :--- |
| 1 | `build.gradle.kts` | Root Build | Configures plugins (Android, Compose, KSP) |
| 2 | `settings.gradle.kts` | Root Build | Plugin management, repositories, module declaration |
| 3 | `gradle.properties` | Root Build | JVM memory (2GB), configuration caching, UTF-8 |
| 4 | `gradle/libs.versions.toml` | Root Build | Central Version Catalog for dependencies |
| 5 | `gradle/wrapper/gradle-wrapper.properties` | Root Build | Gradle 8.13 wrapper specification |
| 6 | `local.properties` | Root Config | SDK path and local Gemini developer key |
| 7 | `.gitignore` | Root Config | Excludes build directories, IDE files, and secrets |
| 8 | `app/build.gradle.kts` | App Config | SDK 37, Compose BOM, Room, Retrofit, Gemini SDK |
| 9 | `app/src/main/AndroidManifest.xml` | App Config | Internet permissions, ArbiterApplication, MainActivity |
| 10 | `app/src/main/keepRules/rules.keep` | App Config | ProGuard/R8 optimization rules |
| 11 | `res/values/strings.xml` | Resources | English string tokens and prompt headers |
| 12 | `res/values-th/strings.xml` | Resources | Complete Thai localized strings |
| 13 | `res/values/colors.xml` | Resources | Base XML color tokens |
| 14 | `res/values/themes.xml` | Resources | Android theme definition |
| 15 | `res/xml/backup_rules.xml` | Resources | Data backup definitions |
| 16 | `res/xml/data_extraction_rules.xml` | Resources | Android 12+ cloud backup rules |
| 17 | `ArbiterApplication.kt` | App Core | Application singleton container (Dependency Injection) |
| 18 | `MainActivity.kt` | App Core | Edge-to-edge Compose entry & dynamic Locale provider |
| 19 | `AppDatabase.kt` | Local DB | Room DB v2, SQLite singleton, DAOs registration |
| 20 | `Converters.kt` | Local DB | Type converters for `CharacterType` and `AppLanguage` |
| 21 | `CampaignEntity.kt` | Local DB | Entity model for campaign realms |
| 22 | `SessionLogEntity.kt` | Local DB | Entity model for session notes and AI feedback |
| 23 | `CharacterEntity.kt` | Local DB | Entity model for Dread Bosses and Notable NPCs |
| 24 | `UserSettingsEntity.kt` | Local DB | Entity model for singleton settings row (Key, Language) |
| 25 | `CampaignDao.kt` | Local DB | CRUD operations and live realm statistics queries |
| 26 | `SessionLogDao.kt` | Local DB | CRUD operations for sessions and next-session indexing |
| 27 | `CharacterDao.kt` | Local DB | CRUD operations for characters filtered by realm/type |
| 28 | `UserSettingsDao.kt` | Local DB | Atomic settings read/write operations |
| 29 | `RetrofitClient.kt` | Network | Retrofit 2 + OkHttp client pointing to D&D 5e SRD |
| 30 | `Dnd5eApiService.kt` | Network | Retrofit interface for `/monsters` endpoints |
| 31 | `MonsterListDto.kt` | Network | DTO models for bestiary index listing |
| 32 | `MonsterDetailDto.kt` | Network | DTO models for full monster statblock details |
| 33 | `AiModels.kt` | Network/AI | Clean data classes for parsed AI analysis outputs |
| 34 | `CampaignRepository.kt` | Repository | Exposes campaign streams and real-time statistics |
| 35 | `SessionRepository.kt` | Repository | Exposes session drafts and prior session echoes |
| 36 | `CharacterRepository.kt` | Repository | Exposes character persistence and statblock links |
| 37 | `UserSettingsRepository.kt` | Repository | Manages atomic settings persistence and model config |
| 38 | `DndRepository.kt` | Repository | Caches monster index & details, executes searches |
| 39 | `AiRepository.kt` | Repository | Gemini AI engine, fallback chain & delimiter parsing |
| 40 | `Color.kt` | UI Theme | Medieval Grimoire color palette |
| 41 | `Type.kt` | UI Theme | Serif titles and high-legibility body typography |
| 42 | `Theme.kt` | UI Theme | Dark Grimoire and Light Parchment Material3 themes |
| 43 | `GrimoireComponents.kt` | UI Components| Reusable Grimoire Cards, Buttons, Badges, Fields |
| 44 | `LocaleHelper.kt` | UI Utility | Dynamically applies English or Thai runtime locales |
| 45 | `ArbiterNavigation.kt` | Navigation | Navigation graph and bottom navigation bar |
| 46 | `CampaignViewModel.kt` | ViewModel | State management for campaigns, settings, and stats |
| 47 | `CampaignScreen.kt` | Screen | Realm overview, API key config, model picker |
| 48 | `SessionArchitectViewModel.kt` | ViewModel | State management for session drafting and AI sparring |
| 49 | `SessionArchitectScreen.kt` | Screen | Past echoes, drafting studio, psychology & game theory |
| 50 | `DramatisPersonaeViewModel.kt` | ViewModel | State management for Boss/NPC analysis & stats |
| 51 | `DramatisPersonaeScreen.kt` | Screen | Role switcher, SRD linker, Minimax/Tit-for-Tat analysis |
| 52 | `CompendiumViewModel.kt` | ViewModel | Debounced search and monster detail fetching |
| 53 | `CompendiumScreen.kt` | Screen | Live SRD creature search, statblock viewer, exporter |
| 54 | `ExampleUnitTest.kt` | Tests | Local JVM unit test |
| 55 | `ExampleInstrumentedTest.kt` | Tests | On-device Android instrumented test |

---
*Created for the Arbiter TTRPG Dungeon Master Companion System.*

package com.example.final_project

import android.app.Application
import com.example.final_project.data.local.AppDatabase
import com.example.final_project.data.repository.CampaignRepository
import com.example.final_project.data.repository.CharacterRepository
import com.example.final_project.data.repository.DndRepository
import com.example.final_project.data.repository.SessionRepository
import com.example.final_project.data.repository.UserSettingsRepository

/**
 * Application class for the Arbiter, serving as the dependency container
 * providing repositories and database singletons throughout the app.
 */
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

package com.example.final_project.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.local.entity.AppLanguage
import com.example.final_project.data.local.entity.CampaignEntity
import com.example.final_project.data.local.entity.UserSettingsEntity
import com.example.final_project.data.repository.CampaignRepository
import com.example.final_project.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CampaignStats(
    val sessionCount: Int = 0,
    val bossCount: Int = 0,
    val npcCount: Int = 0
)

class CampaignViewModel(
    private val campaignRepository: CampaignRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val campaigns: StateFlow<List<CampaignEntity>> =
        campaignRepository.allCampaigns
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userSettings: StateFlow<UserSettingsEntity?> =
        userSettingsRepository.settingsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _campaignStats = MutableStateFlow<Map<Long, CampaignStats>>(emptyMap())
    val campaignStats: StateFlow<Map<Long, CampaignStats>> = _campaignStats.asStateFlow()

    init {
        // Observe campaigns and load statistics for each
        viewModelScope.launch {
            campaigns.collect { list ->
                list.forEach { c ->
                    observeStatsForCampaign(c.id)
                }
            }
        }
    }

    private fun observeStatsForCampaign(campaignId: Long) {
        viewModelScope.launch {
            campaignRepository.getSessionCount(campaignId).collect { sessions ->
                _campaignStats.value = _campaignStats.value.toMutableMap().apply {
                    val current = this[campaignId] ?: CampaignStats()
                    put(campaignId, current.copy(sessionCount = sessions))
                }
            }
        }
        viewModelScope.launch {
            campaignRepository.getBossCount(campaignId).collect { bosses ->
                _campaignStats.value = _campaignStats.value.toMutableMap().apply {
                    val current = this[campaignId] ?: CampaignStats()
                    put(campaignId, current.copy(bossCount = bosses))
                }
            }
        }
        viewModelScope.launch {
            campaignRepository.getNpcCount(campaignId).collect { npcs ->
                _campaignStats.value = _campaignStats.value.toMutableMap().apply {
                    val current = this[campaignId] ?: CampaignStats()
                    put(campaignId, current.copy(npcCount = npcs))
                }
            }
        }
    }

    fun createCampaign(title: String, description: String, partyLevel: Int) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val entity = CampaignEntity(
                title = title.trim(),
                description = description.trim(),
                partyLevel = partyLevel.coerceIn(1, 20)
            )
            campaignRepository.insertCampaign(entity)
        }
    }

    fun deleteCampaign(id: Long) {
        viewModelScope.launch {
            campaignRepository.deleteCampaignById(id)
        }
    }

    fun saveSettings(key: String, model: String) {
        viewModelScope.launch {
            userSettingsRepository.saveSettings(key, model)
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            userSettingsRepository.saveApiKey(key)
        }
    }

    fun saveModel(model: String) {
        viewModelScope.launch {
            userSettingsRepository.saveModel(model)
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val current = userSettings.value?.preferredLanguage ?: AppLanguage.EN
            val next = if (current == AppLanguage.EN) AppLanguage.TH else AppLanguage.EN
            userSettingsRepository.saveLanguage(next)
        }
    }

    class Factory(
        private val campaignRepository: CampaignRepository,
        private val userSettingsRepository: UserSettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CampaignViewModel(campaignRepository, userSettingsRepository) as T
        }
    }
}

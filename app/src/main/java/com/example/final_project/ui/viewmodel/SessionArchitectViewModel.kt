package com.example.final_project.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.local.entity.AppLanguage
import com.example.final_project.data.local.entity.CampaignEntity
import com.example.final_project.data.local.entity.SessionLogEntity
import com.example.final_project.data.repository.AiRepository
import com.example.final_project.data.repository.CampaignRepository
import com.example.final_project.data.repository.SessionRepository
import com.example.final_project.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionArchitectViewModel(
    private val campaignRepository: CampaignRepository,
    private val sessionRepository: SessionRepository,
    private val aiRepository: AiRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val campaigns: StateFlow<List<CampaignEntity>> =
        campaignRepository.allCampaigns
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCampaignId = MutableStateFlow<Long?>(null)
    val selectedCampaignId: StateFlow<Long?> = _selectedCampaignId.asStateFlow()

    private val _pastEchoes = MutableStateFlow<List<SessionLogEntity>>(emptyList())
    val pastEchoes: StateFlow<List<SessionLogEntity>> = _pastEchoes.asStateFlow()

    private val _allSessions = MutableStateFlow<List<SessionLogEntity>>(emptyList())
    val allSessions: StateFlow<List<SessionLogEntity>> = _allSessions.asStateFlow()

    // Drafting State
    private val _currentDraft = MutableStateFlow("")
    val currentDraft: StateFlow<String> = _currentDraft.asStateFlow()

    private val _pastEventsSummary = MutableStateFlow("")
    val pastEventsSummary: StateFlow<String> = _pastEventsSummary.asStateFlow()

    private val _sessionNumber = MutableStateFlow(1)
    val sessionNumber: StateFlow<Int> = _sessionNumber.asStateFlow()

    private val _editingSessionId = MutableStateFlow<Long?>(null)
    val editingSessionId: StateFlow<Long?> = _editingSessionId.asStateFlow()

    private val _isConsultingOracle = MutableStateFlow(false)
    val isConsultingOracle: StateFlow<Boolean> = _isConsultingOracle.asStateFlow()

    private val _aiPacingFeedback = MutableStateFlow("")
    val aiPacingFeedback: StateFlow<String> = _aiPacingFeedback.asStateFlow()

    private val _aiDilemmaFeedback = MutableStateFlow("")
    val aiDilemmaFeedback: StateFlow<String> = _aiDilemmaFeedback.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        viewModelScope.launch {
            campaigns.collect { list ->
                if (_selectedCampaignId.value == null && list.isNotEmpty()) {
                    selectCampaign(list.first().id)
                }
            }
        }
    }

    fun selectCampaign(id: Long) {
        _selectedCampaignId.value = id
        _editingSessionId.value = null
        _currentDraft.value = ""
        _pastEventsSummary.value = ""
        _aiPacingFeedback.value = ""
        _aiDilemmaFeedback.value = ""
        _errorMessage.value = null
        _successMessage.value = null

        // Collect past echoes (limit 3)
        viewModelScope.launch {
            sessionRepository.getLatestSessions(id, limit = 3).collect { echoes ->
                _pastEchoes.value = echoes
            }
        }
        // Collect all sessions
        viewModelScope.launch {
            sessionRepository.getSessionsForCampaign(id).collect { sessions ->
                _allSessions.value = sessions
            }
        }
        // Compute next session number
        viewModelScope.launch {
            _sessionNumber.value = sessionRepository.getNextSessionNumber(id)
        }
    }

    fun onDraftChange(draft: String) {
        _currentDraft.value = draft
    }

    fun onPastEventsChange(summary: String) {
        _pastEventsSummary.value = summary
    }

    fun consultOracle() {
        val campId = _selectedCampaignId.value ?: return
        val draft = _currentDraft.value.trim()
        if (draft.isBlank()) {
            _errorMessage.value = "Draft cannot be empty when consulting the Oracle."
            return
        }

        viewModelScope.launch {
            _isConsultingOracle.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                val campaign = campaignRepository.getCampaignByIdSync(campId)
                val settings = userSettingsRepository.getSettingsSync()
                val echoes = _pastEchoes.value.mapNotNull { it.currentDraft.ifBlank { it.pastEventsSummary } }

                val result = aiRepository.analyzeSessionDraft(
                    campaignTitle = campaign?.title ?: "Unknown Realm",
                    partyLevel = campaign?.partyLevel ?: 1,
                    pastRecaps = echoes,
                    currentDraft = draft,
                    language = settings.preferredLanguage
                )

                result.onSuccess { analysis ->
                    _aiPacingFeedback.value = analysis.pacingFeedback
                    _aiDilemmaFeedback.value = analysis.dilemmaFeedback
                }.onFailure { ex ->
                    _errorMessage.value = ex.message ?: "Failed to channel the Oracle."
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An ominous disruption occurred."
            } finally {
                _isConsultingOracle.value = false
            }
        }
    }

    fun saveSession() {
        val campId = _selectedCampaignId.value ?: return
        val draft = _currentDraft.value.trim()
        if (draft.isBlank()) {
            _errorMessage.value = "Draft cannot be empty."
            return
        }

        viewModelScope.launch {
            try {
                val entity = SessionLogEntity(
                    id = _editingSessionId.value ?: 0L,
                    campaignId = campId,
                    sessionNumber = _sessionNumber.value,
                    pastEventsSummary = _pastEventsSummary.value.trim(),
                    currentDraft = draft,
                    aiPacingFeedback = _aiPacingFeedback.value,
                    aiDilemmaFeedback = _aiDilemmaFeedback.value
                )
                sessionRepository.saveSession(entity)
                _successMessage.value = "Session chronicle sealed successfully."
                // Reset draft for next session if not editing
                if (_editingSessionId.value == null) {
                    _currentDraft.value = ""
                    _pastEventsSummary.value = ""
                    _aiPacingFeedback.value = ""
                    _aiDilemmaFeedback.value = ""
                    _sessionNumber.value = sessionRepository.getNextSessionNumber(campId)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to seal session."
            }
        }
    }

    fun loadSessionForEdit(session: SessionLogEntity) {
        _editingSessionId.value = session.id
        _sessionNumber.value = session.sessionNumber
        _pastEventsSummary.value = session.pastEventsSummary
        _currentDraft.value = session.currentDraft
        _aiPacingFeedback.value = session.aiPacingFeedback
        _aiDilemmaFeedback.value = session.aiDilemmaFeedback
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun clearFeedback() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    class Factory(
        private val campaignRepository: CampaignRepository,
        private val sessionRepository: SessionRepository,
        private val aiRepository: AiRepository,
        private val userSettingsRepository: UserSettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SessionArchitectViewModel(
                campaignRepository,
                sessionRepository,
                aiRepository,
                userSettingsRepository
            ) as T
        }
    }
}

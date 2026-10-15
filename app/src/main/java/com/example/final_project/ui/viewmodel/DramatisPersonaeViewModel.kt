package com.example.final_project.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.local.entity.CampaignEntity
import com.example.final_project.data.local.entity.CharacterEntity
import com.example.final_project.data.local.entity.CharacterType
import com.example.final_project.data.remote.dto.MonsterDetailDto
import com.example.final_project.data.repository.AiRepository
import com.example.final_project.data.repository.CampaignRepository
import com.example.final_project.data.repository.CharacterRepository
import com.example.final_project.data.repository.DndRepository
import com.example.final_project.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DramatisPersonaeViewModel(
    private val campaignRepository: CampaignRepository,
    private val characterRepository: CharacterRepository,
    private val dndRepository: DndRepository,
    private val aiRepository: AiRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val campaigns: StateFlow<List<CampaignEntity>> =
        campaignRepository.allCampaigns
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCampaignId = MutableStateFlow<Long?>(null)
    val selectedCampaignId: StateFlow<Long?> = _selectedCampaignId.asStateFlow()

    private val _characters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters.asStateFlow()

    // Form State
    private val _characterType = MutableStateFlow(CharacterType.BOSS)
    val characterType: StateFlow<CharacterType> = _characterType.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _backstory = MutableStateFlow("")
    val backstory: StateFlow<String> = _backstory.asStateFlow()

    private val _attachedMonsterIndex = MutableStateFlow<String?>(null)
    val attachedMonsterIndex: StateFlow<String?> = _attachedMonsterIndex.asStateFlow()

    private val _attachedMonsterDetail = MutableStateFlow<MonsterDetailDto?>(null)
    val attachedMonsterDetail: StateFlow<MonsterDetailDto?> = _attachedMonsterDetail.asStateFlow()

    private val _editingCharacterId = MutableStateFlow<Long?>(null)
    val editingCharacterId: StateFlow<Long?> = _editingCharacterId.asStateFlow()

    // AI Analysis State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _gameTheoryFeedback = MutableStateFlow("")
    val gameTheoryFeedback: StateFlow<String> = _gameTheoryFeedback.asStateFlow()

    private val _psychologyFeedback = MutableStateFlow("")
    val psychologyFeedback: StateFlow<String> = _psychologyFeedback.asStateFlow()

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
        viewModelScope.launch {
            characterRepository.getCharactersForCampaign(id).collect { list ->
                _characters.value = list
            }
        }
    }

    fun setCharacterType(type: CharacterType) {
        _characterType.value = type
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onBackstoryChange(backstory: String) {
        _backstory.value = backstory
    }

    fun attachMonster(index: String) {
        _attachedMonsterIndex.value = index
        viewModelScope.launch {
            val result = dndRepository.getMonsterDetail(index)
            result.onSuccess { detail ->
                _attachedMonsterDetail.value = detail
                // If name is blank, default to monster name
                if (_name.value.isBlank()) {
                    _name.value = detail.name
                }
            }
        }
    }

    fun detachMonster() {
        _attachedMonsterIndex.value = null
        _attachedMonsterDetail.value = null
    }

    fun consultAi() {
        val campId = _selectedCampaignId.value ?: return
        val entityName = _name.value.trim()
        val backstoryText = _backstory.value.trim()

        if (entityName.isBlank() || backstoryText.isBlank()) {
            _errorMessage.value = "Both Name and Backstory/Motivations are required for AI analysis."
            return
        }

        viewModelScope.launch {
            _isAnalyzing.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                val campaign = campaignRepository.getCampaignByIdSync(campId)
                val settings = userSettingsRepository.getSettingsSync()
                val srdSummary = _attachedMonsterDetail.value?.let { m ->
                    "Monster: ${m.name} | CR: ${m.displayChallengeRating} | AC: ${m.displayArmorClass} | HP: ${m.hitPoints ?: 0}\nActions: ${m.actions?.joinToString { it.name } ?: "None"}"
                }

                if (_characterType.value == CharacterType.BOSS) {
                    val res = aiRepository.analyzeBoss(
                        campaignTitle = campaign?.title ?: "Unknown Realm",
                        partyLevel = campaign?.partyLevel ?: 1,
                        bossName = entityName,
                        backstory = backstoryText,
                        srdMonsterInfo = srdSummary,
                        language = settings.preferredLanguage
                    )
                    res.onSuccess { analysis ->
                        _gameTheoryFeedback.value = analysis.gameTheoryFeedback
                        _psychologyFeedback.value = analysis.psychologyFeedback
                    }.onFailure { ex ->
                        _errorMessage.value = ex.message ?: "Boss analysis failed."
                    }
                } else {
                    val res = aiRepository.analyzeNpc(
                        campaignTitle = campaign?.title ?: "Unknown Realm",
                        partyLevel = campaign?.partyLevel ?: 1,
                        npcName = entityName,
                        backstory = backstoryText,
                        srdMonsterInfo = srdSummary,
                        language = settings.preferredLanguage
                    )
                    res.onSuccess { analysis ->
                        _gameTheoryFeedback.value = analysis.gameTheoryFeedback
                        _psychologyFeedback.value = analysis.psychologyFeedback
                    }.onFailure { ex ->
                        _errorMessage.value = ex.message ?: "NPC analysis failed."
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An ominous error occurred during AI analysis."
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun saveCharacter() {
        val campId = _selectedCampaignId.value ?: return
        val entityName = _name.value.trim()
        val backstoryText = _backstory.value.trim()

        if (entityName.isBlank()) {
            _errorMessage.value = "Entity name cannot be blank."
            return
        }

        viewModelScope.launch {
            try {
                val entity = CharacterEntity(
                    id = _editingCharacterId.value ?: 0L,
                    campaignId = campId,
                    name = entityName,
                    characterType = _characterType.value,
                    rawBackstoryDraft = backstoryText,
                    attachedMonsterIndex = _attachedMonsterIndex.value,
                    aiPsychologyFeedback = _psychologyFeedback.value,
                    aiGameTheoryFeedback = _gameTheoryFeedback.value
                )
                characterRepository.saveCharacter(entity)
                _successMessage.value = "Personage inscribed into the Grimoire."
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to inscribe personage."
            }
        }
    }

    fun deleteCharacter(id: Long) {
        viewModelScope.launch {
            characterRepository.deleteCharacterById(id)
        }
    }

    fun loadCharacterForEdit(character: CharacterEntity) {
        _editingCharacterId.value = character.id
        _name.value = character.name
        _characterType.value = character.characterType
        _backstory.value = character.rawBackstoryDraft
        _psychologyFeedback.value = character.aiPsychologyFeedback
        _gameTheoryFeedback.value = character.aiGameTheoryFeedback
        _attachedMonsterIndex.value = character.attachedMonsterIndex
        if (character.attachedMonsterIndex != null) {
            viewModelScope.launch {
                dndRepository.getMonsterDetail(character.attachedMonsterIndex).onSuccess {
                    _attachedMonsterDetail.value = it
                }
            }
        } else {
            _attachedMonsterDetail.value = null
        }
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun clearForm() {
        _editingCharacterId.value = null
        _name.value = ""
        _backstory.value = ""
        _attachedMonsterIndex.value = null
        _attachedMonsterDetail.value = null
        _gameTheoryFeedback.value = ""
        _psychologyFeedback.value = ""
        _errorMessage.value = null
        _successMessage.value = null
    }

    class Factory(
        private val campaignRepository: CampaignRepository,
        private val characterRepository: CharacterRepository,
        private val dndRepository: DndRepository,
        private val aiRepository: AiRepository,
        private val userSettingsRepository: UserSettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DramatisPersonaeViewModel(
                campaignRepository,
                characterRepository,
                dndRepository,
                aiRepository,
                userSettingsRepository
            ) as T
        }
    }
}

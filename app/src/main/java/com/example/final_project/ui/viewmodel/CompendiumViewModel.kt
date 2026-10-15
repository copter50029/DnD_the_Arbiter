package com.example.final_project.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.dto.MonsterDetailDto
import com.example.final_project.data.remote.dto.MonsterItemDto
import com.example.final_project.data.repository.DndRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CompendiumViewModel(
    private val dndRepository: DndRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MonsterItemDto>>(emptyList())
    val searchResults: StateFlow<List<MonsterItemDto>> = _searchResults.asStateFlow()

    private val _selectedMonsterDetail = MutableStateFlow<MonsterDetailDto?>(null)
    val selectedMonsterDetail: StateFlow<MonsterDetailDto?> = _selectedMonsterDetail.asStateFlow()

    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Initial load of monsters
        loadMonsters()

        // Debounced search observation
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    filterMonsters(query)
                }
        }
    }

    fun loadMonsters() {
        viewModelScope.launch {
            _isSearching.value = true
            _errorMessage.value = null
            val result = dndRepository.getMonsterList()
            result.onSuccess { list ->
                _searchResults.value = list
            }.onFailure { ex ->
                _errorMessage.value = ex.message ?: "Failed to channel SRD archives."
            }
            _isSearching.value = false
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private suspend fun filterMonsters(query: String) {
        _isSearching.value = true
        val filtered = dndRepository.filterMonsters(query)
        _searchResults.value = filtered
        _isSearching.value = false
    }

    fun selectMonster(index: String) {
        viewModelScope.launch {
            _isLoadingDetail.value = true
            _errorMessage.value = null
            val result = dndRepository.getMonsterDetail(index)
            result.onSuccess { detail ->
                _selectedMonsterDetail.value = detail
            }.onFailure { ex ->
                _errorMessage.value = ex.message ?: "Failed to retrieve monster detail."
            }
            _isLoadingDetail.value = false
        }
    }

    fun clearSelectedMonster() {
        _selectedMonsterDetail.value = null
    }

    class Factory(
        private val dndRepository: DndRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CompendiumViewModel(dndRepository) as T
        }
    }
}

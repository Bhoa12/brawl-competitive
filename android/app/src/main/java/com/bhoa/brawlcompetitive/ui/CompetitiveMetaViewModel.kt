package com.bhoa.brawlcompetitive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhoa.brawlcompetitive.network.AnalyticsResponse
import com.bhoa.brawlcompetitive.BuildConfig
import com.bhoa.brawlcompetitive.network.CompetitiveApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CompetitiveMetaUiState(
    val loading: Boolean = false,
    val response: AnalyticsResponse? = null,
    val error: String? = null
)

class CompetitiveMetaViewModel(
    private val api: CompetitiveApi = CompetitiveApi(BuildConfig.COMPETITIVE_API_BASE_URL)
) : ViewModel() {
    private val _state = MutableStateFlow(CompetitiveMetaUiState())
    val state: StateFlow<CompetitiveMetaUiState> = _state

    fun refresh(mapId: Int? = null, mode: String? = null, rank: String? = null, subrank: String? = null, patch: String? = null) {
        _state.value = CompetitiveMetaUiState(loading = true)
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.fetchSummary(mapId, mode, rank, subrank, patch)
                }
                _state.value = CompetitiveMetaUiState(response = result)
            } catch (error: Exception) {
                _state.value = CompetitiveMetaUiState(error = error.message ?: "No se pudo actualizar el meta")
            }
        }
    }
}

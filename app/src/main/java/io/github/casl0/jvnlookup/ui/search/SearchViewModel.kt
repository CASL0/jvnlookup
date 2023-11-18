/*
 * Copyright 2022 CASL0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.casl0.jvnlookup.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.domain.SearchVulnOverviewUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/** UI状態 */
sealed interface SearchUiState {
    /** 検索完了時のUI状態 */
    data class Loaded(
        /** 検索ボックスの入力値 */
        val searchValue: String = ""
    ) : SearchUiState

    /** 検索中のUI状態 */
    object Loading : SearchUiState
}

/**
 * 検索画面のビジネスロジックを扱うViewModel
 *
 * @param searchVulnOverviewUseCase 検索用のUseCase
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchVulnOverviewUseCase: SearchVulnOverviewUseCase
) : ViewModel() {

    /** 検索失敗時のチャネル */
    private val errorChannel = Channel<Int>()

    /** 検索失敗時のエラーイベント */
    val hasError: Flow<Int> = errorChannel.receiveAsFlow()

    /** UI状態 */
    val uiState: StateFlow<SearchUiState> get() = _uiState

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loaded())

    /**
     * キーワードで JVN を検索します
     *
     * @param keyword キーワード
     * @param onSearchComplete 検索完了後のコールバック
     */
    fun searchOnJvn(keyword: CharSequence, onSearchComplete: (() -> Unit)? = null) {
        if (keyword.isBlank()) return
        val currentUiState = _uiState.value
        if (currentUiState is SearchUiState.Loaded) {
            viewModelScope.launch {
                _uiState.value = SearchUiState.Loading
                coroutineScope {
                    val hitCount = searchVulnOverviewUseCase(keyword).getOrElse {
                        Timber.d(it)
                        errorChannel.send(R.string.error_network_connection)
                        return@coroutineScope
                    }
                    if (hitCount == 0) {
                        errorChannel.send(R.string.error_no_results_found)
                    } else {
                        onSearchComplete?.let { it() }
                    }
                }
                _uiState.value = currentUiState
            }
        }
    }

    /**
     * 検索ボックスの入力値変更時のイベントハンドラ
     *
     * @param newValue 変更後の入力値
     */
    fun onSearchValueChanged(newValue: String) {
        _uiState.update {
            if (it is SearchUiState.Loaded) {
                it.copy(searchValue = newValue)
            } else {
                it
            }
        }
    }
}

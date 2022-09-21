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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.repository.SearchRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

/**
 * 検索画面のビジネスロジックを扱うViewModel
 * @param searchRepository 検索結果のリポジトリ
 */
class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    val searchResult = searchRepository.searchResults

    /**
     * 検索ボックスの入力値
     */
    private var _searchValue by mutableStateOf("")
    val searchValue get() = _searchValue

    /**
     * 検索中の状態
     */
    private var _searchInProgress by mutableStateOf(false)
    val searchInProgress get() = _searchInProgress

    /**
     * 検索失敗時のチャネル
     */
    private val errorChannel = Channel<Int>()

    /**
     * 検索失敗時のエラーイベント
     */
    val hasError: Flow<Int> = errorChannel.receiveAsFlow()

    /**
     * キーワードで JVN を検索します
     */
    fun searchOnJvn(keyword: CharSequence) {
        if (keyword.isEmpty() || keyword.isBlank()) return
        viewModelScope.launch {
            _searchInProgress = true
            try {
                val hitCount = searchRepository.searchOnJvn(keyword)
                if (hitCount == 0) {
                    errorChannel.send(R.string.error_no_results_found)
                }
            } catch (e: Exception) {
                // ネットワークエラー
                e.localizedMessage?.let { Timber.d(it) }
                errorChannel.send(R.string.error_network_connection)
            }
            _searchInProgress = false
        }
    }

    /**
     * 検索ボックスの入力値変更時のイベントハンドラ
     */
    fun onSearchValueChanged(newValue: String) {
        _searchValue = newValue
    }

    companion object {
        /**
         * SearchViewModelのファクトリ
         */
        fun provideFactory(
            searchRepository: SearchRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(searchRepository) as T
            }
        }
    }
}
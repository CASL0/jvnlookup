/*
 * Copyright 2022 CASL0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.casl0.jvnlookup.ui.search.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.casl0.jvnlookup.domain.FavoriteVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.SearchVulnOverviewUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 検索結果画面のビジネスロジックを扱うViewModel
 * @param searchVulnOverviewUseCase 検索用のUseCase
 * @param favoriteVulnOverviewUseCase お気に入り登録用のUseCase
 */
@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val searchVulnOverviewUseCase: SearchVulnOverviewUseCase,
    private val favoriteVulnOverviewUseCase: FavoriteVulnOverviewUseCase
) : ViewModel() {
    /**
     * 検索結果
     */
    val searchResults = searchVulnOverviewUseCase.searchResults

    /**
     * お気に入り登録済みの脆弱性対策情報
     */
    val favorites = favoriteVulnOverviewUseCase.favorites

    /**
     * お気に入り登録を更新します
     * @param id 更新したいレコードのセキュリティID
     * @param favorite 更新後のお気に入り状態
     */
    fun onFavoriteButtonClicked(id: String, favorite: Boolean) {
        viewModelScope.launch {
            val searchResults = searchResults.first()

            searchResults.find { it.id == id }?.let {
                favoriteVulnOverviewUseCase(it, favorite)
            }
        }
    }
}

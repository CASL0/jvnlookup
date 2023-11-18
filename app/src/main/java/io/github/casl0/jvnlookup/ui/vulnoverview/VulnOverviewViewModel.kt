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

package io.github.casl0.jvnlookup.ui.vulnoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.domain.FavoriteVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.FetchVulnOverviewUseCase
import io.github.casl0.jvnlookup.model.Category
import io.github.casl0.jvnlookup.model.DomainCVSS
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ホーム画面のUI状態 */
data class VulnOverviewUiState(
    /** 選択されているフィルターカテゴリ */
    val selectedCategory: Category = Category.All,
    /** リフレッシュ中 */
    val isRefreshing: Boolean = false,
    /** フィルターした脆弱性概要情報一覧 */
    val filteredVulnOverviews: List<DomainVulnOverview> = listOf()
)

/**
 * ホーム画面のビジネスロジックを扱うViewModel
 *
 * @param fetchVulnOverviewUseCase 脆弱性対策情報を取得するUseCase
 * @param favoriteVulnOverviewUseCase 脆弱性対策情報をお気に入り登録するUseCase
 */
@HiltViewModel
class VulnOverviewViewModel @Inject constructor(
    private val fetchVulnOverviewUseCase: FetchVulnOverviewUseCase,
    private val favoriteVulnOverviewUseCase: FavoriteVulnOverviewUseCase,
) : ViewModel() {
    /** リフレッシュ失敗時のチャネル */
    private val errorChannel = Channel<Int>()

    /** リフレッシュ失敗時のエラーイベント */
    val hasError: Flow<Int> = errorChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(VulnOverviewUiState())

    /** UI状態 */
    val uiState: Flow<VulnOverviewUiState>
        get() = combine(
            fetchVulnOverviewUseCase.vulnOverviews,
            _uiState
        ) { vulnOverviews, uiState ->
            uiState.copy(
                filteredVulnOverviews = vulnOverviews.filterCategory(uiState.selectedCategory)
            )
        }

    init {
        refreshVulnOverviews()
    }

    /** ホーム画面に表示する脆弱性対策情報を更新します */
    fun refreshVulnOverviews() {
        if (!_uiState.value.isRefreshing) {
            viewModelScope.launch {
                _uiState.update { it.copy(isRefreshing = true) }
                val result = fetchVulnOverviewUseCase()
                if (result.isFailure) {
                    // ネットワークエラー
                    errorChannel.send(R.string.error_network_connection)
                }
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    /**
     * お気に入り登録を更新します
     *
     * @param id 対象のID
     * @param favorite お気に入りに変更する場合はtrue、それ以外はfalse
     */
    fun onFavoriteButtonClicked(id: String, favorite: Boolean) {
        viewModelScope.launch {
            favoriteVulnOverviewUseCase(id, favorite)
        }
    }

    /**
     * フィルターカテゴリを変更します
     *
     * @param category 対象のカテゴリ
     */
    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * カテゴリでフィルタリングします
     *
     * @param category フィルタリングするカテゴリ
     * @return フィルタリングした脆弱性概要情報の一覧
     */
    private fun List<DomainVulnOverview>.filterCategory(
        category: Category
    ): List<DomainVulnOverview> {
        return when (category) {
            Category.All              -> this
            Category.Favorite         -> this.filter { it.isFavorite }
            Category.SeverityCritical -> {
                this.filter {
                    it.cvssList.checkSeverity("critical")
                }
            }

            Category.SeverityHigh     -> {
                this.filter {
                    it.cvssList.checkSeverity("high")
                }
            }

            Category.SeverityMiddle   -> {
                this.filter {
                    it.cvssList.checkSeverity("middle")
                }
            }
        }
    }

    /**
     * 指定の深刻度であるかをチェックします
     *
     * @param severity 深刻度
     * @return 指定の深刻度に合致する場合はtrue、それ以外はfalse
     */
    private fun List<DomainCVSS>.checkSeverity(severity: CharSequence): Boolean {
        return this.any {
            it.severity.equals(severity.toString(), ignoreCase = true)
        }
    }
}

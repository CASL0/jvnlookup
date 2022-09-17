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

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.casl0.jvnlookup.model.*
import io.github.casl0.jvnlookup.repository.JvnRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * ホーム画面のビジネスロジックを扱うViewModel
 * @param jvnRepository JVN API操作用のリポジトリ
 */
class VulnOverviewViewModel(private val jvnRepository: JvnRepository) : ViewModel() {

    val vulnOverviews = jvnRepository.vulnOverviews

    /**
     * リフレッシュ中
     */
    private var _isRefreshing by mutableStateOf(false)
    val isRefreshing get() = _isRefreshing

    /**
     * 選択されているフィルターカテゴリ
     */
    private var _selectedCategory by mutableStateOf(categoryAll)
    val selectedCategory get() = _selectedCategory

    /**
     * リフレッシュ失敗時のチャネル
     */
    private val errorChannel = Channel<Boolean>()

    /**
     * リフレッシュ失敗時のエラーイベント
     */
    val hasError: Flow<Boolean> = errorChannel.receiveAsFlow()

    init {
        refreshVulnOverviews()
    }

    /**
     * ホーム画面に表示する脆弱性対策情報を更新します
     */
    fun refreshVulnOverviews() {
        viewModelScope.launch {
            _isRefreshing = true
            try {
                jvnRepository.refreshVulnOverviews()
            } catch (e: Exception) {
                // ネットワークエラー
                e.localizedMessage?.let { Log.d(TAG, it) }
                errorChannel.send(true)
            }
            _isRefreshing = false
        }
    }

    /**
     * リストアイテムのクリックハンドラ。脆弱性対策情報のWebページへ遷移します
     */
    fun onItemClicked(context: Context, url: CharSequence) {
        CustomTabsIntent.Builder().build().run {
            launchUrl(context, Uri.parse(url as String?))
        }
    }

    /**
     * お気に入り登録を更新します
     */
    fun onFavoriteButtonClicked(id: String, favorite: Boolean) {
        viewModelScope.launch {
            jvnRepository.updateFavorite(id, favorite)
        }
    }

    /**
     * フィルターカテゴリを変更します
     */
    fun onCategorySelected(category: Category) {
        _selectedCategory = category
    }

    /**
     * カテゴリでフィルタリングします
     */
    fun filterCategory(originalList: List<DomainVulnOverview>, category: Category) =
        when (category) {
            categoryAll -> originalList
            categoryFavorite -> originalList.filter { it.isFavorite }
            categorySeverityCritical -> {
                originalList.filter {
                    checkSeverity(it.cvssList, "critical")
                }
            }
            categorySeverityHigh -> {
                originalList.filter {
                    checkSeverity(it.cvssList, "high")
                }
            }
            categorySeverityMiddle -> {
                originalList.filter {
                    checkSeverity(it.cvssList, "middle")
                }
            }
            else -> originalList
        }

    /**
     * 指定の深刻度であるかをチェックします
     */
    private fun checkSeverity(cvssList: List<DomainCVSS>, severity: CharSequence): Boolean {
        cvssList.forEach {
            if (it.severity.equals(severity.toString(), ignoreCase = true)) return true
        }
        return false
    }

    /**
     * VulnOverviewViewModelのファクトリ
     */
    companion object {
        fun provideFactory(
            jvnRepository: JvnRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VulnOverviewViewModel(jvnRepository) as T
            }
        }

        private const val TAG = "VulnOverviewViewModel"
    }
}
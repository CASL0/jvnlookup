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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.casl0.jvnlookup.repository.JvnRepository
import kotlinx.coroutines.launch

/**
 * ホーム画面のビジネスロジックを扱うViewModel
 * @param jvnRepository JVN API操作用のリポジトリ
 */
class VulnOverviewViewModel(private val jvnRepository: JvnRepository) : ViewModel() {

    val vulnOverviews = jvnRepository.vulnOverviews

    init {
        refreshVulnOverviews()
    }

    /**
     * ホーム画面に表示する脆弱性対策情報を更新します
     */
    fun refreshVulnOverviews() {
        viewModelScope.launch {
            jvnRepository.refreshVulnOverviews()
        }
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
    }
}
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

package io.github.casl0.jvnlookup.repository

import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * JVN APIでキーワード検索した結果を取得するリポジトリ
 *
 * @param jvnRemoteDataSource リモートのデータ層
 */
class DefaultSearchRepository @Inject constructor(
    private val jvnRemoteDataSource: JvnDataSource
) : SearchRepository {
    /** 検索結果 */
    private val _searchResults = MutableStateFlow<List<DomainVulnOverview>>(listOf())
    override val searchResults: StateFlow<List<DomainVulnOverview>> get() = _searchResults

    /**
     * JVN APIを使用し、脆弱性対策情報をキーワード検索します
     *
     * @return 検索のHIT件数
     */
    override suspend fun searchOnJvn(keyword: CharSequence): Int {
        Timber.d("search on jvn")
        val result = jvnRemoteDataSource.getVulnOverviews(keyword).getOrElse {
            // TODO: エラーハンドリング
            return 0
        }
        _searchResults.value = result
        return result.size
    }
}

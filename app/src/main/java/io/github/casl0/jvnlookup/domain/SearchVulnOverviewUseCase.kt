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

package io.github.casl0.jvnlookup.domain

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** 脆弱性対策情報の検索のためのUseCase層のインターフェース */
interface SearchVulnOverviewUseCase {
    /** 検索結果 */
    val searchResults: Flow<List<DomainVulnOverview>>

    /**
     * JVN APIを使用し、脆弱性対策情報をキーワード検索します
     *
     * @return 検索のHIT件数
     */
    suspend operator fun invoke(keyword: CharSequence): Result<Int>
}

/**
 * 脆弱性対策情報の検索のためのUseCase層
 *
 * @param searchRepository JVN検索用のリポジトリ層
 * @param defaultDispatcher 脆弱性対策情報を検索時のDispatcher
 */
class DefaultSearchVulnOverviewUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SearchVulnOverviewUseCase {
    /** 検索結果 */
    override val searchResults: Flow<List<DomainVulnOverview>> = searchRepository.searchResults

    /**
     * JVN APIを使用し、脆弱性対策情報をキーワード検索します
     *
     * @return 検索のHIT件数
     */
    override suspend operator fun invoke(keyword: CharSequence): Result<Int> =
        withContext(defaultDispatcher) {
            return@withContext searchRepository.searchOnJvn(keyword)
        }
}

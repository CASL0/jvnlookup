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
import io.github.casl0.jvnlookup.repository.JvnRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** 脆弱性対策情報を取得するためのUseCase層のインターフェース */
interface FetchVulnOverviewUseCase {
    /** ディスク保存済み脆弱性対策情報 */
    val vulnOverviews: Flow<List<DomainVulnOverview>>

    /**
     * ローカルに保存しているJVNデータを更新します
     *
     * @return 保存に成功した場合はResult.success、失敗した場合はResult.failure
     */
    suspend operator fun invoke(): Result<Unit>
}

/**
 * 脆弱性対策情報を取得するためのUseCase層
 *
 * @param jvnRepository JVNデータ取得用のリポジトリ層
 * @param defaultDispatcher 脆弱性対策情報を取得時のDispatcher
 */
class DefaultFetchVulnOverviewUseCase @Inject constructor(
    private val jvnRepository: JvnRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FetchVulnOverviewUseCase {
    /** ディスク保存済み脆弱性対策情報 */
    override val vulnOverviews: Flow<List<DomainVulnOverview>> =
        jvnRepository.vulnOverviews.flowOn(defaultDispatcher)

    /**
     * ローカルに保存しているJVNデータを更新します
     *
     * @return 保存に成功した場合はResult.success、失敗した場合はResult.failure
     */
    override suspend operator fun invoke(): Result<Unit> =
        withContext(defaultDispatcher) {
            return@withContext jvnRepository.refreshVulnOverviews()
        }
}

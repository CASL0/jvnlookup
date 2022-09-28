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

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.casl0.jvnlookup.database.asDomainModel
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.repository.JvnRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 脆弱性対策情報を取得するためのUseCase層
 * @param jvnRepository JVNデータ取得用のリポジトリ層
 * @param defaultDispatcher 脆弱性対策情報を取得時のDispatcher
 */
class FetchVulnOverviewUseCase(
    private val jvnRepository: JvnRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    val vulnOverviews: LiveData<List<DomainVulnOverview>> = Transformations.map(
        jvnRepository.vulnOverviews
    ) {
        it.asDomainModel()
    }

    suspend operator fun invoke() =
        withContext(defaultDispatcher) {
            jvnRepository.refreshVulnOverviews()
        }
}

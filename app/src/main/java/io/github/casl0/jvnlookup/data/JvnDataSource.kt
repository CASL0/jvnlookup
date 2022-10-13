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

package io.github.casl0.jvnlookup.data

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.flow.Flow

/**
 * JVNの情報へのエントリーポイント
 */
interface JvnDataSource {
    fun getVulnOverviewsStream(): Flow<List<DomainVulnOverview>>

    suspend fun getVulnOverviews(
        keyword: CharSequence? = null,
        rangeDatePublic: CharSequence = "n",
        rangeDatePublished: CharSequence = "n",
        rangeDateFirstPublished: CharSequence = "n"
    ): List<DomainVulnOverview>

    fun getFavoritesStream(): Flow<List<DomainVulnOverview>>

    suspend fun saveVulnOverviews(vulnOverviews: List<DomainVulnOverview>)

    suspend fun updateFavorite(id: String, favorite: Boolean)

    suspend fun deleteAll()

    suspend fun exists(secIdentifier: CharSequence): Boolean
}

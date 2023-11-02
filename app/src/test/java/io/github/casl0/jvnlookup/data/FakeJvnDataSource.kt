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

package io.github.casl0.jvnlookup.data

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeJvnDataSource(
    initialLocalValues: List<DomainVulnOverview>,
    private val stubRemoteValues: List<DomainVulnOverview>
) : JvnDataSource {
    private val _vulnOverviews = MutableStateFlow(initialLocalValues)

    override fun getVulnOverviewsStream(): Flow<List<DomainVulnOverview>> {
        return _vulnOverviews
    }

    override suspend fun getVulnOverviews(
        keyword: CharSequence?,
        rangeDatePublic: CharSequence,
        rangeDatePublished: CharSequence,
        rangeDateFirstPublished: CharSequence
    ): Result<List<DomainVulnOverview>> {
        return Result.success(stubRemoteValues)
    }

    override fun getFavoritesStream(): Flow<List<DomainVulnOverview>> {
        return _vulnOverviews.map { it.filter { overview -> overview.isFavorite } }
    }

    override suspend fun saveVulnOverviews(vulnOverviews: List<DomainVulnOverview>) {
        val current = _vulnOverviews.value.toMutableList()
        current.addAll(vulnOverviews)
        _vulnOverviews.value = current
    }

    override suspend fun updateFavorite(id: String, favorite: Boolean) {
        val converted = _vulnOverviews.value.map {
            if (it.id == id) {
                it.copy(isFavorite = favorite)
            } else {
                it
            }
        }
        _vulnOverviews.value = converted
    }

    override suspend fun deleteAll() {
        _vulnOverviews.value = listOf()
    }

    override suspend fun exists(secIdentifier: CharSequence): Boolean {
        TODO("Not yet implemented")
    }
}

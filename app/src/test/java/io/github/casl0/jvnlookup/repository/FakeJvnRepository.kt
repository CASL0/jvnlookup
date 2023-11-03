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

package io.github.casl0.jvnlookup.repository

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeJvnRepository(
    private val stubRemoteVulnOverviews: List<DomainVulnOverview>
) : JvnRepository {
    private val _vulnOverviews = MutableStateFlow(listOf<DomainVulnOverview>())

    override val vulnOverviews: Flow<List<DomainVulnOverview>>
        get() = _vulnOverviews

    override val favorites: Flow<List<DomainVulnOverview>>
        get() = _vulnOverviews.map { it.filter { overview -> overview.isFavorite } }

    override suspend fun refreshVulnOverviews(): Result<Unit> {
        _vulnOverviews.value = _vulnOverviews.value.toMutableList().apply {
            addAll(stubRemoteVulnOverviews)
        }
        return Result.success(Unit)
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

    override suspend fun exists(secIdentifier: CharSequence): Boolean {
        return _vulnOverviews.value.any { it.id == secIdentifier }
    }

    override suspend fun insertVulnOverview(vulnOverview: DomainVulnOverview) {
        _vulnOverviews.value = _vulnOverviews.value.toMutableList().apply {
            add(vulnOverview)
        }
    }
}

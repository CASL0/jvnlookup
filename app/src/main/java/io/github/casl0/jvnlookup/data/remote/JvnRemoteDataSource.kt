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

package io.github.casl0.jvnlookup.data.remote

import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.network.MyJvnApiService
import io.github.casl0.jvnlookup.network.asDomainModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * RetrofitによるData層の実装
 */
class JvnRemoteDataSource @Inject constructor(private val myJvnApiService: MyJvnApiService) :
    JvnDataSource {
    override fun getVulnOverviewsStream(): Flow<List<DomainVulnOverview>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVulnOverviews(keyword: CharSequence?): List<DomainVulnOverview> =
        myJvnApiService.getVulnOverviewList(
            keyword = keyword as String?,
            rangeDatePublic = "n",
            rangeDatePublished = "m",
            rangeDateFirstPublished = "m"
        ).asDomainModel()

    override fun getFavoritesStream(): Flow<List<DomainVulnOverview>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveVulnOverviews(vulnOverviews: List<DomainVulnOverview>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateFavorite(id: String, favorite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override suspend fun exists(secIdentifier: CharSequence): Boolean {
        TODO("Not yet implemented")
    }
}

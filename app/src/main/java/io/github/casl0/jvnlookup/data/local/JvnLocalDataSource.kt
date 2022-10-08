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

package io.github.casl0.jvnlookup.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.database.JvnDatabase
import io.github.casl0.jvnlookup.database.asDomainModel
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.model.asDatabaseEntity

/**
 * RoomによるData層の実装
 */
class JvnLocalDataSource(private val database: JvnDatabase) : JvnDataSource {
    override fun getVulnOverviewsStream(): LiveData<List<DomainVulnOverview>> = Transformations.map(
        database.vulnOverviewDao.getVulnOverviewWithReferencesAndCVSS()
    ) {
        it.asDomainModel()
    }

    override suspend fun getVulnOverviews(keyword: CharSequence?): List<DomainVulnOverview> {
        TODO("Not yet implemented")
    }

    override fun getFavoritesStream(): LiveData<List<DomainVulnOverview>> =
        Transformations.map(database.vulnOverviewDao.getFavorites()) {
            it.asDomainModel()
        }


    override suspend fun saveVulnOverviews(vulnOverviews: List<DomainVulnOverview>) {
        vulnOverviews.forEach {
            database.vulnOverviewDao.insert(it.asDatabaseEntity())
            database.referenceDao.insertAll(it.references.asDatabaseEntity(it.id))
            database.cvssDao.insertAll(it.cvssList.asDatabaseEntity(it.id))
        }
    }

    override suspend fun updateFavorite(id: String, favorite: Boolean) {
        database.vulnOverviewDao.updateFavoriteById(id, favorite)
    }

    override suspend fun deleteAll() {
        database.cvssDao.deleteAll()
        database.referenceDao.deleteAll()
        database.vulnOverviewDao.deleteAllNonFavorite()
    }

    override suspend fun exists(secIdentifier: CharSequence): Boolean =
        database.vulnOverviewDao.exists(secIdentifier as String)
}

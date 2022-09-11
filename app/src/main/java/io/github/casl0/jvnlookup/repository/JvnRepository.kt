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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.casl0.jvnlookup.database.JvnDatabase
import io.github.casl0.jvnlookup.database.asDomainModel
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.network.MyJvnApi
import io.github.casl0.jvnlookup.network.asDatabaseCVSS
import io.github.casl0.jvnlookup.network.asDatabaseReferences
import io.github.casl0.jvnlookup.network.asDatabaseVulnOverviews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * JVN APIからデータを取得し、Roomに保存するリポジトリ
 * @param database 保存先のデータベース
 */
class JvnRepository(private val database: JvnDatabase) {
    companion object {
        private const val TAG = "JvnRepository"
    }

    val vulnOverviews: LiveData<List<DomainVulnOverview>> = Transformations.map(
        database.vulnOverviewDao.getVulnOverviewWithReferencesAndCVSS()
    ) {
        it.asDomainModel()
    }

    /**
     * ローカルに保存しているJVNデータを更新します
     */
    suspend fun refreshVulnOverviews() = withContext(Dispatchers.IO) {
        Log.d(TAG, "refresh vuln overviews")
        val vulnOverviews = MyJvnApi.retrofitService.getVulnOverviewList()
        database.cvssDao.deleteAll()
        database.referenceDao.deleteAll()
        database.vulnOverviewDao.deleteAll()
        database.vulnOverviewDao.insertAll(vulnOverviews.asDatabaseVulnOverviews())
        database.referenceDao.insertAll(vulnOverviews.asDatabaseReferences())
        database.cvssDao.insertAll(vulnOverviews.asDatabaseCVSS())
    }
}
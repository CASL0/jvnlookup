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

import androidx.lifecycle.LiveData
import io.github.casl0.jvnlookup.database.*
import io.github.casl0.jvnlookup.network.MyJvnApi
import io.github.casl0.jvnlookup.network.asDatabaseCVSS
import io.github.casl0.jvnlookup.network.asDatabaseReferences
import io.github.casl0.jvnlookup.network.asDatabaseVulnOverviews
import timber.log.Timber

/**
 * JVN APIからデータを取得し、Roomに保存するリポジトリ
 * @param database 保存先のデータベース
 */
class JvnRepository(private val database: JvnDatabase) {

    /**
     * 保存済み脆弱性対策情報
     */
    val vulnOverviews: LiveData<List<VulnOverviewWithReferencesAndCVSS>> =
        database.vulnOverviewDao.getVulnOverviewWithReferencesAndCVSS()

    /**
     * お気に入り登録済みの脆弱性対策情報
     */
    val favorites: LiveData<List<VulnOverviewWithReferencesAndCVSS>> =
        database.vulnOverviewDao.getFavorites()

    /**
     * ローカルに保存しているJVNデータを更新します
     */
    suspend fun refreshVulnOverviews() {
        Timber.d("refresh vuln overviews")
        val vulnOverviews = MyJvnApi.retrofitService.getVulnOverviewList(
            rangeDatePublic = "n",
            rangeDatePublished = "m",
            rangeDateFirstPublished = "m"
        )
        database.cvssDao.deleteAll()
        database.referenceDao.deleteAll()
        database.vulnOverviewDao.deleteAllNonFavorite()
        database.vulnOverviewDao.insertAll(vulnOverviews.asDatabaseVulnOverviews())
        database.referenceDao.insertAll(vulnOverviews.asDatabaseReferences())
        database.cvssDao.insertAll(vulnOverviews.asDatabaseCVSS())
    }

    /**
     * お気に入り登録を更新します
     */
    suspend fun updateFavorite(id: String, favorite: Boolean) {
        Timber.d("update favorite")
        database.vulnOverviewDao.updateFavoriteById(id, favorite)
    }

    /**
     * ローカルに保存済みかをチェックします
     * @param secIdentifier ベンダ固有のセキュリティID
     */
    suspend fun exists(secIdentifier: CharSequence): Boolean {
        return database.vulnOverviewDao.exists(secIdentifier as String)
    }

    /**
     * 脆弱性対策情報をローカルに保存します
     * @param vulnOverview 脆弱性対策情報
     * @param references 脆弱性対策情報の参考情報
     * @param cvssList 脆弱性対策情報のCVSS
     */
    suspend fun insertVulnOverview(
        vulnOverview: DatabaseVulnOverview,
        references: List<DatabaseReference>,
        cvssList: List<DatabaseCVSS>
    ) {
        database.vulnOverviewDao.insert(vulnOverview)
        database.referenceDao.insertAll(references)
        database.cvssDao.insertAll(cvssList)
    }
}

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
import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import timber.log.Timber

/**
 * JVN APIからデータを取得し、Roomに保存するリポジトリ
 * @param jvnLocalDataSource ローカルのデータ層
 * @param jvnRemoteDataSource リモートのデータ層
 */
class JvnRepository(
    private val jvnLocalDataSource: JvnDataSource,
    private val jvnRemoteDataSource: JvnDataSource
) {

    /**
     * 保存済み脆弱性対策情報
     */
    val vulnOverviews: LiveData<List<DomainVulnOverview>> =
        jvnLocalDataSource.getVulnOverviewsStream()

    /**
     * お気に入り登録済みの脆弱性対策情報
     */
    val favorites: LiveData<List<DomainVulnOverview>> = jvnLocalDataSource.getFavoritesStream()

    /**
     * ローカルに保存しているJVNデータを更新します
     */
    suspend fun refreshVulnOverviews() {
        Timber.d("refresh vuln overviews")
        val vulnOverviews = jvnRemoteDataSource.getVulnOverviews()
        jvnLocalDataSource.deleteAll()
        jvnLocalDataSource.saveVulnOverviews(vulnOverviews)
    }

    /**
     * お気に入り登録を更新します
     */
    suspend fun updateFavorite(id: String, favorite: Boolean) {
        Timber.d("update favorite")
        jvnLocalDataSource.updateFavorite(id, favorite)
    }

    /**
     * ローカルに保存済みかをチェックします
     * @param secIdentifier ベンダ固有のセキュリティID
     */
    suspend fun exists(secIdentifier: CharSequence): Boolean {
        return jvnLocalDataSource.exists(secIdentifier)
    }

    /**
     * 脆弱性対策情報をローカルに保存します
     * @param vulnOverview 脆弱性対策情報
     */
    suspend fun insertVulnOverview(
        vulnOverview: DomainVulnOverview,
    ) {
        jvnLocalDataSource.saveVulnOverviews(listOf(vulnOverview))
    }
}

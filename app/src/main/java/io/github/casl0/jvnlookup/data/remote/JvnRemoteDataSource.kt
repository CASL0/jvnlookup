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
import timber.log.Timber
import javax.inject.Inject

/** RetrofitによるData層の実装 */
class JvnRemoteDataSource @Inject constructor(private val myJvnApiService: MyJvnApiService) :
    JvnDataSource {
    override fun getVulnOverviewsStream(): Flow<List<DomainVulnOverview>> {
        TODO("Not yet implemented")
    }

    /**
     * 脆弱性概要情報を取得します
     *
     * @param keyword キーワード（オプション）
     * @param rangeDatePublic 発見日(脆弱性対策情報が一般に公表された日付)の範囲指定
     *     n:範囲指定なし、w:過去1週間、m:過去1ヶ月
     * @param rangeDatePublished 更新日(脆弱性対策情報が更新された最後の日付)の範囲指定
     *     n:範囲指定なし、w:過去1週間、m:過去1ヶ月
     * @param rangeDateFirstPublished 発行日(脆弱性対策情報に初めて登録された日付)の範囲指定
     *     n:範囲指定なし、w:過去1週間、m:過去1ヶ月
     * @return 取得した脆弱性概要情報
     */
    override suspend fun getVulnOverviews(
        keyword: CharSequence?,
        rangeDatePublic: CharSequence,
        rangeDatePublished: CharSequence,
        rangeDateFirstPublished: CharSequence
    ): Result<List<DomainVulnOverview>> {
        return try {
            Result.success(
                myJvnApiService.getVulnOverviewList(
                    keyword = keyword as String?,
                    rangeDatePublic = rangeDatePublic as String,
                    rangeDatePublished = rangeDatePublished as String,
                    rangeDateFirstPublished = rangeDateFirstPublished as String
                ).asDomainModel()
            )
        } catch (e: Throwable) {
            Timber.e(e)
            Result.failure(e)
        }
    }


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

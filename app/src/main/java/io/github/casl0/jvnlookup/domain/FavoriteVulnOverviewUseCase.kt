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

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.repository.JvnRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** 脆弱性対策情報をお気に入り登録するためのUseCase層のインターフェース */
interface FavoriteVulnOverviewUseCase {
    /** お気に入りした脆弱性概要情報一覧 */
    val favorites: Flow<List<DomainVulnOverview>>

    /**
     * 既に保存済みのレコードのお気に入り状態を更新します
     *
     * @param id ベンダ固有のセキュリティID
     * @param favorite 更新後の状態
     */
    suspend operator fun invoke(id: CharSequence, favorite: Boolean)

    /**
     * お気に入り状態を更新したレコードを登録します
     *
     * @param vulnOverview 更新したいレコード
     * @param favorite 更新後のお気に入り状態
     */
    suspend operator fun invoke(vulnOverview: DomainVulnOverview, favorite: Boolean)
}

/**
 * 脆弱性対策情報をお気に入り登録するためのUseCase層
 *
 * @param jvnRepository JVNデータ取得用のリポジトリ層
 * @param defaultDispatcher お気に入り登録実行時のDispatcher
 */
class DefaultFavoriteVulnOverviewUseCase @Inject constructor(
    private val jvnRepository: JvnRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FavoriteVulnOverviewUseCase {
    /** お気に入りした脆弱性概要情報一覧 */
    override val favorites: Flow<List<DomainVulnOverview>> =
        jvnRepository.favorites.flowOn(defaultDispatcher)

    /**
     * 既に保存済みのレコードのお気に入り状態を更新します
     *
     * @param id ベンダ固有のセキュリティID
     * @param favorite 更新後の状態
     */
    override suspend operator fun invoke(id: CharSequence, favorite: Boolean) =
        withContext(defaultDispatcher) {
            jvnRepository.updateFavorite(id as String, favorite)
        }

    /**
     * お気に入り状態を更新したレコードを登録します
     *
     * @param vulnOverview 更新したいレコード
     * @param favorite 更新後のお気に入り状態
     */
    override suspend operator fun invoke(vulnOverview: DomainVulnOverview, favorite: Boolean) =
        withContext(defaultDispatcher) {
            vulnOverview.isFavorite = favorite
            if (jvnRepository.exists(vulnOverview.id)) {
                // ローカルに保存済みの場合は更新
                jvnRepository.updateFavorite(vulnOverview.id, vulnOverview.isFavorite)
            } else {
                jvnRepository.insertVulnOverview(vulnOverview)
            }
        }
}

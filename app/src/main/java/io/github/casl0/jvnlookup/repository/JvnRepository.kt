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

/** JVNデータ層へのアクセス抽象化のリポジトリ */
interface JvnRepository {
    /** 保存済み脆弱性対策情報 */
    val vulnOverviews: Flow<List<DomainVulnOverview>>

    /** お気に入り登録済みの脆弱性対策情報 */
    val favorites: Flow<List<DomainVulnOverview>>

    /**
     * ローカルに保存しているJVNデータを更新します
     *
     * @return 保存に成功した場合はResult.success、失敗した場合はResult.failure
     */
    suspend fun refreshVulnOverviews(): Result<Unit>

    /**
     * お気に入り登録を更新します
     *
     * @param id 変更対象のレコードID
     * @param favorite 変更後のお気に入り状態
     */
    suspend fun updateFavorite(id: String, favorite: Boolean)

    /**
     * ローカルに保存済みかをチェックします
     *
     * @param secIdentifier ベンダ固有のセキュリティID
     * @return 保存済みであればtrue、それ以外はfalse
     */
    suspend fun exists(secIdentifier: CharSequence): Boolean

    /**
     * 脆弱性対策情報をローカルに保存します
     *
     * @param vulnOverview 脆弱性対策情報
     */
    suspend fun insertVulnOverview(
        vulnOverview: DomainVulnOverview,
    )
}

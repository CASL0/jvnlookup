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
import kotlinx.coroutines.flow.StateFlow

/** JVN検索結果のデータ層へのアクセス抽象化のリポジトリ */
interface SearchRepository {
    /** 検索結果 */
    val searchResults: StateFlow<List<DomainVulnOverview>>

    /**
     * JVN APIを使用し、脆弱性対策情報をキーワード検索します
     *
     * @return 検索のHIT件数
     */
    suspend fun searchOnJvn(keyword: CharSequence): Result<Int>
}

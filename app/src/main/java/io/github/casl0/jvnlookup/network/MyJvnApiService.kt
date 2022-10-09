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

package io.github.casl0.jvnlookup.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MyJvnApiService {
    /**
     * フィルタリング条件に当てはまる脆弱性対策の概要情報リストを取得します。
     * @param keyword 検索キーワード
     * @param rangeDatePublic 発見日(脆弱性対策情報が一般に公表された日付)の範囲指定 n:範囲指定なし、w:過去1週間、m:過去1ヶ月
     * @param rangeDatePublished 更新日(脆弱性対策情報が更新された最後の日付)の範囲指定 n:範囲指定なし、w:過去1週間、m:過去1ヶ月
     * @param rangeDateFirstPublished 発行日(脆弱性対策情報に初めて登録された日付)の範囲指定 n:範囲指定なし、w:過去1週間、m:過去1ヶ月
     * @see https://jvndb.jvn.jp/apis/getVulnOverviewList_api_hnd.html
     */
    @GET("myjvn?method=getVulnOverviewList&feed=hnd")
    suspend fun getVulnOverviewList(
        @Query("keyword") keyword: String? = null,
        @Query("rangeDatePublic") rangeDatePublic: String = "n",
        @Query("rangeDatePublished") rangeDatePublished: String = "n",
        @Query("rangeDateFirstPublished") rangeDateFirstPublished: String = "n",
    ): VulnOverviewResponse
}

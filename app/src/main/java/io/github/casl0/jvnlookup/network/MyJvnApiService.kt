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

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://jvndb.jvn.jp/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        TikXmlConverterFactory.create(
            TikXml.Builder().exceptionOnUnreadXml(false).build()
        )
    )
    .baseUrl(BASE_URL)
    .build()

interface MyJvnApiService {
    /**
     * フィルタリング条件に当てはまる脆弱性対策の概要情報リストを取得します。
     * @see https://jvndb.jvn.jp/apis/getVulnOverviewList_api_hnd.html
     */
    @GET("myjvn?method=getVulnOverviewList&feed=hnd&rangeDatePublic=n")
    suspend fun getVulnOverviewList(): VulnOverviewResponse
}

object MyJvnApi {
    val retrofitService: MyJvnApiService by lazy {
        retrofit.create(MyJvnApiService::class.java)
    }
}
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
import androidx.lifecycle.MutableLiveData
import io.github.casl0.jvnlookup.network.MyJvnApi
import io.github.casl0.jvnlookup.network.VulnOverviewResponse
import timber.log.Timber

/**
 * JVN APIでキーワード検索した結果を取得するリポジトリ
 */
class SearchRepository {
    /**
     * 検索結果
     */
    private val _searchResults = MutableLiveData<VulnOverviewResponse>()
    val searchResults: LiveData<VulnOverviewResponse> get() = _searchResults

    /**
     * JVN APIを使用し、脆弱性対策情報をキーワード検索します
     * @return 検索のHIT件数
     */
    suspend fun searchOnJvn(keyword: CharSequence): Int {
        Timber.d("search on jvn")
        MyJvnApi.retrofitService.getVulnOverviewList(keyword as String).run {
            _searchResults.postValue(this)
            return this.vulnOverviews.filter { it.id != null }.size
        }
    }
}

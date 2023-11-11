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

import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toInstant
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class DefaultSearchRepositoryTest {
    @Test
    fun searchOnJvn_getVulnOverviewsFailed_ReturnsResultFailure() = runTest {
        val keyword = "keyword"
        val mock = mock<JvnDataSource> {
            onBlocking {
                getVulnOverviews(
                    keyword,
                )
            } doReturn Result.failure(Exception("error"))
        }

        val repository = DefaultSearchRepository(mock)
        val result = repository.searchOnJvn(keyword)

        assertTrue(result.isFailure)
        assertThat(
            result.exceptionOrNull().toString(),
            CoreMatchers.`is`(Exception("error").toString())
        )
    }

    @Test
    fun searchOnJvn_getVulnOverviewsSucceeded_LocalDataUpdated() = runTest {
        val keyword = "keyword"
        val remoteData = listOf(
            DomainVulnOverview(
                title = "Google の Android における境界外書き込みに関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020636.html",
                description = "Google の Android には、境界外書き込みに関する脆弱性が存在します。",
                id = "JVNDB-2022-020636",
                issued = "2023-11-02T17:07:02+09:00".toInstant(),
                modified = "2023-11-02T17:07:02+09:00".toInstant(),
                isFavorite = false
            )
        )
        val mock = mock<JvnDataSource> {
            onBlocking {
                getVulnOverviews(
                    keyword,
                )
            } doReturn Result.success(remoteData)
        }

        val repository = DefaultSearchRepository(mock)
        val result = repository.searchOnJvn(keyword)

        assertTrue(result.isSuccess)
        assertThat(result.getOrNull(), `is`(remoteData.size))
        assertThat(repository.searchResults.first(), `is`(remoteData))
    }
}

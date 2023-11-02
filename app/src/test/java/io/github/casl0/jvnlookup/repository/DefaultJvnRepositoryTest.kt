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

import io.github.casl0.jvnlookup.data.FakeJvnDataSource
import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultJvnRepositoryTest {
    private lateinit var initialSavedLocalVulnOverviews: List<DomainVulnOverview>
    private lateinit var stubRemoteVulnOverviews: List<DomainVulnOverview>
    private lateinit var dataSource: JvnDataSource

    @Before
    fun setup() {
        initialSavedLocalVulnOverviews = listOf(
            DomainVulnOverview(
                title = "D-Link Systems, Inc. の DIR-846 ファームウェアにおける脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2020/JVNDB-2020-017892.html",
                description = "D-Link Systems, Inc. の DIR-846 ファームウェアには、不特定の脆弱性が存在します。",
                id = "JVNDB-2020-017892",
                issued = "2023-10-30T17:23:20+09:00",
                modified = "2023-10-30T17:23:20+09:00",
                isFavorite = true
            ),
            DomainVulnOverview(
                title = "Bluetooth SIG, Inc. の Bluetooth Core Specification における観測可能な不一致に関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2020/JVNDB-2020-017895.html",
                description = "Bluetooth SIG, Inc. の Bluetooth Core Specification には、観測可能な不一致に関する脆弱性、Capture-replay による認証回避に関する脆弱性が存在します。",
                id = "JVNDB-2020-017895",
                issued = "2023-11-02T17:07:04+09:00",
                modified = "2023-11-02T17:07:04+09:00",
                isFavorite = false
            ),
            DomainVulnOverview(
                title = "Google の Android における境界外書き込みに関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020636.html",
                description = "Google の Android には、境界外書き込みに関する脆弱性が存在します。",
                id = "JVNDB-2022-020636",
                issued = "2023-11-02T17:07:02+09:00",
                modified = "2023-11-02T17:07:02+09:00",
                isFavorite = false
            )
        )

        stubRemoteVulnOverviews = listOf(
            DomainVulnOverview(
                title = "シトリックス・システムズの Citrix Gateway および Citrix Application Delivery Controller ファームウェアにおける過度な認証試行の不適切な制限に関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020610.html",
                description = "シトリックス・システムズの Citrix Gateway および Citrix Application Delivery Controller ファームウェアには、過度な認証試行の不適切な制限に関する脆弱性が存在します。",
                id = "JVNDB-2022-020610",
                issued = "2023-11-02T17:06:01+09:00",
                modified = "2023-11-02T17:06:01+09:00",
                isFavorite = false
            ),
            DomainVulnOverview(
                title = "Fatcat Apps の WordPress 用 analytics cat におけるクロスサイトリクエストフォージェリの脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020609.html",
                description = "Fatcat Apps の WordPress 用 analytics cat には、クロスサイトリクエストフォージェリの脆弱性が存在します。",
                id = "JVNDB-2022-020609",
                issued = "2023-11-02T17:05:59+09:00",
                modified = "2023-11-02T17:05:59+09:00",
                isFavorite = false
            ),
            DomainVulnOverview(
                title = "Pojo Me Digital LTD. の WordPress 用 Activity Log における CSV ファイル内の数式要素の中和に関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020608.html",
                description = "Pojo Me Digital LTD. の WordPress 用 Activity Log には、CSV ファイル内の数式要素の中和に関する脆弱性が存在します。",
                id = "JVNDB-2022-020608",
                issued = "2023-11-02T17:05:58+09:00",
                modified = "2023-11-02T17:05:58+09:00",
                isFavorite = false
            )
        )

        dataSource = FakeJvnDataSource(
            initialSavedLocalVulnOverviews,
            stubRemoteVulnOverviews
        )
    }


    @Test
    fun refreshVulnOverviews_getVulnOverviewsFailed_ReturnsResultFailure() = runTest {
        val mock = mock<JvnDataSource> {
            onBlocking {
                getVulnOverviews(
                    null,
                    "n",
                    "m",
                    "m"
                )
            } doReturn Result.failure(Exception("error"))
        }

        val repository = DefaultJvnRepository(mock, mock)
        val result = repository.refreshVulnOverviews()

        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull().toString(), `is`(Exception("error").toString()))
    }

    @Test
    fun refreshVulnOverviews_getVulnOverviewsSucceeded_LocalDataUpdated() = runTest {
        val repository = DefaultJvnRepository(dataSource, dataSource)

        var resultLocalData: List<DomainVulnOverview> = listOf()
        val job = launch(UnconfinedTestDispatcher()) {
            repository.vulnOverviews.collect {
                resultLocalData = it
            }
        }

        val res = repository.refreshVulnOverviews()
        assertTrue(res.isSuccess)
        assertThat(resultLocalData, `is`(stubRemoteVulnOverviews))

        job.cancel()
    }

    @Test
    fun updateFavorite() = runTest {
        val repository = DefaultJvnRepository(dataSource, dataSource)

        var resultLocalData: List<DomainVulnOverview> = listOf()
        val job = launch(UnconfinedTestDispatcher()) {
            repository.vulnOverviews.collect {
                resultLocalData = it
            }
        }

        val targetId = initialSavedLocalVulnOverviews[1].id

        repository.updateFavorite(targetId, true)
        assertTrue(resultLocalData.first { it.id == targetId }.isFavorite)

        repository.updateFavorite(targetId, false)
        assertFalse(resultLocalData.first { it.id == targetId }.isFavorite)

        job.cancel()
    }

    @Test
    fun exists() = runTest {
        val repository = DefaultJvnRepository(dataSource, dataSource)

        val existingId = initialSavedLocalVulnOverviews[0].id
        val nonExistingId = "NON EXISTING"

        assertTrue(repository.exists(existingId))
        assertFalse(repository.exists(nonExistingId))
    }

    @Test
    fun insertVulnOverview_LocalDataUpdated() = runTest {
        val repository = DefaultJvnRepository(dataSource, dataSource)

        var resultLocalData: List<DomainVulnOverview> = listOf()
        val job = launch(UnconfinedTestDispatcher()) {
            repository.vulnOverviews.collect {
                resultLocalData = it
            }
        }

        val newData = DomainVulnOverview(
            title = "脆弱性タイトル",
            link = "https://jvndb.jvn.jp",
            description = "脆弱性説明",
            id = "ID",
            issued = "2023-10-30T17:23:20+09:00",
            modified = "2023-10-30T17:23:20+09:00",
            isFavorite = false
        )
        repository.insertVulnOverview(newData)

        assertTrue(resultLocalData.contains(newData))

        job.cancel()
    }
}

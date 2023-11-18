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

package io.github.casl0.jvnlookup.domain

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import io.github.casl0.jvnlookup.repository.FakeJvnRepository
import io.github.casl0.jvnlookup.repository.JvnRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toInstant
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FetchVulnOverviewUseCaseTest {
    private lateinit var stubRemoteVulnOverviews: List<DomainVulnOverview>
    private lateinit var fakeRepository: JvnRepository

    @Before
    fun setup() {
        stubRemoteVulnOverviews = listOf(
            DomainVulnOverview(
                title = "シトリックス・システムズの Citrix Gateway および Citrix Application Delivery Controller ファームウェアにおける過度な認証試行の不適切な制限に関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020610.html",
                description = "シトリックス・システムズの Citrix Gateway および Citrix Application Delivery Controller ファームウェアには、過度な認証試行の不適切な制限に関する脆弱性が存在します。",
                id = "JVNDB-2022-020610",
                issued = "2023-11-02T17:06:01+09:00".toInstant(),
                modified = "2023-11-02T17:06:01+09:00".toInstant(),
                isFavorite = false
            ),
            DomainVulnOverview(
                title = "Fatcat Apps の WordPress 用 analytics cat におけるクロスサイトリクエストフォージェリの脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020609.html",
                description = "Fatcat Apps の WordPress 用 analytics cat には、クロスサイトリクエストフォージェリの脆弱性が存在します。",
                id = "JVNDB-2022-020609",
                issued = "2023-11-02T17:05:59+09:00".toInstant(),
                modified = "2023-11-02T17:05:59+09:00".toInstant(),
                isFavorite = false
            ),
            DomainVulnOverview(
                title = "Pojo Me Digital LTD. の WordPress 用 Activity Log における CSV ファイル内の数式要素の中和に関する脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020608.html",
                description = "Pojo Me Digital LTD. の WordPress 用 Activity Log には、CSV ファイル内の数式要素の中和に関する脆弱性が存在します。",
                id = "JVNDB-2022-020608",
                issued = "2023-11-02T17:05:58+09:00".toInstant(),
                modified = "2023-11-02T17:05:58+09:00".toInstant(),
                isFavorite = false
            )
        )

        fakeRepository = FakeJvnRepository(stubRemoteVulnOverviews)
    }

    @Test
    fun invoke_LocalDataUpdated() = runTest {
        val useCase =
            DefaultFetchVulnOverviewUseCase(fakeRepository, UnconfinedTestDispatcher(testScheduler))

        assertThat(useCase.vulnOverviews.first(), `is`(listOf()))

        val result = useCase()

        assertTrue(result.isSuccess)
        assertThat(useCase.vulnOverviews.first(), `is`(stubRemoteVulnOverviews))
    }
}

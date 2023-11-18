package io.github.casl0.jvnlookup.ui.vulnoverview

import io.github.casl0.jvnlookup.domain.FavoriteVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.SpyFetchVulnOverviewUseCase
import io.github.casl0.jvnlookup.model.Category
import io.github.casl0.jvnlookup.model.DomainCVSS
import io.github.casl0.jvnlookup.model.DomainReference
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.toInstant
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class VulnOverviewViewModelTest {
    private lateinit var spyFetchVulnOverviewUseCase: SpyFetchVulnOverviewUseCase
    private lateinit var stubData: List<DomainVulnOverview>

    @Mock
    private val mockFavoriteVulnOverviewUseCase = mock<FavoriteVulnOverviewUseCase>()

    @Before
    fun setup() {
        // viewModelScope向けにmainディスパッチャを変更
        Dispatchers.setMain(UnconfinedTestDispatcher())

        stubData = listOf(
            DomainVulnOverview(
                title = "複数の ZyXEL 製品におけるクロスサイトスクリプティングの脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-022564.html",
                description = "ATP800 ファームウェア、ATP700 ファームウェア、ATP500 ファームウェア等複数の ZyXEL 製品には、クロスサイトスクリプティングの脆弱性が存在します。",
                id = "JVNDB-2022-022564",
                issued = "2023-11-17T17:21:16+09:00".toInstant(),
                modified = "2023-11-17T17:21:16+09:00".toInstant(),
                isFavorite = false,
                references = listOf(
                    DomainReference(
                        id = "CVE-2022-40603",
                        url = "https://nvd.nist.gov/vuln/detail/CVE-2022-40603",
                        source = "NVD",
                        title = null
                    )
                ),
                cvssList = listOf(
                    DomainCVSS(
                        version = "3.0",
                        type = "Base",
                        severity = "Medium",
                        score = "6.1",
                        vector = "CVSS:3.0/AV:N/AC:L/PR:N/UI:R/S:C/C:L/I:L/A:N"
                    )
                )
            )
        )
        spyFetchVulnOverviewUseCase = SpyFetchVulnOverviewUseCase(stubData)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun refreshVulnOverviews_exclusiveFetchVulnOverview() = runTest {
        val viewModel = VulnOverviewViewModel(
            spyFetchVulnOverviewUseCase,
            mockFavoriteVulnOverviewUseCase
        )
        // イニシャライザでスケジュールされたタスクを実行しておく
        advanceUntilIdle()

        viewModel.refreshVulnOverviews()
        viewModel.refreshVulnOverviews()
        assertThat(viewModel.uiState.first().isRefreshing, `is`(true))
        advanceTimeBy(1_001)
        assertThat(viewModel.uiState.first().isRefreshing, `is`(false))

        // イニシャライザで実行した分とrefreshVulnOverviews()呼び出しとの計2回
        assertThat(spyFetchVulnOverviewUseCase.invokeCallCount, `is`(2))
    }

    @Test
    fun onFavoriteButtonClicked_invokeFavoriteVulnOverviewUseCase() = runTest {
        val viewModel = VulnOverviewViewModel(
            spyFetchVulnOverviewUseCase,
            mockFavoriteVulnOverviewUseCase
        )
        // イニシャライザでスケジュールされたタスクを実行しておく
        advanceUntilIdle()

        viewModel.onFavoriteButtonClicked("id", true)

        verify(mockFavoriteVulnOverviewUseCase, times(1)).invoke("id", true)
    }

    @Test
    fun onCategorySelected_selectedCategoryInUiStateIsUpdated() = runTest {
        val viewModel = VulnOverviewViewModel(
            spyFetchVulnOverviewUseCase,
            mockFavoriteVulnOverviewUseCase
        )
        // イニシャライザでスケジュールされたタスクを実行しておく
        advanceUntilIdle()

        var resultUiState = VulnOverviewUiState()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {
                resultUiState = it
            }
        }

        assertThat(resultUiState.selectedCategory, `is`(Category.All))
        viewModel.onCategorySelected(Category.Favorite)
        assertThat(resultUiState.selectedCategory, `is`(Category.Favorite))

        job.cancel()
    }
}

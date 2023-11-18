package io.github.casl0.jvnlookup.ui.search

import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.domain.SearchVulnOverviewUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @Before
    fun setup() {
        // viewModelScope向けにmainディスパッチャを変更
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchOnJvn_BlankKeyword_EarlyReturn() = runTest {
        val mock = mock<SearchVulnOverviewUseCase>()
        val viewModel = SearchViewModel(mock)

        viewModel.searchOnJvn("")
        verify(mock, times(0)).invoke("")

        viewModel.searchOnJvn("　")
        verify(mock, times(0)).invoke("　")
    }

    @Test
    fun searchOnJvn_SearchFailedNetworkError_hasErrorReceived() = runTest {
        val mock = mock<SearchVulnOverviewUseCase> {
            onBlocking {
                invoke("keyword1")
            } doReturn Result.failure(Exception("error"))
        }
        val viewModel = SearchViewModel(mock)

        var result = 0
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.hasError.collect {
                result = it
            }
        }

        viewModel.searchOnJvn("keyword1")
        assertThat(result, `is`(R.string.error_network_connection))

        job.cancel()
    }

    @Test
    fun searchOnJvn_SearchNoResults_hasErrorReceived() = runTest {
        val mock = mock<SearchVulnOverviewUseCase> {
            onBlocking {
                invoke("keyword1")
            } doReturn Result.success(0)
        }
        val viewModel = SearchViewModel(mock)

        var result = 0
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.hasError.collect {
                result = it
            }
        }

        viewModel.searchOnJvn("keyword1")
        assertThat(result, `is`(R.string.error_no_results_found))

        job.cancel()
    }

    @Test
    fun onSearchValueChanged_searchValueInUiStateIsUpdated() = runTest {
        val viewModel = SearchViewModel(mock<SearchVulnOverviewUseCase>())

        var result: SearchUiState = SearchUiState.Loaded()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {
                result = it
            }
        }

        viewModel.onSearchValueChanged("keyword1")
        assertThat((result as SearchUiState.Loaded).searchValue, `is`("keyword1"))
        job.cancel()
    }
}

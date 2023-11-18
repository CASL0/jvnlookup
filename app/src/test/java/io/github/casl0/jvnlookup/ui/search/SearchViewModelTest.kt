package io.github.casl0.jvnlookup.ui.search

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
import org.mockito.kotlin.mock

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

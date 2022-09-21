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

package io.github.casl0.jvnlookup.ui.vulnoverview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.model.filterCategories
import io.github.casl0.jvnlookup.ui.components.SnackbarLaunchedEffect

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VulnOverviewScreen(viewModel: VulnOverviewViewModel, modifier: Modifier = Modifier) {
    val vulnOverviews = viewModel.vulnOverviews.observeAsState(listOf())
    val filteredVulnOverviews =
        viewModel.filterCategory(vulnOverviews.value, viewModel.selectedCategory)
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.hasError.SnackbarLaunchedEffect(
        snackbarHostState = snackbarHostState,
        R.string.refresh_overview_action_label
    ) {
        when (it) {
            SnackbarResult.ActionPerformed -> {
                viewModel.refreshVulnOverviews()
            }
            SnackbarResult.Dismissed -> {
            }
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing),
            onRefresh = { viewModel.refreshVulnOverviews() }) {
            Column {
                val scrollState = rememberLazyListState()
                LazyColumn(
                    state = scrollState,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        CategoryChips(
                            filterCategories,
                            viewModel.selectedCategory,
                            viewModel::onCategorySelected,
                            Modifier.fillMaxWidth()
                        )
                    }
                    items(items = filteredVulnOverviews,
                        key = { vulnOverview -> vulnOverview.id }
                    ) { vulnOverview ->
                        Surface(modifier.padding(horizontal = 4.dp)) {
                            VulnOverviewItem(
                                vulnOverview,
                                viewModel::onItemClicked,
                                viewModel::onFavoriteButtonClicked,
                                modifier
                            )
                        }
                    }
                }
            }
        }
    }
}
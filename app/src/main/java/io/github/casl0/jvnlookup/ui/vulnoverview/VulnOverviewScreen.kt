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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.github.casl0.jvnlookup.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VulnOverviewScreen(viewModel: VulnOverviewViewModel, modifier: Modifier = Modifier) {
    val vulnOverviews = viewModel.vulnOverviews.observeAsState(listOf())
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.error_refresh_overview)
    val actionLabel = stringResource(R.string.refresh_overview_action_label)
    LaunchedEffect(snackbarHostState) {
        viewModel.hasError.collect { hasError ->
            if (hasError) {
                val result = snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        viewModel.refreshVulnOverviews()
                    }
                    SnackbarResult.Dismissed -> {
                    }
                }
            }
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing),
            onRefresh = { viewModel.refreshVulnOverviews() }) {
            VulnOverviewList(
                vulnOverviews = vulnOverviews.value,
                onItemClicked = viewModel.onItemClicked
            )
        }
    }
}
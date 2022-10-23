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

package io.github.casl0.jvnlookup.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.ui.components.SnackbarLaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navigateToSearchResults: () -> Unit,
) {
    val searchValue = viewModel.searchValue
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.hasError.SnackbarLaunchedEffect(snackbarHostState = snackbarHostState)
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        val scrollState = rememberScrollState()
        if (viewModel.searchInProgress) {
            ProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(it),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                SearchTextField(
                    searchValue = searchValue,
                    onValueChange = viewModel::onSearchValueChanged,
                    onSearch = { keyword ->
                        viewModel.searchOnJvn(
                            keyword,
                            navigateToSearchResults
                        )
                    },
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

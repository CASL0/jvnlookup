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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.ui.components.SnackbarLaunchedEffect
import io.github.casl0.jvnlookup.utils.CWE_IDS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    snackbarHostState: SnackbarHostState,
    navigateToSearchResults: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    viewModel.hasError.SnackbarLaunchedEffect(snackbarHostState = snackbarHostState)
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        val scrollState = rememberScrollState()

        when (uiState) {
            is SearchUiState.Loaded -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(it),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    SearchTextField(
                        searchValue = uiState.searchValue,
                        onValueChange = viewModel::onSearchValueChanged,
                        onSearch = { keyword ->
                            viewModel.searchOnJvn(
                                keyword,
                                navigateToSearchResults
                            )
                        }
                    )
                    SearchSection(title = R.string.search_cwe_section) {
                        CweCollectionsGrid(onClickCard = { keyword ->
                            viewModel.searchOnJvn(
                                keyword,
                                navigateToSearchResults
                            )
                        }, Modifier.height(232.dp))
                    }
                }
            }

            is SearchUiState.Loading -> {
                ProgressIndicator()
            }
        }
    }
}

@Composable
private fun SearchSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

@Composable
private fun CweCollectionsGrid(onClickCard: (CharSequence) -> Unit, modifier: Modifier = Modifier) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(CWE_IDS.values()) { item ->
            CweCard(item, onClickCard, Modifier.width(192.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CweCard(
    cwe: CWE_IDS,
    onClickCard: (CharSequence) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, onClick = { onClickCard(cwe.id) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(72.dp)
        ) {
            Text(
                stringResource(cwe.description),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

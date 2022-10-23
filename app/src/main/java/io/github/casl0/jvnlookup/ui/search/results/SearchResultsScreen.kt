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

package io.github.casl0.jvnlookup.ui.search.results

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.ui.components.VulnCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    viewModel: SearchResultsViewModel,
    upPress: () -> Unit,
    onClickVulnOverviewItem: (CharSequence) -> Unit
) {
    val searchResults = viewModel.searchResults.collectAsState(listOf())
    val favorites = viewModel.favorites.collectAsState(listOf())
    val scrollState = rememberLazyListState()
    Scaffold(topBar = {
        SearchResultsAppBar(title = R.string.search_results, upPress = upPress)
    }) { innerPaddingModifier ->
        LazyColumn(
            state = scrollState,
            contentPadding = innerPaddingModifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = searchResults.value, key = { item -> item.id }) { vulnOverview ->
                // お気に入り登録状態を反映
                vulnOverview.isFavorite =
                    favorites.value.find { it.id == vulnOverview.id } != null
                Surface(modifier = Modifier.padding(horizontal = 4.dp)) {
                    VulnCard(
                        vulnOverview = vulnOverview,
                        onItemClicked = onClickVulnOverviewItem,
                        onFavoriteButtonClicked = viewModel::onFavoriteButtonClicked
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultsAppBar(@StringRes title: Int, upPress: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = title),
            )
        },
        navigationIcon = {
            IconButton(onClick = upPress) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    )
}

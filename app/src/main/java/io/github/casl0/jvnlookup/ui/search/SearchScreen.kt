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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val searchResults = viewModel.searchResult.observeAsState(listOf())
    val searchValue = viewModel.searchValue
    Scaffold {
        val scrollState = rememberLazyListState()
        LazyColumn(
            state = scrollState,
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                SearchTextField(
                    searchValue = searchValue,
                    onValueChange = viewModel::onSearchValueChanged,
                    onSearch = viewModel::searchOnJvn,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(items = searchResults.value, key = { item -> item.id }) { vulnOverview ->
                Surface(modifier = Modifier.padding(horizontal = 4.dp)) {
                    SearchItem(
                        vulnOverview = vulnOverview,
                        onItemClicked = viewModel::onItemClicked
                    )
                }
            }
        }
    }
}
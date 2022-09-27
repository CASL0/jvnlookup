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

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        divider = {}, // Dividerを無効化
        edgePadding = categoryChipsEdgePadding,
        indicator = emptyTabIndicator,
        modifier = modifier
    ) {
        categories.forEachIndexed { index, category ->
            FilterChip(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) },
                label = { Text(stringResource(category.name)) },
                leadingIcon = if (index == selectedIndex) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(category.name),
                        )
                    }
                } else {
                    null
                },
                modifier = Modifier.padding(horizontal = 4.dp),
                shape = MaterialTheme.shapes.large
            )
        }
    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}
private val categoryChipsEdgePadding = 2.dp

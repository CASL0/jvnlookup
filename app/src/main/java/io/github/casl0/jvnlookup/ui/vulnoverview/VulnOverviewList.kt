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

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.model.DomainVulnOverview

@Composable
fun VulnOverviewList(
    vulnOverviews: List<DomainVulnOverview>,
    onItemClicked: (Context, CharSequence) -> Unit,
    onFavoriteButtonClicked: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = vulnOverviews,
            key = { vulnOverview -> vulnOverview.id }
        ) { vulnOverview ->
            VulnOverviewItem(vulnOverview, onItemClicked, onFavoriteButtonClicked, modifier)
        }
    }
}
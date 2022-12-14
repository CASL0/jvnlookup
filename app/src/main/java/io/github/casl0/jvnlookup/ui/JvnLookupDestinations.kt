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

package io.github.casl0.jvnlookup.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.casl0.jvnlookup.R

interface JvnLookupDestination {
    val icon: ImageVector
    val label: Int
    val route: String
}

object VulnOverview : JvnLookupDestination {
    override val icon = Icons.Filled.Home
    override val label = R.string.vuln_overview_label
    override val route = "vuln_overview"
}

object Search : JvnLookupDestination {
    override val icon = Icons.Filled.Search
    override val label = R.string.search_label
    override val route = "search"
}

object Settings : JvnLookupDestination {
    override val icon = Icons.Filled.Settings
    override val label = R.string.settings_label
    override val route = "settings"
}

object SearchResults : JvnLookupDestination {
    override val icon: ImageVector
        get() = TODO("Not yet implemented")
    override val label: Int
        get() = TODO("Not yet implemented")
    override val route = "search/results"
}

val jvnLookupBottomNavBarScreens = listOf(VulnOverview, Search, Settings)
val jvnLookupBottomNavBarScreenRoutes = jvnLookupBottomNavBarScreens.map { it.route }

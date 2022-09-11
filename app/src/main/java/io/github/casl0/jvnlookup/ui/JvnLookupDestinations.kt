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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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

object Favorite : JvnLookupDestination {
    override val icon = Icons.Filled.Favorite
    override val label = R.string.favorite_label
    override val route = "favorite"
}

val jvnLookupBottomNavBarScreens = listOf(VulnOverview, Favorite)
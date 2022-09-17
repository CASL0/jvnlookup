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

package io.github.casl0.jvnlookup.model

import androidx.annotation.StringRes
import io.github.casl0.jvnlookup.R

data class Category(
    var id: String,
    @StringRes var name: Int,
)

val categoryAll = Category(id = "All", name = R.string.category_all)
val categoryFavorite = Category(id = "Favorite", name = R.string.category_favorite)
val categorySeverityCritical =
    Category(id = "SeverityCritical", name = R.string.category_severity_critical)
val categorySeverityHigh = Category(id = "SeverityHigh", name = R.string.category_severity_high)
val categorySeverityMiddle =
    Category(id = "SeverityMiddle", name = R.string.category_severity_middle)

val filterCategories =
    listOf(
        categoryAll,
        categoryFavorite,
        categorySeverityCritical,
        categorySeverityHigh,
        categorySeverityMiddle
    )
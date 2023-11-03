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

/** JVNカテゴリ */
sealed class Category(
    /** カテゴリ識別子 */
    val id: String,

    /** 表示文言 */
    @StringRes val name: Int,
) {
    /** すべて */
    object All : Category(id = "All", name = R.string.category_all)

    /** お気に入り */
    object Favorite : Category(id = "Favorite", name = R.string.category_favorite)

    /** 深刻度：緊急 */
    object SeverityCritical :
        Category(id = "SeverityCritical", name = R.string.category_severity_critical)

    /** 深刻度：重要 */
    object SeverityHigh : Category(id = "SeverityHigh", name = R.string.category_severity_high)

    /** 深刻度：警告 */
    object SeverityMiddle :
        Category(id = "SeverityMiddle", name = R.string.category_severity_middle)
}

/** カテゴリリスト */
val filterCategories =
    listOf(
        Category.All,
        Category.Favorite,
        Category.SeverityCritical,
        Category.SeverityHigh,
        Category.SeverityMiddle
    )

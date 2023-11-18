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

package io.github.casl0.jvnlookup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.model.DomainVulnOverview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VulnCard(
    vulnOverview: DomainVulnOverview,
    onItemClicked: (CharSequence) -> Unit,
    onFavoriteButtonClicked: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography
    Card(onClick = { onItemClicked(vulnOverview.link ?: "") }) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            val (title, description) = listOf(vulnOverview.title, vulnOverview.description)
            if (title != null && description != null) {
                // タイトル
                Text(
                    text = title,
                    style = typography.titleMedium,
                )

                Spacer(modifier.height(8.dp))

                // JVN ID
                Text(text = vulnOverview.id, style = typography.bodyMedium)

                // 発行日
                Text(
                    text = stringResource(
                        R.string.overview_issued_label,
                        vulnOverview.issued
                    ),
                    style = typography.bodyMedium
                )

                // CVSS
                vulnOverview.cvssList.forEach { cvss ->
                    val (cvssVersion, cvssScore) = listOf(cvss.version, cvss.score)
                    if (cvssVersion != null && cvssScore != null) {
                        val evaluation =
                            stringResource(R.string.overview_cvss_label, cvssVersion, cvssScore)
                        Text(text = evaluation, style = typography.bodyMedium)
                    }
                }
                val dividerColor = LocalContentColor.current
                Divider(
                    modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
                    thickness = 1.dp,
                    color = dividerColor
                )

                // 概要
                Text(text = description, style = typography.bodyMedium)

                Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    val isFavorite = vulnOverview.isFavorite
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onClick = {
                            onFavoriteButtonClicked(
                                vulnOverview.id,
                                !vulnOverview.isFavorite
                            )
                        },
                        modifier = Modifier.height(48.dp)
                    )
                }
            }
        }
    }
}

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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.R
import io.github.casl0.jvnlookup.model.DomainVulnOverview

@Composable
fun VulnOverviewItem(vulnOverview: DomainVulnOverview, modifier: Modifier = Modifier) {
    val typography = MaterialTheme.typography
    Card {
        Column(modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(8.dp)) {
            // タイトル
            Text(text = vulnOverview.title!!, style = typography.titleMedium)
            Spacer(modifier.height(8.dp))

            // JVN ID
            Text(text = vulnOverview.id, style = typography.bodyMedium)

            // 発行日
            vulnOverview.issued?.let {
                Text(
                    text =
                    stringResource(
                        R.string.overview_issued_label,
                        it
                    ), style = typography.bodyMedium
                )
            }

            // CVSS
            vulnOverview.cvssList.forEach { cvss ->
                val evaluation =
                    stringResource(R.string.overview_cvss_label, cvss.version!!, cvss.score!!)
                Text(text = evaluation, style = typography.bodyMedium)
            }
            val dividerColor = LocalContentColor.current
            Divider(
                modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
                thickness = 1.dp,
                color = dividerColor
            )

            // 概要
            Text(text = vulnOverview.description!!, style = typography.bodyMedium)
        }
    }
}

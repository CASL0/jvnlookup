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

package io.github.casl0.jvnlookup.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.casl0.jvnlookup.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onClickOssLicenses: () -> Unit) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            AppVersion()
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            SettingItem(
                Icons.Filled.List,
                R.string.open_source_licenses,
                onClickOssLicenses
            )
        }
    }
}
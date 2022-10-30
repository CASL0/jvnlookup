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

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.casl0.jvnlookup.ui.theme.JVNlookupTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JvnLookupApp(appState: JvnLookupAppState = rememberJvnLookupAppState()) {
    JVNlookupTheme {
        Scaffold(bottomBar = {
            if (appState.shouldShowBottomBar) {
                NavigationBar {
                    jvnLookupBottomNavBarScreens.forEachIndexed { _, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = stringResource(id = item.label)
                                )
                            },
                            label = { Text(stringResource(item.label)) },
                            selected = item.route == appState.currentScreen?.route,
                            onClick = { appState.navController.navigateSingleTopTo(item.route) }
                        )
                    }
                }
            }
        }) { innerPadding ->
            JvnLookupNavHost(
                navController = appState.navController,
                snackbarHostState = appState.snackbarHostState,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

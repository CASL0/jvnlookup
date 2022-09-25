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

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.github.casl0.jvnlookup.JvnLookupApplication
import io.github.casl0.jvnlookup.domain.FavoriteVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.FetchVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.SearchVulnOverviewUseCase
import io.github.casl0.jvnlookup.ui.search.SearchScreen
import io.github.casl0.jvnlookup.ui.search.SearchViewModel
import io.github.casl0.jvnlookup.ui.settings.SettingScreen
import io.github.casl0.jvnlookup.ui.vulnoverview.VulnOverviewScreen
import io.github.casl0.jvnlookup.ui.vulnoverview.VulnOverviewViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun JvnLookupNavHost(
    application: JvnLookupApplication,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = VulnOverview.route,
        modifier = modifier
    ) {
        composable(route = VulnOverview.route) {
            val vulnOverviewViewModel: VulnOverviewViewModel =
                viewModel(
                    viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
                    key = "VulnOverviewViewModel",
                    VulnOverviewViewModel.provideFactory(
                        FetchVulnOverviewUseCase(application.jvnRepository, Dispatchers.IO),
                        FavoriteVulnOverviewUseCase(application.jvnRepository, Dispatchers.IO)
                    )
                )
            VulnOverviewScreen(vulnOverviewViewModel, navController::navigationUrlInCustomTabs)
        }
        composable(route = Search.route) {
            val searchViewModel: SearchViewModel = viewModel(
                viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
                key = "SearchViewModel",
                SearchViewModel.provideFactory(
                    SearchVulnOverviewUseCase(
                        application.searchRepository,
                        Dispatchers.IO
                    )
                )
            )
            SearchScreen(searchViewModel, navController::navigationUrlInCustomTabs)
        }
        composable(route = Settings.route) {
            SettingScreen(navController::navigationOssLicensesActivity)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

/**
 * Chrome Custom Tabs によるナビゲーションをします
 * @param url URL文字列
 */
fun NavHostController.navigationUrlInCustomTabs(url: CharSequence) {
    CustomTabsIntent.Builder().build().run {
        launchUrl(context, Uri.parse(url as String?))
    }
}

/**
 * OSS Licenses Activity に遷移します
 */
fun NavHostController.navigationOssLicensesActivity() {
    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
}
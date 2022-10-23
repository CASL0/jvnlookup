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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.github.casl0.jvnlookup.JvnLookupApplication
import io.github.casl0.jvnlookup.ui.search.SearchScreen
import io.github.casl0.jvnlookup.ui.search.results.SearchResultsScreen
import io.github.casl0.jvnlookup.ui.settings.SettingScreen
import io.github.casl0.jvnlookup.ui.vulnoverview.VulnOverviewScreen
import timber.log.Timber

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
            VulnOverviewScreen(hiltViewModel(), navController::navigationUrlInCustomTabs)
        }
        composable(route = Search.route) {
            SearchScreen(
                hiltViewModel(),
                navController::navigateToSearchResults,
            )
        }
        composable(route = Settings.route) {
            SettingScreen(
                navController::navigationOssLicensesActivity,
                navController::navigationDeepLink
            )
        }
        composable(route = SearchResults.route) {
            SearchResultsScreen(
                hiltViewModel(),
                navController::navigateUp,
                navController::navigationUrlInCustomTabs
            )
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

/**
 * 外部のアプリコンテンツにアクセスします
 * @param url アクセス先のカスタムURL
 */
fun NavHostController.navigationDeepLink(url: CharSequence) {
    try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(url as String)
        }.run {
            context.startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        Timber.e(e.localizedMessage)
    }
}

/**
 * 検索結果画面に遷移します
 */
fun NavHostController.navigateToSearchResults() {
    navigate(SearchResults.route)
}

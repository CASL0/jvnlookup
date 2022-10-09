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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import io.github.casl0.jvnlookup.JvnLookupApplication
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * アプリ内アップデート制御
     */
    private lateinit var appUpdateManager: AppUpdateManager

    /**
     * アプリ内アップデートコールバック
     */
    private val updateFlowResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> Timber.d("update ok")
                RESULT_CANCELED -> Timber.d("update canceled")
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> Timber.d("update failed")
            }
        }

    /**
     * アプリ内アップデートの実行
     */
    private fun requestAppUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val starter =
                IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
                    val request = IntentSenderRequest.Builder(intent)
                        .setFillInIntent(fillInIntent)
                        .setFlags(flagsValues, flagsMask)
                        .build()

                    updateFlowResultLauncher.launch(request)
                }
            when {
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> {
                    Timber.d("start app update")
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        starter,
                        1,
                    )
                }
                appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    // アップデートが停止している場合は、更新を再開します
                    Timber.d("resume app update")
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        starter,
                        1,
                    )
                }
            }
        }
    }

    // ライフサイクルメソッド
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val jvnLookupApplication = application as JvnLookupApplication
        setContent {
            JvnLookupApp(jvnLookupApplication)
        }
    }

    override fun onResume() {
        super.onResume()
        requestAppUpdate()
    }
}

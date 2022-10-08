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

package io.github.casl0.jvnlookup

import android.app.Application
import io.github.casl0.jvnlookup.data.local.JvnLocalDataSource
import io.github.casl0.jvnlookup.database.getDatabase
import io.github.casl0.jvnlookup.repository.JvnRepository
import io.github.casl0.jvnlookup.repository.SearchRepository
import timber.log.Timber

class JvnLookupApplication : Application() {

    /**
     * JVN APIから取得した情報のリポジトリ
     */
    val jvnRepository by lazy { JvnRepository(JvnLocalDataSource(getDatabase(this))) }

    /**
     * JVN API キーワード検索のリポジトリ
     */
    val searchRepository by lazy { SearchRepository() }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

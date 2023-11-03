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

package io.github.casl0.jvnlookup.database

import androidx.room.Database
import androidx.room.RoomDatabase

/** JVN APIで取得した情報を保持するRoom */
@Database(
    entities = [DatabaseVulnOverview::class, DatabaseReference::class, DatabaseCVSS::class],
    version = 1,
    exportSchema = true
)
abstract class JvnDatabase : RoomDatabase() {
    abstract val vulnOverviewDao: VulnOverviewDao
    abstract val referenceDao: ReferenceDao
    abstract val cvssDao: CVSSDao
}

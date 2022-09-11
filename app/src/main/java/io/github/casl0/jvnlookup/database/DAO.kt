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

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VulnOverviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vulnOverviews: List<DatabaseVulnOverview>)

    @Transaction
    @Query("SELECT * FROM vuln_overview ORDER BY issued DESC")
    fun getVulnOverviewWithReferencesAndCVSS(): LiveData<List<VulnOverviewWithReferencesAndCVSS>>

    @Query("DELETE FROM vuln_overview")
    fun deleteAll()
}

@Dao
interface ReferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(references: List<DatabaseReference>)

    @Query("DELETE FROM sec_references")
    fun deleteAll()
}

@Dao
interface CVSSDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cvssList: List<DatabaseCVSS>)

    @Query("DELETE FROM sec_cvss")
    fun deleteAll()
}
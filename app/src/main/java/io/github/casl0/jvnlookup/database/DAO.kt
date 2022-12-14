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

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * vuln_overviewテーブル操作用クエリ
 */
@Dao
interface VulnOverviewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vulnOverviews: List<DatabaseVulnOverview>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vulnOverview: DatabaseVulnOverview)

    @Transaction
    @Query("SELECT * FROM vuln_overview ORDER BY issued DESC")
    fun getVulnOverviewWithReferencesAndCVSS(): Flow<List<VulnOverviewWithReferencesAndCVSS>>

    @Transaction
    @Query("SELECT * FROM vuln_overview WHERE favorite = 1")
    fun getFavorites(): Flow<List<VulnOverviewWithReferencesAndCVSS>>

    @Query("SELECT EXISTS(SELECT * FROM vuln_overview WHERE sec_identifier = :id)")
    fun exists(id: String): Boolean

    @Query("DELETE FROM vuln_overview")
    fun deleteAll()

    @Query("DELETE FROM vuln_overview WHERE favorite = 0")
    fun deleteAllNonFavorite()

    @Query("UPDATE vuln_overview SET favorite = :favorite WHERE sec_identifier = :id")
    fun updateFavoriteById(id: String, favorite: Boolean)
}

/**
 * sec_referencesテーブル操作用クエリ
 */
@Dao
interface ReferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(references: List<DatabaseReference>)

    @Query("DELETE FROM sec_references")
    fun deleteAll()
}

/**
 * sec_cvssテーブル操作用クエリ
 */
@Dao
interface CVSSDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cvssList: List<DatabaseCVSS>)

    @Query("DELETE FROM sec_cvss")
    fun deleteAll()
}

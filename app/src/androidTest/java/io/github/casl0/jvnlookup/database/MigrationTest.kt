/*
 * Copyright 2022 CASL0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.casl0.jvnlookup.database

import android.database.SQLException
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class MigrationTest {
    private val TEST_DB = "migration-test.db"

    /** 初期データ投入用のSQL */
    private val SEED_SQL = """
        INSERT INTO vuln_overview 
            (
                title,
                link,
                description,
                sec_identifier,
                issued,
                modified,
                favorite
            ) 
        VALUES 
            (
                'Google の Android における境界外書き込みに関する脆弱性',
                'https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-020636.html',
                'Google の Android には、境界外書き込みに関する脆弱性が存在します。',
                'JVNDB-2022-020636',
                '2023-11-02T17:07:02+09:00',
                '2023-11-02T17:07:02+09:00',
                0
            )
        ;
    """.trimIndent()

    /** テスト対象のマイグレ */
    private val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2,
    )

    /** スキーマJSONファイルからDBを作成します */
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        JvnDatabase::class.java
    )

    @Before
    fun setup() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(SEED_SQL)
            close()
        }
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun migrateAll() {
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            JvnDatabase::class.java,
            TEST_DB
        ).addMigrations(*ALL_MIGRATIONS).build().apply {
            openHelper.writableDatabase.close()
        }
    }
}

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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.datetime.toInstant
import timber.log.Timber

/**
 * issuedとmodifiedのカラム定義変更のマイグレ
 *
 * TEXTで保持していたのをエポック秒（Long）に修正
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    @Throws(SQLException::class)
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE new_vuln_overview (
                    title TEXT,
                    link TEXT,
                    description TEXT,
                    sec_identifier TEXT PRIMARY KEY NOT NULL,
                    issued INTEGER NOT NULL,
                    modified INTEGER NOT NULL,
                    favorite INTEGER NOT NULL
                )
                ;
            """.trimIndent()
        )

        db.query("SELECT * FROM vuln_overview;").also { cursor ->
            while (cursor.moveToNext()) {
                val columnIndex = hashMapOf(
                    "title" to cursor.getColumnIndex("title"),
                    "link" to cursor.getColumnIndex("link"),
                    "description" to cursor.getColumnIndex("description"),
                    "sec_identifier" to cursor.getColumnIndex("sec_identifier"),
                    "issued" to cursor.getColumnIndex("issued"),
                    "modified" to cursor.getColumnIndex("modified"),
                    "favorite" to cursor.getColumnIndex("favorite"),
                )
                val title =
                    columnIndex["title"]?.let { cursor.getString(it) } ?: continue
                val link = columnIndex["link"]?.let { cursor.getString(it) } ?: continue
                val description =
                    columnIndex["description"]?.let { cursor.getString(it) } ?: continue
                val secIdentifier =
                    columnIndex["sec_identifier"]?.let { cursor.getString(it) } ?: continue
                val issued = columnIndex["issued"]?.let { cursor.getString(it) } ?: continue
                val modified = columnIndex["modified"]?.let { cursor.getString(it) } ?: continue
                val favorite = columnIndex["favorite"]?.let { cursor.getInt(it) } ?: continue


                try {
                    val issuedInstant = issued.toInstant()
                    val modifiedInstant = modified.toInstant()

                    db.execSQL(
                        """
                        INSERT INTO new_vuln_overview 
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
                                '$title',
                                '$link',
                                '$description',
                                '$secIdentifier',
                                ${issuedInstant.toEpochMilliseconds()},
                                ${modifiedInstant.toEpochMilliseconds()},
                                $favorite
                            )
                        ;
                        """.trimIndent()
                    )
                } catch (e: IllegalArgumentException) {
                    Timber.e("Instant.parse failed: $e")
                }
            }
        }

        db.execSQL("DROP TABLE vuln_overview;")
        db.execSQL("ALTER TABLE new_vuln_overview RENAME TO vuln_overview;")
    }
}

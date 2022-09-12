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
import io.github.casl0.jvnlookup.model.DomainCVSS
import io.github.casl0.jvnlookup.model.DomainReference
import io.github.casl0.jvnlookup.model.DomainVulnOverview

data class VulnOverviewWithReferencesAndCVSS(
    @Embedded val vulnOverview: DatabaseVulnOverview,

    @Relation(
        parentColumn = "sec_identifier",
        entityColumn = "sec_owner_identifier"
    )
    val references: List<DatabaseReference>,

    @Relation(
        parentColumn = "sec_identifier",
        entityColumn = "sec_owner_identifier"
    )
    val cvssList: List<DatabaseCVSS>,
)

@Entity(tableName = "vuln_overview")
data class DatabaseVulnOverview(
    /**
     * セキュリティ情報のタイトル
     */
    @ColumnInfo(name = "title")
    val title: String?,

    /**
     * セキュリティ情報のURI
     */
    @ColumnInfo(name = "link")
    val link: String?,

    /**
     * セキュリティ情報の概要
     */
    @ColumnInfo(name = "description")
    val description: String?,

    /**
     * ベンダ固有のセキュリティ情報ID
     */
    @PrimaryKey @ColumnInfo(name = "sec_identifier")
    val id: String,

    /**
     * 発行日
     */
    @ColumnInfo(name = "issued")
    val issued: String?,

    /**
     * 更新日
     */
    @ColumnInfo(name = "modified")
    val modified: String?,

    /**
     * お気に入り登録済み
     */
    @ColumnInfo(name = "favorited")
    val isFavorited: Boolean,
)

@Entity(tableName = "sec_references")
data class DatabaseReference(
    /**
     * 参照情報に対応するセキュリティ情報ID
     */
    @ColumnInfo(name = "sec_owner_identifier")
    val ownerId: String?,

    /**
     * 発行元省略名
     */
    @ColumnInfo(name = "source")
    val source: String?,

    /**
     * 識別番号
     */
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,

    /**
     * タイトル
     */
    @ColumnInfo(name = "title")
    val title: String?,

    /**
     * 関連情報
     */
    @ColumnInfo(name = "url")
    val url: String = "",
)

@Entity(tableName = "sec_cvss")
data class DatabaseCVSS(
    /**
     * CVSS評価値に対応するセキュリティ情報ID
     */
    @ColumnInfo(name = "sec_owner_identifier")
    val ownerId: String?,

    /**
     * CVSSバージョン
     */
    @ColumnInfo(name = "version")
    val version: String?,

    /**
     * CVSS基準(基本|現状|環境評価基準)
     */
    @ColumnInfo(name = "type")
    val type: String?,

    /**
     * typeで指定された評価基準の深刻度
     * n:なし、l:注意、m:警告、h:重要、c:緊急
     */
    @ColumnInfo(name = "severity")
    val severity: String?,

    /**
     * typeで指定された評価基準の評価値
     */
    @ColumnInfo(name = "score")
    val score: String?,

    /**
     * 短縮表記
     */
    @ColumnInfo(name = "vector")
    val vector: String?,

    @PrimaryKey(autoGenerate = true)
    val id: Int,
)

fun List<VulnOverviewWithReferencesAndCVSS>.asDomainModel(): List<DomainVulnOverview> {
    return map {
        DomainVulnOverview(
            title = it.vulnOverview.title,
            link = it.vulnOverview.link,
            description = it.vulnOverview.description,
            id = it.vulnOverview.id,
            references = it.references.asDomainModel(),
            cvssList = it.cvssList.asDomainModel(),
            issued = it.vulnOverview.issued,
            modified = it.vulnOverview.modified,
            isFavorited = it.vulnOverview.isFavorited,
        )
    }
}

/**
 * 脆弱性参照情報データベースエンティティをドメインモデルに変換します
 */
@JvmName("asDomainModelDomainReference")
fun List<DatabaseReference>.asDomainModel(): List<DomainReference> {
    return map {
        DomainReference(
            source = it.source,
            id = it.id,
            title = it.title,
            url = it.url,
        )
    }
}

/**
 * CVSSデータベースエンティティをドメインモデルに変換します
 */
@JvmName("asDomainModelDatabaseCVSS")
fun List<DatabaseCVSS>.asDomainModel(): List<DomainCVSS> {
    return map {
        DomainCVSS(
            version = it.version,
            type = it.type,
            severity = it.severity,
            score = it.score,
            vector = it.vector
        )
    }
}


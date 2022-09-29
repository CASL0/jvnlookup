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

package io.github.casl0.jvnlookup.model

import io.github.casl0.jvnlookup.database.DatabaseCVSS
import io.github.casl0.jvnlookup.database.DatabaseReference
import io.github.casl0.jvnlookup.database.DatabaseVulnOverview

/**
 * 脆弱性対策情報のドメインモデル
 */
data class DomainVulnOverview(
    /**
     * セキュリティ情報のタイトル
     */
    var title: String?,

    /**
     * セキュリティ情報のURI
     */
    var link: String?,

    /**
     * セキュリティ情報の概要
     */
    var description: String?,

    /**
     * ベンダ固有のセキュリティ情報ID
     */
    var id: String,

    /**
     * 参考情報
     */
    val references: List<DomainReference> = emptyList(),

    /**
     * CVSS評価情報
     */
    var cvssList: List<DomainCVSS> = emptyList(),

    /**
     * 発行日
     */
    var issued: String?,

    /**
     * 更新日
     */
    var modified: String?,

    /**
     * お気に入り登録済み
     */
    var isFavorite: Boolean = false,
)

/**
 * 脆弱性対策情の参考情報のドメインモデル
 */
data class DomainReference(
    /**
     * 発行元省略名
     */
    val source: String?,

    /**
     * 識別番号
     */
    val id: String,

    /**
     * タイトル
     */
    val title: String?,

    /**
     * 関連情報
     */
    val url: String = "",
)

/**
 * CVSS情報のドメインモデル
 */
data class DomainCVSS(
    /**
     * CVSSバージョン
     */
    var version: String?,

    /**
     * CVSS基準(基本|現状|環境評価基準)
     */
    var type: String?,

    /**
     * typeで指定された評価基準の深刻度
     * n:なし、l:注意、m:警告、h:重要、c:緊急
     */
    var severity: String?,

    /**
     * typeで指定された評価基準の評価値
     */
    var score: String?,

    /**
     * 短縮表記
     */
    var vector: String?,
)

/**
 * 脆弱性対策情報のドメインモデルをデータベースエンティティへ変換します
 */
fun DomainVulnOverview.asDatabaseEntity(): DatabaseVulnOverview {
    return DatabaseVulnOverview(
        title = this.title,
        link = this.link,
        description = this.description,
        id = this.id,
        issued = this.issued,
        modified = this.modified,
        isFavorite = this.isFavorite
    )
}

/**
 * 脆弱性対策情報の参考情報のドメインモデルをデータベースエンティティへ変換します
 * @param ownerId 参考情報が紐づくベンダ固有のセキュリティID
 */
@JvmName("asDatabaseEntityDomainReference")
fun List<DomainReference>.asDatabaseEntity(ownerId: String): List<DatabaseReference> {
    return map {
        DatabaseReference(
            ownerId = ownerId,
            source = it.source,
            id = it.id,
            title = it.title,
            url = it.url
        )
    }
}

/**
 * CVSS情報のドメインモデルをデータベースエンティティへ変換します
 * @param ownerId CVSS情報が紐づくベンダ固有のセキュリティID
 */
@JvmName("asDatabaseEntityDomainCVSS")
fun List<DomainCVSS>.asDatabaseEntity(ownerId: String): List<DatabaseCVSS> {
    return map {
        DatabaseCVSS(
            ownerId = ownerId,
            version = it.version,
            type = it.type,
            severity = it.severity,
            score = it.score,
            vector = it.vector,
        )
    }
}

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

package io.github.casl0.jvnlookup.network

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import io.github.casl0.jvnlookup.database.DatabaseCVSS
import io.github.casl0.jvnlookup.database.DatabaseReference
import io.github.casl0.jvnlookup.database.DatabaseVulnOverview
import io.github.casl0.jvnlookup.model.DomainCVSS
import io.github.casl0.jvnlookup.model.DomainReference
import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.datetime.toInstant

/** JVN API レスポンス */
@Xml(name = "rdf:RDF")
data class VulnOverviewResponse(
    @Element(name = "item")
    var vulnOverviews: List<VulnOverview> = emptyList()
)

/** JVN API レスポンスのitemプロパティ */
@Xml(name = "item")
data class VulnOverview(
    /** セキュリティ情報のタイトル */
    @PropertyElement(name = "title")
    var title: String?,

    /** セキュリティ情報のURI */
    @PropertyElement(name = "link")
    var link: String?,

    /** セキュリティ情報の概要 */
    @PropertyElement(name = "description")
    var description: String?,

    /** ベンダ固有のセキュリティ情報ID */
    @PropertyElement(name = "sec:identifier")
    var id: String?,

    /** 参考情報 */
    @Element(name = "sec:references")
    var references: List<Reference>?,

    /** CVSS評価情報 */
    @Element(name = "sec:cvss")
    var cvssList: List<CVSS>?,

    /** 発行日 */
    @PropertyElement(name = "dcterms:issued")
    var issued: String?,

    /** 更新日 */
    @PropertyElement(name = "dcterms:modified")
    var modified: String?,
)

/** JVN API レスポンスのsec:referencesプロパティ */
@Xml(name = "sec:references")
data class Reference(
    /** 発行元省略名 */
    @Attribute(name = "source")
    val source: String?,

    /** 識別番号 */
    @Attribute(name = "id")
    val id: String,

    /** タイトル */
    @Attribute(name = "title")
    val title: String?,

    /** 関連情報 */
    @TextContent
    val url: String?,
)

/** JVN API レスポンスのsec:cvssプロパティ */
@Xml(name = "sec:cvss")
data class CVSS(
    /** CVSSバージョン */
    @Attribute(name = "version")
    var version: String?,

    /** CVSS基準(基本|現状|環境評価基準) */
    @Attribute(name = "type")
    var type: String?,

    /**
     * typeで指定された評価基準の深刻度
     *
     * n:なし、l:注意、m:警告、h:重要、c:緊急
     */
    @Attribute(name = "severity")
    var severity: String?,

    /** typeで指定された評価基準の評価値 */
    @Attribute(name = "score")
    var score: String?,

    /** 短縮表記 */
    @Attribute(name = "vector")
    var vector: String?,
)

/**
 * VulnOverviewをデータベースエンティティに変換します
 *
 * @throws [IllegalArgumentException]
 */
@Throws(IllegalArgumentException::class)
fun VulnOverviewResponse.asDatabaseVulnOverviews(): List<DatabaseVulnOverview> {
    return vulnOverviews
        .map {
            val (issued, modified) = Pair(it.issued, it.modified)
            return@map if (issued != null && modified != null) {
                DatabaseVulnOverview(
                    title = it.title,
                    link = it.link,
                    description = it.description,
                    id = it.id ?: "",
                    issued = issued.toInstant(),
                    modified = modified.toInstant(),
                    isFavorite = false,
                )
            } else {
                null
            }
        }.filterNotNull()
}

/** List<Reference>をデータベースエンティティに変換します */
fun VulnOverviewResponse.asDatabaseReferences(): List<DatabaseReference> {
    val databaseReferences = mutableListOf<DatabaseReference>()
    vulnOverviews.forEach { vulnOverview ->
        vulnOverview.references?.map {
            DatabaseReference(
                ownerId = vulnOverview.id,
                source = it.source,
                id = it.id,
                title = it.title,
                url = it.url ?: "",
            )
        }?.also {
            databaseReferences.addAll(it)
        }
    }
    return databaseReferences
}

/** List<CVSS>をデータベースエンティティに変換します */
fun VulnOverviewResponse.asDatabaseCVSS(): List<DatabaseCVSS> {
    val databaseCVSS = mutableListOf<DatabaseCVSS>()
    vulnOverviews.forEach { vulnOverview ->
        vulnOverview.cvssList?.map {
            DatabaseCVSS(
                ownerId = vulnOverview.id,
                version = it.version,
                type = it.type,
                severity = it.severity,
                score = it.score,
                vector = it.vector,
                id = 0,
            )
        }?.also {
            databaseCVSS.addAll(it)
        }
    }
    return databaseCVSS
}

/**
 * JVN APIのレスポンスをドメインモデルに変換します
 *
 * @throws [IllegalArgumentException]
 */
@Throws(IllegalArgumentException::class)
fun VulnOverviewResponse.asDomainModel(): List<DomainVulnOverview> {
    return vulnOverviews
        .map {
            val (id, issued, modified) = Triple(it.id, it.issued, it.modified)
            return@map if (id != null && issued != null && modified != null) {
                DomainVulnOverview(
                    title = it.title,
                    link = it.link,
                    description = it.description,
                    id = id,
                    references = it.references?.asDomainModel() ?: listOf(),
                    cvssList = it.cvssList?.asDomainModel() ?: listOf(),
                    issued = issued.toInstant(),
                    modified = modified.toInstant(),
                )
            } else {
                null
            }
        }.filterNotNull()
}

/** JVN参照情報のネットワークモデルをドメインモデルに変換します */
@JvmName("asDomainModelReference")
fun List<Reference>.asDomainModel(): List<DomainReference> {
    return map {
        DomainReference(
            source = it.source,
            id = it.id,
            title = it.title,
            url = it.url ?: ""
        )
    }
}

/** CVSSのネットワークモデルをドメインモデルに変換します */
@JvmName("asDomainModelCVSS")
fun List<CVSS>.asDomainModel(): List<DomainCVSS> {
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

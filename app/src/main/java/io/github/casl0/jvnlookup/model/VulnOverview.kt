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

data class DomainReference(
    /**
     * 発行元省略名
     */
    val source: String?,

    /**
     * 識別番号
     */
    val id: String?,

    /**
     * タイトル
     */
    val title: String?,

    /**
     * 関連情報
     */
    val url: String = "",
)

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
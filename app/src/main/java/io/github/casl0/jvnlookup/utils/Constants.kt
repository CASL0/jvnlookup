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

package io.github.casl0.jvnlookup.utils

import androidx.annotation.StringRes
import io.github.casl0.jvnlookup.R

internal const val SOURCE_CODE_URL = "https://github.com/CASL0/jvnlookup"
internal const val MYJVN_API_BASE_URL = "https://jvndb.jvn.jp/"

internal enum class CWE_IDS(
    val id: CharSequence, @StringRes val description: Int
) {
    CWE_16("CWE-16", R.string.cwe_16), // 環境設定
    CWE_20("CWE-20", R.string.cwe_20), // 不適切な入力確認
    CWE_22("CWE-22", R.string.cwe_22), // パス・トラバーサル
    CWE_59("CWE-59", R.string.cwe_59), // リンク解釈の問題
    CWE_78("CWE-78", R.string.cwe_78), // OSコマンドインジェクション
    CWE_79("CWE-79", R.string.cwe_79), // クロスサイトスクリプティング
    CWE_89("CWE-89", R.string.cwe_89), // SQLインジェクション
    CWE_94("CWE-94", R.string.cwe_94), // コード・インジェクション
    CWE_119("CWE-119", R.string.cwe_119), // バッファエラー
    CWE_134("CWE-134", R.string.cwe_134), // 書式文字列の問題
    CWE_189("CWE-189", R.string.cwe_189), // 数値処理の問題
    CWE_200("CWE-200", R.string.cwe_200), // 情報漏えい
    CWE_255("CWE-255", R.string.cwe_255), // 証明書・パスワードの管理
    CWE_264("CWE-264", R.string.cwe_264), // 認可・権限・アクセス制御
    CWE_287("CWE-287", R.string.cwe_287), // 不適切な認証
    CWE_310("CWE-310", R.string.cwe_310), // 暗号の問題
    CWE_352("CWE-352", R.string.cwe_352), // クロスサイトリクエストフォージェリ
    CWE_362("CWE-362", R.string.cwe_362), // 競合状態
    CWE_399("CWE-399", R.string.cwe_399), // リソース管理の問題
    CWE_DesignError("CWE-DesignError", R.string.cwe_designerror), // 設計上の問題
}

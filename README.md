# JVNlookup

[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![codecov](https://codecov.io/github/CASL0/jvnlookup/graph/badge.svg?token=FHGIH87WJ2)](https://codecov.io/github/CASL0/jvnlookup)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[MyJVN API](https://jvndb.jvn.jp/apis/index.html) を使用した脆弱性対策情報確認アプリです。

Japan Vulnerability Notes (JVN) は脆弱性関連情報とその対策情報を Web
API を介して提供しています。本アプリは当該 API を使用し脆弱性対策情報をブラウズ・検索できるアプリです。

対象 OS：Android 6+

<a href='https://play.google.com/store/apps/details?id=io.github.casl0.jvnlookup'>
    <img alt='Get it on Google Play' height="80" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

## 開発

### 開発環境

-   Kotlin 1.8.10
-   Android Gradle Plugin 8.1.2
-   Gradle 8.4
-   JDK 17

### アプリアーキテクチャ

MVVM + Repository パターンで実装しています。

![アプリアーキテクチャ](https://user-images.githubusercontent.com/28913760/194712660-3664ecef-f183-41cc-8afd-2a4080690f2e.svg)

## ドキュメント

-   [KDoc](https://casl0.github.io/jvnlookup/)

## ライセンス

```
Copyright 2022 CASL0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

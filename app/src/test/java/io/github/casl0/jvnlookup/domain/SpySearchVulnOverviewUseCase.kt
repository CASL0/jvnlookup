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

package io.github.casl0.jvnlookup.domain

import io.github.casl0.jvnlookup.model.DomainVulnOverview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/** [SearchVulnOverviewUseCase]のスパイ */
class SpySearchVulnOverviewUseCase : SearchVulnOverviewUseCase {
    var invokeCount = 0
        private set

    override val searchResults: Flow<List<DomainVulnOverview>>
        get() = TODO("Not yet implemented")

    override suspend fun invoke(keyword: CharSequence): Result<Int> {
        invokeCount++
        delay(1_000)
        return Result.success(5)
    }
}

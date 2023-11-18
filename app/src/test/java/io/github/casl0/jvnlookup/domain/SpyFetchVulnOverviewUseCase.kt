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
import kotlinx.coroutines.flow.MutableStateFlow

/** FetchVulnOverviewUseCaseのSpy */
class SpyFetchVulnOverviewUseCase(private val stubData: List<DomainVulnOverview>) :
    FetchVulnOverviewUseCase {

    /** invoke呼び出し回数 */
    var invokeCallCount: Int = 0
        private set

    private val _vulnOverviews = MutableStateFlow(listOf<DomainVulnOverview>())

    override val vulnOverviews: Flow<List<DomainVulnOverview>>
        get() = _vulnOverviews

    override suspend fun invoke(): Result<Unit> {
        invokeCallCount++
        delay(1_000)
        _vulnOverviews.value = stubData
        return Result.success(Unit)
    }
}

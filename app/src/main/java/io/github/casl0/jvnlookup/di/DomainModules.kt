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

package io.github.casl0.jvnlookup.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.casl0.jvnlookup.domain.FavoriteVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.FetchVulnOverviewUseCase
import io.github.casl0.jvnlookup.domain.SearchVulnOverviewUseCase
import io.github.casl0.jvnlookup.repository.JvnRepository
import io.github.casl0.jvnlookup.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideFetchVulnOverviewUseCase(
        jvnRepository: JvnRepository,
        @IoDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    ): FetchVulnOverviewUseCase {
        return FetchVulnOverviewUseCase(jvnRepository, defaultDispatcher)
    }

    @Provides
    fun provideFavoriteVulnOverviewUseCase(
        jvnRepository: JvnRepository,
        @IoDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    ): FavoriteVulnOverviewUseCase {
        return FavoriteVulnOverviewUseCase(jvnRepository, defaultDispatcher)
    }

    @Provides
    fun provideSearchVulnOverviewUseCase(
        searchRepository: SearchRepository,
        @IoDispatcher defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    ): SearchVulnOverviewUseCase {
        return SearchVulnOverviewUseCase(searchRepository, defaultDispatcher)
    }
}

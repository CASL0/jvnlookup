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

import android.content.Context
import androidx.room.Room
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.casl0.jvnlookup.BuildConfig
import io.github.casl0.jvnlookup.data.JvnDataSource
import io.github.casl0.jvnlookup.data.local.JvnLocalDataSource
import io.github.casl0.jvnlookup.data.remote.JvnRemoteDataSource
import io.github.casl0.jvnlookup.database.JvnDatabase
import io.github.casl0.jvnlookup.database.MIGRATION_1_2
import io.github.casl0.jvnlookup.network.MyJvnApiService
import io.github.casl0.jvnlookup.repository.DefaultJvnRepository
import io.github.casl0.jvnlookup.repository.DefaultSearchRepository
import io.github.casl0.jvnlookup.repository.JvnRepository
import io.github.casl0.jvnlookup.repository.SearchRepository
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalJvnDataSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteJvnDataSource

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideJvnRepository(
        @LocalJvnDataSource jvnLocalDataSource: JvnDataSource,
        @RemoteJvnDataSource jvnRemoteDataSource: JvnDataSource
    ): JvnRepository {
        return DefaultJvnRepository(jvnLocalDataSource, jvnRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        @RemoteJvnDataSource jvnRemoteDataSource: JvnDataSource
    ): SearchRepository {
        return DefaultSearchRepository(jvnRemoteDataSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @LocalJvnDataSource
    @Provides
    fun provideJvnLocalDataSource(database: JvnDatabase): JvnDataSource {
        return JvnLocalDataSource(database)
    }

    @Singleton
    @RemoteJvnDataSource
    @Provides
    fun provideJvnRemoteDataSource(
        myJvnApiService: MyJvnApiService
    ): JvnDataSource {
        return JvnRemoteDataSource(myJvnApiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): JvnDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            JvnDatabase::class.java,
            "jvn"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideApiService(): MyJvnApiService {
        return Retrofit.Builder()
            .addConverterFactory(
                TikXmlConverterFactory.create(
                    TikXml.Builder().exceptionOnUnreadXml(false).build()
                )
            )
            .baseUrl(BuildConfig.MYJVN_URL)
            .build()
            .create(MyJvnApiService::class.java)
    }
}

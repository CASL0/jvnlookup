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

package io.github.casl0.jvnlookup.repository

import io.github.casl0.jvnlookup.data.JvnDataSource
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class DefaultJvnRepositoryTest {
    @Test
    fun refreshVulnOverviews_getVulnOverviewsFailed_ReturnsResultFailure() = runTest {
        val mock = mock<JvnDataSource> {
            onBlocking {
                getVulnOverviews(
                    null,
                    "n",
                    "m",
                    "m"
                )
            } doReturn Result.failure(Exception("error"))
        }

        val repository = DefaultJvnRepository(mock, mock)
        val result = repository.refreshVulnOverviews()

        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull().toString(), `is`(Exception("error").toString()))
    }
}

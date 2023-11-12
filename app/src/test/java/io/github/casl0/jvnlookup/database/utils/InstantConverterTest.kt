package io.github.casl0.jvnlookup.database.utils

import kotlinx.datetime.toInstant
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class InstantConverterTest {

    @Test
    fun instantToLong() {
        val converter = InstantConverter()

        assertThat(
            converter.instantToLong("2023-11-12T11:22:33Z".toInstant()),
            `is`(1699788153000)
        )
    }

    @Test
    fun longToInstant() {
        val converter = InstantConverter()

        assertThat(
            converter.longToInstant(1699788153000),
            `is`("2023-11-12T11:22:33Z".toInstant())
        )
    }
}

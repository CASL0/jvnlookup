package io.github.casl0.jvnlookup.database

import kotlinx.datetime.toInstant
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class DatabaseEntitiesTest {
    private lateinit var databaseData: VulnOverviewWithReferencesAndCVSS

    @Before
    fun setup() {
        databaseData = VulnOverviewWithReferencesAndCVSS(
            vulnOverview = DatabaseVulnOverview(
                title = "複数の ZyXEL 製品におけるクロスサイトスクリプティングの脆弱性",
                link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-022564.html",
                description = "ATP800 ファームウェア、ATP700 ファームウェア、ATP500 ファームウェア等複数の ZyXEL 製品には、クロスサイトスクリプティングの脆弱性が存在します。",
                id = "JVNDB-2022-022564",
                issued = "2023-11-17T17:21:16+09:00".toInstant(),
                modified = "2023-11-17T17:21:16+09:00".toInstant(),
                isFavorite = false,
            ),
            references = listOf(
                DatabaseReference(
                    ownerId = "JVNDB-2022-022564",
                    url = "https://nvd.nist.gov/vuln/detail/CVE-2022-40603",
                    source = "NVD",
                    title = null,
                    id = "CVE-2022-40603"
                )
            ),
            cvssList = listOf(
                DatabaseCVSS(
                    ownerId = "JVNDB-2022-022564",
                    version = "3.0",
                    type = "Base",
                    severity = "Medium",
                    score = "6.1",
                    vector = "CVSS:3.0/AV:N/AC:L/PR:N/UI:R/S:C/C:L/I:L/A:N"
                )
            )
        )
    }

    @Test
    fun asDomainModel() {
        val domainData = listOf(databaseData).asDomainModel()[0]

        assertThat(domainData.id, `is`(databaseData.vulnOverview.id))
        assertThat(domainData.link, `is`(databaseData.vulnOverview.link))
        assertThat(domainData.description, `is`(databaseData.vulnOverview.description))
        assertThat(domainData.issued, `is`(databaseData.vulnOverview.issued))
        assertThat(domainData.modified, `is`(databaseData.vulnOverview.modified))
        assertThat(domainData.isFavorite, `is`(databaseData.vulnOverview.isFavorite))
        assertThat(domainData.title, `is`(databaseData.vulnOverview.title))
        assertThat(domainData.cvssList[0].score, `is`(databaseData.cvssList[0].score))
        assertThat(domainData.cvssList[0].type, `is`(databaseData.cvssList[0].type))
        assertThat(domainData.cvssList[0].severity, `is`(databaseData.cvssList[0].severity))
        assertThat(domainData.cvssList[0].vector, `is`(databaseData.cvssList[0].vector))
        assertThat(domainData.cvssList[0].version, `is`(databaseData.cvssList[0].version))
        assertThat(domainData.references[0].id, `is`(databaseData.references[0].id))
        assertThat(domainData.references[0].source, `is`(databaseData.references[0].source))
        assertThat(domainData.references[0].title, `is`(databaseData.references[0].title))
        assertThat(domainData.references[0].url, `is`(databaseData.references[0].url))
    }
}

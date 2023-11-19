package io.github.casl0.jvnlookup.model

import kotlinx.datetime.toInstant
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class VulnOverviewTest {
    private lateinit var domainData: DomainVulnOverview

    @Before
    fun setup() {
        domainData = DomainVulnOverview(
            title = "複数の ZyXEL 製品におけるクロスサイトスクリプティングの脆弱性",
            link = "https://jvndb.jvn.jp/ja/contents/2022/JVNDB-2022-022564.html",
            description = "ATP800 ファームウェア、ATP700 ファームウェア、ATP500 ファームウェア等複数の ZyXEL 製品には、クロスサイトスクリプティングの脆弱性が存在します。",
            id = "JVNDB-2022-022564",
            issued = "2023-11-17T17:21:16+09:00".toInstant(),
            modified = "2023-11-17T17:21:16+09:00".toInstant(),
            isFavorite = false,
            references = listOf(
                DomainReference(
                    id = "CVE-2022-40603",
                    url = "https://nvd.nist.gov/vuln/detail/CVE-2022-40603",
                    source = "NVD",
                    title = null
                )
            ),
            cvssList = listOf(
                DomainCVSS(
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
    fun asDatabaseEntity() {
        val databaseVulnOverview = domainData.asDatabaseEntity()
        val databaseCvss = domainData.cvssList.asDatabaseEntity(domainData.id)
        val databaseReference = domainData.references.asDatabaseEntity(domainData.id)

        assertThat(databaseVulnOverview.id, `is`(domainData.id))
        assertThat(databaseVulnOverview.description, `is`(domainData.description))
        assertThat(databaseVulnOverview.link, `is`(domainData.link))
        assertThat(databaseVulnOverview.issued, `is`(domainData.issued))
        assertThat(databaseVulnOverview.modified, `is`(domainData.modified))
        assertThat(databaseVulnOverview.title, `is`(domainData.title))
        assertThat(databaseVulnOverview.isFavorite, `is`(domainData.isFavorite))

        assertThat(databaseCvss.size, `is`(1))
        assertThat(databaseCvss[0].ownerId, `is`(domainData.id))
        assertThat(databaseCvss[0].score, `is`(domainData.cvssList[0].score))
        assertThat(databaseCvss[0].type, `is`(domainData.cvssList[0].type))
        assertThat(databaseCvss[0].vector, `is`(domainData.cvssList[0].vector))
        assertThat(databaseCvss[0].severity, `is`(domainData.cvssList[0].severity))
        assertThat(databaseCvss[0].version, `is`(domainData.cvssList[0].version))

        assertThat(databaseReference.size, `is`(1))
        assertThat(databaseReference[0].ownerId, `is`(domainData.id))
        assertThat(databaseReference[0].url, `is`(domainData.references[0].url))
        assertThat(databaseReference[0].title, `is`(domainData.references[0].title))
        assertThat(databaseReference[0].source, `is`(domainData.references[0].source))
        assertThat(databaseReference[0].id, `is`(domainData.references[0].id))
    }
}

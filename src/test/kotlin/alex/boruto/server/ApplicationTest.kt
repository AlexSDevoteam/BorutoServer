package alex.boruto.server

import alex.boruto.server.models.ApiResponse
import alex.boruto.server.repository.HeroRepository
import alex.boruto.server.repository.NEXT_PAGE_KEY
import alex.boruto.server.repository.PREVIOUS_PAGE_KEY
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.koin.java.KoinJavaComponent.inject
import kotlin.test.assertEquals

class ApplicationTest {

    private val heroRepository: HeroRepository by inject(HeroRepository::class.java)

    @Test
    fun `access root endpoint, assert correct information`() {
        testApplication {
            application { module() }

            val response = client.get("/")

            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            assertEquals(
                expected = "Welcome to Boruto API!",
                actual = response.bodyAsText()
            )
        }
    }

    @Test
    fun `access all heroes endpoint, query non existing page number ,assert error`() = testApplication {
        application { module() }

        val response = client.get("boruto/heroes?page=6")
        assertEquals(
            expected = HttpStatusCode.NotFound,
            actual = response.status
        )

        assertEquals(expected = "Page not found", actual = response.bodyAsText())
    }

    @Test
    fun `access all heroes endpoint, query invalid page number ,assert error`() = testApplication {
        application { module() }

        val response = client.get("boruto/heroes?page=xdd")
        assertEquals(
            expected = HttpStatusCode.BadRequest,
            actual = response.status
        )

        val expected = ApiResponse(
            success = false,
            message = "Only numbers allowed"
        )
        val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun `access all heroes endpoint, query all pages, assert correct information`() = testApplication {
        application { module() }

        val pages = 1..5
        val heroes = listOf(
            heroRepository.page1,
            heroRepository.page2,
            heroRepository.page3,
            heroRepository.page4,
            heroRepository.page5
        )

        pages.forEach { page ->
            val response = client.get("/boruto/heroes?$page")
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            val expected = ApiResponse(
                success = true,
                message = "ok",
                previousPage = calculatePage(page)["prevPage"],
                nextPage = calculatePage(page)["nextPage"],
                heroes = heroes[page - 1]
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
            assertEquals(expected = expected, actual = actual)
        }
    }


    private fun calculatePage(page: Int): Map<String, Int?> {
        var prevPage: Int? = page
        var nextPage: Int? = page
        if (page in 1..4) {
            nextPage = nextPage?.plus(1)
        }
        if (page in 2..5) {
            prevPage = prevPage?.minus(1)
        }
        if (page == 1) {
            prevPage = null
        }
        if (page == 5) {
            nextPage = null
        }
        return mapOf(PREVIOUS_PAGE_KEY to prevPage, NEXT_PAGE_KEY to nextPage)
    }

    @Test
    fun `access search heroes endpoint, query hero name ,assert single hero result`() = testApplication {
        application { module() }

        val response = client.get("boruto/heroes/search?name=Sasuke")
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status
        )

        val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
        assertEquals(
            expected = 1,
            actual = actual
        )
    }

    @Test
    fun `access search heroes endpoint, query hero name ,assert multiple heroes result`() = testApplication {
        application { module() }

        val response = client.get("boruto/heroes/search?name=sa")
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status
        )

        val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
        assertEquals(
            expected = 3,
            actual = actual
        )
    }

    @Test
    fun `access search heroes endpoint, query empty text ,assert empty list as a result`() = testApplication {
        application { module() }

        val response = client.get("boruto/heroes/search?name=")
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status
        )

        val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes
        assertEquals(
            expected = emptyList(),
            actual = actual
        )
    }

    @Test
    fun `access search heroes endpoint, query non existing hero ,assert empty list as a result`() = testApplication {
        application { module() }

        val response = client.get("boruto/heroes/search?name=choji")
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status
        )

        val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes
        assertEquals(
            expected = emptyList(),
            actual = actual
        )
    }

    @Test
    fun `access non existing endpoint, assert not found`() = testApplication {
        application { module() }

        val response = client.get("boruto")
        assertEquals(
            expected = HttpStatusCode.NotFound,
            actual = response.status
        )

        assertEquals(expected = "Page not found", actual = response.bodyAsText())
    }
}
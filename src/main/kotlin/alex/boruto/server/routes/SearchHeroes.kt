package alex.boruto.server.routes

import alex.boruto.server.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchHeroes() {
    val heroRepository by application.inject<HeroRepository>()

    get("/boruto/heroes/search") {
        val name = call.request.queryParameters["name"]
        val apiResponse = heroRepository.searchHeroes(query = name)
        call.respond(message = apiResponse, status = HttpStatusCode.OK)
    }
}
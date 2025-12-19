package alex.boruto.server.routes

import alex.boruto.server.models.ApiResponse
import alex.boruto.server.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.getAllHeroes() {
    val heroRepository: HeroRepository by application.inject()

    get("/boruto/heroes") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            val limit = call.request.queryParameters["limit"]?.toInt() ?: 3

            val apiResponse = heroRepository.getAllHeroes(
                page = page, limit = limit
            )
            call.respond(
                message = apiResponse,
                status = HttpStatusCode.OK
            )
        } catch (_: NumberFormatException) {
            call.respond(
                message = ApiResponse(success = false, message = "Only Numbers Allowed."),
                status = HttpStatusCode.BadRequest
            )
        } catch (_: IllegalArgumentException) {
            call.respond(
                message = ApiResponse(success = false, message = "Heroes not Found."),
                status = HttpStatusCode.NotFound
            )
        } catch (e: Exception) {
            call.respond(
                message = ApiResponse(success = false, message = e.stackTrace.toString()),
                status = HttpStatusCode.NotFound
            )
        }
    }
}
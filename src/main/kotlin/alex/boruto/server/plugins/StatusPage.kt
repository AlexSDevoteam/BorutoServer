package alex.boruto.server.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPage() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(
                message = "Page not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
}
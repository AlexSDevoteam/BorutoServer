package alex.boruto.server.plugins

import alex.boruto.server.routes.getAllHeroes
import alex.boruto.server.routes.root
import alex.boruto.server.routes.searchHeroes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
        getAllHeroes()
        searchHeroes()

        staticResources(remotePath = "/images", basePackage = "images")
    }
}
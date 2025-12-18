package alex.boruto.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*

fun Application.configureMonitoring() {
    install(CallLogging)
}
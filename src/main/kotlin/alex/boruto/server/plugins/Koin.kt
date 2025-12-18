package alex.boruto.server.plugins

import alex.boruto.server.di.koinModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {

    // Install Koin plugin
    install(Koin) {
        // SLF4J Koin logger
        slf4jLogger()

        // Declare modules
        modules(koinModule)
    }
}
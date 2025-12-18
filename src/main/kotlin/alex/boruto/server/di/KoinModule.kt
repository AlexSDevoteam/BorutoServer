package alex.boruto.server.di

import alex.boruto.server.repository.HeroRepository
import alex.boruto.server.repository.HeroRepositoryImpl
import org.koin.dsl.module

val koinModule = module {
    single<HeroRepository> { HeroRepositoryImpl() }
}
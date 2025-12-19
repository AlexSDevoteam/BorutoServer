package alex.boruto.server.repository

import alex.boruto.server.models.ApiResponse
import alex.boruto.server.models.Hero

interface HeroRepository {
    val heroes: List<Hero>

    suspend fun getAllHeroes(page: Int = 1, limit: Int = 4): ApiResponse
    suspend fun searchHeroes(query: String?): ApiResponse
}
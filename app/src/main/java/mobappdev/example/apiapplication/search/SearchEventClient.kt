package mobappdev.example.apiapplication.search

import Matches
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 2:50 PM
 */
interface SearchEventClient {
    fun byName(name: String): ResultResponse

    suspend fun byNameAndSeason(name: String, season: String): Result<Matches>
}
package mobappdev.example.apiapplication.search

import mobappdev.example.apiapplication.data.Teams
import mobappdev.example.apiapplication.data.TeamsDetails
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 2:43 PM
 */
interface SearchTeamClient {
    suspend fun byName(name: String): Result<Teams>

    fun byShortCode(shortCode: String): ResultResponse
    suspend fun searchTeamByCountry(country: String) :Result<TeamsDetails>
}
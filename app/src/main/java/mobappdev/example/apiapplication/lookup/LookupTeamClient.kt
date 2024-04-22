package mobappdev.example.apiapplication.lookup

import mobappdev.example.apiapplication.data.TeamDetails
import mobappdev.example.apiapplication.data.TeamsDetails
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:55 PM
 */
interface LookupTeamClient {
    suspend fun byId(id: Int): Result<TeamsDetails>
}
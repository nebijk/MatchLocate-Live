package mobappdev.example.apiapplication.list

import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
interface ListTeamsPlayersClient {
    fun byTeamId(id: Int): ResultResponse
}
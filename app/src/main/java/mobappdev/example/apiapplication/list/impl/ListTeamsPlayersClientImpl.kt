package mobappdev.example.apiapplication.list.impl

import mobappdev.example.apiapplication.list.ListTeamsPlayersClient
import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.AbstractJsportsClient

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListTeamsPlayersClientImpl : ListTeamsPlayersClient, AbstractJsportsClient() {
    override fun byTeamId(id: Int): ResultResponse {
        val request = requestBuilder("/lookup_all_players.php?id=$id")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
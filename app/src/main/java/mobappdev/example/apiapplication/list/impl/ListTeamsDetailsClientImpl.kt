package mobappdev.example.apiapplication.list.impl

import mobappdev.example.apiapplication.list.ListTeamsDetailsClient
import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.AbstractJsportsClient

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListTeamsDetailsClientImpl : ListTeamsDetailsClient, AbstractJsportsClient() {
    override fun byLeague(id: Int): ResultResponse {
        val request = requestBuilder("/lookup_all_teams.php?id=$id")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
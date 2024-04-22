package mobappdev.example.apiapplication.list.impl

import mobappdev.example.apiapplication.list.ListTeamsClient
import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.AbstractJsportsClient

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListTeamsClientImpl : ListTeamsClient, AbstractJsportsClient() {
    override fun byLeague(league: String): ResultResponse {
        val request = requestBuilder("/search_all_teams.php?l=$league")
        return ResultResponse(handleClientCall(request), objectMapper)
    }

    override fun bySportAndCountry(sport: String, country: String): ResultResponse {
        val request = requestBuilder("/search_all_teams.php?s=$sport&c=$country")
        return ResultResponse(handleClientCall(request), objectMapper)
    }

}
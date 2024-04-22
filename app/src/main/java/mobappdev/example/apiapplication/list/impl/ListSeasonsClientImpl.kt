package mobappdev.example.apiapplication.list.impl

import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.list.ListSeasonsClient

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListSeasonsClientImpl : ListSeasonsClient, AbstractJsportsClient() {
    override fun byLeague(league: Int): ResultResponse {
        val request = requestBuilder("/search_all_seasons.php?id=$league")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
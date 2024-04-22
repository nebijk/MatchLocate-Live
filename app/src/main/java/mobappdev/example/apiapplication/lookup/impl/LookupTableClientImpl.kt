package mobappdev.example.apiapplication.lookup.impl


import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.lookup.LookupTableClient
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:56 PM
 */
class LookupTableClientImpl : LookupTableClient, AbstractJsportsClient() {
    override fun byLeagueAndSeason(leagueId: Int, seasonId: Int): ResultResponse {
        val request = requestBuilder("/lookuptable.php?l=$leagueId&s=$seasonId")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
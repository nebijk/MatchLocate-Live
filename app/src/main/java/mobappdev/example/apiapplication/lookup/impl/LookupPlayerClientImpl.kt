package mobappdev.example.apiapplication.lookup.impl


import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.lookup.LookupPlayerClient
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:56 PM
 */
class LookupPlayerClientImpl : LookupPlayerClient, AbstractJsportsClient() {
    override fun byId(id: Int): ResultResponse {
        val request = requestBuilder("/lookupplayer.php?id=$id")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
package mobappdev.example.apiapplication.lookup.impl


import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.lookup.LookupEventClient
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:56 PM
 */
class LookupEventClientImpl : LookupEventClient, AbstractJsportsClient() {
    override fun byId(id: Int): ResultResponse {
        val request = requestBuilder("/lookupevent.php?id=$id")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
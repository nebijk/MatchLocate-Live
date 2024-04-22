package mobappdev.example.apiapplication.lookup.impl


import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.lookup.LookupHonorClient
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:56 PM
 */
class LookupHonorClientImpl : LookupHonorClient, AbstractJsportsClient() {
    override fun byId(id: Int): ResultResponse {
        val request = requestBuilder("/lookuphonors.php?id=$id")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
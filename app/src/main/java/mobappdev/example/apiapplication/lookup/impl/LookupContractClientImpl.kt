package mobappdev.example.apiapplication.lookup.impl

import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.lookup.LookupContractClient
import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:56 PM
 */
class LookupContractClientImpl : LookupContractClient, AbstractJsportsClient() {
    override fun byId(id: Int): ResultResponse {
        val request = requestBuilder("/lookupcontracts.php?id=$id")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
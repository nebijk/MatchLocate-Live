package mobappdev.example.apiapplication.list.impl

import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.list.ListSportsClient

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListSportsClientImpl : ListSportsClient, AbstractJsportsClient() {
    override fun all(): ResultResponse {
        val request = requestBuilder("/all_sports.php")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
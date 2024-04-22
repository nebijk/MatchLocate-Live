package mobappdev.example.apiapplication.list.impl

import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.list.ListUsersLovedClient
import mobappdev.example.apiapplication.model.ResultResponse


/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListUsersLovedClientImpl : ListUsersLovedClient, AbstractJsportsClient() {
    override fun byUser(user: String): ResultResponse {
        val request = requestBuilder("/searchloves.php?u=$user")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
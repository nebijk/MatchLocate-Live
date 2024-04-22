package mobappdev.example.apiapplication.search

import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 2:51 PM
 */
interface SearchFileClient {
    fun byEventName(fileName: String): ResultResponse
}
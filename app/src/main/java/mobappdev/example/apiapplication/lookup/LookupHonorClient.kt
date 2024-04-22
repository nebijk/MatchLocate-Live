package mobappdev.example.apiapplication.lookup

import mobappdev.example.apiapplication.model.ResultResponse

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:55 PM
 */
interface LookupHonorClient {
    fun byId(id: Int): ResultResponse
}
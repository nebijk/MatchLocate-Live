package mobappdev.example.apiapplication.search.impl

import Matches
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.data.Leagues

import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.search.SearchEventClient
import java.net.HttpURLConnection


/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 2:54 PM
 */
class SearchEventClientImpl : SearchEventClient, AbstractJsportsClient() {

    override suspend fun byNameAndSeason(name: String, season: String): Result<Matches> {
        val request = requestBuilder("/searchevents.php?e=$name&s=$season")

        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<Matches>() {}.type
                val joke = Gson().fromJson<Matches>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    override fun byName(name: String): ResultResponse {
        val request = requestBuilder("/searchevents.php?e=$name")
        return ResultResponse(handleClientCall(request), objectMapper)
    }


}
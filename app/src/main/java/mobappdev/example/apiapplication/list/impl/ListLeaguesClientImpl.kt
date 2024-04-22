package mobappdev.example.apiapplication.list.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.data.Leagues
import mobappdev.example.apiapplication.list.ListLeaguesClient
import java.net.HttpURLConnection

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 4:06 PM
 */
class ListLeaguesClientImpl : ListLeaguesClient, AbstractJsportsClient() {
    override suspend fun all(): Result<Leagues> {
        val request = requestBuilder("/search_all_leagues.php?s=Soccer")

        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<Leagues>() {}.type
                val joke = Gson().fromJson<Leagues>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun byCountry(country: String): ResultResponse {
        val request = requestBuilder("/search_all_leagues.php?c=$country")
        return ResultResponse(handleClientCall(request), objectMapper)
    }

    override fun byCountryAndSport(country: String, sport: String): ResultResponse {
        val request = requestBuilder("/search_all_leagues.php?c=$country&s=$sport")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
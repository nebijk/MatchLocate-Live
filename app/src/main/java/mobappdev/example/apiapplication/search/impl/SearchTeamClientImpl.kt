package mobappdev.example.apiapplication.search.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.data.Leagues
import mobappdev.example.apiapplication.data.Teams
import mobappdev.example.apiapplication.data.TeamsDetails
import mobappdev.example.apiapplication.model.ResultResponse
import mobappdev.example.apiapplication.search.SearchTeamClient
import java.net.HttpURLConnection


/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 3:00 PM
 */
class SearchTeamClientImpl : SearchTeamClient, AbstractJsportsClient() {
    override suspend fun byName(name: String): Result<Teams> {
        val request = requestBuilder("/searchteams.php?t=$name")

        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<Teams>() {}.type
                val joke = Gson().fromJson<Teams>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


   override suspend fun searchTeamByCountry(country: String) :Result<TeamsDetails>
   {
        val request = requestBuilder("/search_all_teams.php?s=Soccer&c=$country")

        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<TeamsDetails>() {}.type
                val joke = Gson().fromJson<TeamsDetails>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    override fun byShortCode(shortCode: String): ResultResponse {
        val request = requestBuilder("/searchteams.php?sname=$shortCode")
        return ResultResponse(handleClientCall(request), objectMapper)
    }
}
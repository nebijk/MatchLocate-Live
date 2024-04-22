package mobappdev.example.apiapplication.lookup.impl


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.data.Leagues
import mobappdev.example.apiapplication.data.TeamDetails
import mobappdev.example.apiapplication.data.TeamsDetails
import mobappdev.example.apiapplication.lookup.LookupTeamClient
import mobappdev.example.apiapplication.model.ResultResponse
import java.net.HttpURLConnection

/**
 * Created by Arthur Asatryan.
 * Date: 11/17/19
 * Time: 8:56 PM
 */
class LookupTeamClientImpl : LookupTeamClient, AbstractJsportsClient() {
    override suspend fun byId(id: Int): Result<TeamsDetails> {
        val request = requestBuilder("/lookupteam.php?id=$id")

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
}
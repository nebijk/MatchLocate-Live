package mobappdev.example.apiapplication.search.impl

import LiveDataEvents
import MatchDetails
import Stats
import TimeLines
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.data.Teams
import okhttp3.Request
import java.net.HttpURLConnection

class MatchDetailsImpl: AbstractJsportsClient()
{
    suspend fun getMatchDetail(id: Int): Result<MatchDetails>
    {
        val request = requestBuilder("/lookupevent.php?id=$id")

        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<MatchDetails>() {}.type
                val joke = Gson().fromJson<MatchDetails>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    suspend fun getStats(id: Int):Result<Stats>{
        val request = requestBuilder("/lookupeventstats.php?id=$id")
        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                if (JsonParser.parseString(json).asJsonObject.get("eventstats").isJsonNull) {
                    return@withContext Result.success(Stats(emptyList()))
                }
                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<Stats>() {}.type
                val joke = Gson().fromJson<Stats>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getTimeLine(id: Int):Result<TimeLines>{
        val request = requestBuilder("/lookuptimeline.php?id=$id")
        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                if (JsonParser.parseString(json).asJsonObject.get("timeline").isJsonNull) {
                    return@withContext Result.success(TimeLines(emptyList()))
                }
                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<TimeLines>() {}.type
                val joke = Gson().fromJson<TimeLines>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getLiveData():Result<LiveDataEvents>{
        val request = Request.Builder().url("https://thesportsdb.com/api/v2/json/60130162/livescore.php?s=Soccer").build()
        val url = request.url.toUrl()

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<LiveDataEvents>() {}.type
                val joke = Gson().fromJson<LiveDataEvents>(json, type)

                Result.success(joke)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


}
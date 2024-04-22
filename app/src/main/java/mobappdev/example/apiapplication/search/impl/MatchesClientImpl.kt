package mobappdev.example.apiapplication.search.impl

import PastAndUpcomingMatches
import PastMatches
import UpcomingMatch
import UpcomingMatches
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.apiapplication.AbstractJsportsClient
import mobappdev.example.apiapplication.lookup.LookupTeamClient
import mobappdev.example.apiapplication.lookup.impl.LookupTeamClientImpl
import mobappdev.example.apiapplication.search.MatchesClient
import java.net.HttpURLConnection

class MatchesClientImpl: MatchesClient, AbstractJsportsClient()
{
    override suspend fun searchUpcomingMatchesById(id: Int): Result<UpcomingMatches> {
        val lookupTeamClient: LookupTeamClient = LookupTeamClientImpl()
        return withContext(Dispatchers.IO) {
            try {
                val request = requestBuilder("/eventsnext.php?id=$id")
                val url = request.url.toUrl()

                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                if (JsonParser.parseString(json).asJsonObject.get("events").isJsonNull) {
                    return@withContext Result.success(UpcomingMatches(emptyList()))
                }
                // Use Gson to parse the JSON string into an UpcomingMatches object
                val type = object : TypeToken<UpcomingMatches>() {}.type
                val upcomingMatches = Gson().fromJson<UpcomingMatches>(json, type)
                Log.e("UPCOM", upcomingMatches.events.toString())
                if (upcomingMatches.events != null) {
                    // Check if "events" is not empty before iterating
                    if (upcomingMatches.events.isNotEmpty()) {
                        // Iterate through each upcoming match and fetch team details
                        for (event in upcomingMatches.events) {
                            if (event.idHomeTeam == id) {
                                // Update strHomeTeamBadge directly without making an additional call
                                event.strHomeTeamBadge =
                                    "BadgeURL"  // Replace with the actual badge URL
                                val awayTeamResult = lookupTeamClient.byId(event.idAwayTeam)

                                // Check if the call was successful
                                if (awayTeamResult.isSuccess) {
                                    val awayTeam = awayTeamResult.getOrThrow()

                                    // Update strAwayTeamBadge
                                    event.strAwayTeamBadge = awayTeam.teams[0].strTeamBadge
                                } else {
                                    // Handle the case where the byId call for the away team failed
                                    // You can log an error or handle it based on your application's logic
                                }
                            } else if (event.idAwayTeam == id) {
                                // If the current match is not the specified match, fetch away team details using byId
                                // Update strAwayTeamBadge directly without making an additional call
                                event.strAwayTeamBadge =
                                    "BadgeURL"  // Replace with the actual badge URL
                                val homeTeamResult = lookupTeamClient.byId(event.idHomeTeam)

                                // Check if the call was successful
                                if (homeTeamResult.isSuccess) {
                                    val homeTeam = homeTeamResult.getOrThrow()

                                    // Update strAwayTeamBadge
                                    event.strHomeTeamBadge = homeTeam.teams[0].strTeamBadge
                                } else {
                                    // Handle the case where the byId call for the away team failed
                                    // You can log an error or handle it based on your application's logic
                                }
                            }
                        }
                    }
                }

                Result.success(upcomingMatches)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun searchPastMatchesById(id: Int): Result<PastMatches> {
        val lookupTeamClient: LookupTeamClient = LookupTeamClientImpl()
        return withContext(Dispatchers.IO) {
            try {
                val request = requestBuilder("/eventslast.php?id=$id")
                val url = request.url.toUrl()

                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into an UpcomingMatches object
                val type = object : TypeToken<PastMatches>() {}.type
                val pastMatches = Gson().fromJson<PastMatches>(json, type)

                // Iterate through each upcoming match and fetch team details
                Log.e("CLIENT PAST ", pastMatches.toString())
                for (event in pastMatches.results) {
                    if (event.idHomeTeam == id) {
                        // Update strHomeTeamBadge directly without making an additional call
                        event.strHomeTeamBadge = "BadgeURL"  // Replace with the actual badge URL
                        val awayTeamResult = lookupTeamClient.byId(event.idAwayTeam)

                        // Check if the call was successful
                        if (awayTeamResult.isSuccess) {
                            val awayTeam = awayTeamResult.getOrThrow()

                            // Update strAwayTeamBadge
                            event.strAwayTeamBadge = awayTeam.teams[0].strTeamBadge
                        } else {
                            // Handle the case where the byId call for the away team failed
                            // You can log an error or handle it based on your application's logic
                        }
                    } else if (event.idAwayTeam == id){
                        // If the current match is not the specified match, fetch away team details using byId
                        // Update strHomeTeamBadge directly without making an additional call
                        event.strAwayTeamBadge = "BadgeURL"  // Replace with the actual badge URL
                        val homeTeamResult = lookupTeamClient.byId(event.idHomeTeam)

                        // Check if the call was successful
                        if (homeTeamResult.isSuccess) {
                            val homeTeam = homeTeamResult.getOrThrow()

                            // Update strAwayTeamBadge
                            event.strHomeTeamBadge = homeTeam.teams[0].strTeamBadge
                        } else {
                            // Handle the case where the byId call for the away team failed
                            // You can log an error or handle it based on your application's logic
                        }
                    }
                }

                Result.success(pastMatches)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getPastAndUpcomingMatchesForMultipleTeams(ids: List<Int>): Result<Map<Int, PastAndUpcomingMatches>> {
        return withContext(Dispatchers.IO) {
            try {
                val resultHashMap = mutableMapOf<Int, PastAndUpcomingMatches>()

                for (id in ids) {
                    // Fetch past matches
                    val pastMatchesResult = searchPastMatchesById(id)
                    val pastMatches = pastMatchesResult.getOrThrow()

                    // Fetch upcoming matches
                    val upcomingMatchesResult = searchUpcomingMatchesById(id)
                    val upcomingMatches = upcomingMatchesResult.getOrThrow()

                    // Create and add PastAndUpcomingMatches object to the HashMap
                    val combinedMatches = PastAndUpcomingMatches(
                        results = pastMatches,
                        events = upcomingMatches
                    )
                    Log.e("TEEASD", combinedMatches.toString())
                    resultHashMap[id] = combinedMatches
                }

                Result.success(resultHashMap)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


}
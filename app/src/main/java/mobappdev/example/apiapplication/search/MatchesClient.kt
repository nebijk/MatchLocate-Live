package mobappdev.example.apiapplication.search

import PastAndUpcomingMatches
import PastMatches
import UpcomingMatches

interface MatchesClient {
    suspend fun searchUpcomingMatchesById(id: Int): Result<UpcomingMatches>
    suspend fun searchPastMatchesById(id: Int): Result<PastMatches>
    suspend fun getPastAndUpcomingMatchesForMultipleTeams(ids: List<Int>): Result<Map<Int, PastAndUpcomingMatches>>
}

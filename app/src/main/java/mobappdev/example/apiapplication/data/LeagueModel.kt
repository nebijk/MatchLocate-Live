package mobappdev.example.apiapplication.data

import android.provider.CalendarContract.Events
import org.json.JSONObject

data class Leagues(
    val countries: List<League>
)

data class League(
    val idLeague: Int,
    val strLeague: String,
    val strSport: String,
    val strLeagueAlternate: String
)

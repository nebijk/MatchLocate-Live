import org.json.JSONObject

data class Match(
    val strHomeTeam: String,
    val strAwayTeam: String,
    val intHomeScore: Int,
    val intAwayScore: Int,
)
data class Matches (
    val event: List<Match>
)
fun parseToMatch(responseData: JSONObject): Match {
    val strHomeTeam = responseData.optString("strHomeTeam")
    val strAwayTeam = responseData.optString("strAwayTeam")
    val intHomeScore = responseData.optInt("intHomeScore")
    val intAwayScore = responseData.optInt("intAwayScore")

    return Match(strHomeTeam, strAwayTeam, intHomeScore, intAwayScore)
}

data class UpcomingMatch(
    val idEvent: Int,
    val strHomeTeam: String,
    val idHomeTeam: Int,
    val strAwayTeam: String,
    val idAwayTeam: Int,
    var strHomeTeamBadge: String,
    var strAwayTeamBadge: String,
    val strVenue: String,
    val dateEvent: String,
    val strTime: String,
)
data class UpcomingMatches (
    val events: List<UpcomingMatch>
)

data class PastMatch(
    val idEvent: Int,
    val strHomeTeam: String,
    val idHomeTeam: Int,
    val strAwayTeam: String,
    val idAwayTeam: Int,
    var strHomeTeamBadge: String,
    var strAwayTeamBadge: String,
    val intHomeScore: Int,
    val intAwayScore: Int,
    val strVenue: String,
    val dateEvent: String,
    val strTime: String,
)
data class PastMatches (
    val results: List<PastMatch>
)

data class MatchDetails(
    val events: List<MatchDetail>
)

data class MatchDetail(
    val idEvent: String,
    val dateEvent: String,
    val strVenue: String,
    val strCountry: String,
    val strCity: String,
    val strEvent: String,
    val strLeague: String,
    val strSeason: String,
    val intRound: Int,
    val intAwayScore: Int,
    val intHomeScore: Int,
    val strHomeTeam: String,
    val strAwayTeam: String,
)
data class TimeLine(
    val idEvent: Int,
    val strTimeline: String,
    val strTimelineDetail: String,
    val strPlayer: String,
    val intTime: Int,
    val strTeam: String,
    val strAssist: String,
)
data class TimeLines(
    val timeline: List<TimeLine>
)
data class Stat(
    val strStat: String,
    val intHome: Int,
    val intAway: Int
)
data class Stats(
    val eventstats: List<Stat>

)
data class PastAndUpcomingMatches (
    val results: PastMatches,
    val events: UpcomingMatches
)

data class LiveData(
    val idLiveScore: Int,
    val idEvent: Int,
    val idHomeTeam: Int,
    val idAwayTeam: Int,
    val strHomeTeam: String,
    val strAwayTeam: String,
    val strHomeTeamBadge: String,
    val strAwayTeamBadge: String,
    val intHomeScore: Int,
    val intAwayScore: Int,
    val strProgress: String,
    val strEventTime: String,
    val dateEvent: String,
    val updated: String,
)
data class LiveDataEvents(
    var events: MutableList<LiveData>
)



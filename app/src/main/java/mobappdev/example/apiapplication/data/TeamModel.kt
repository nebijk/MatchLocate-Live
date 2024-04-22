package mobappdev.example.apiapplication.data

data class Teams(
    val teams: List<TeamShort>
)

data class TeamShort(
    val idTeam: Int,
    val strTeam: String,
    val strCountry: String,
    val strTeamBadge: String
)

data class TeamsDetails(
    val teams: List<TeamDetails>
)

data class TeamDetails(
    val idTeam: Int,
    val strTeam: String,
    val strCountry: String,
    val strTeamBadge: String,
    val intFormedYear: String,
    val strLeague: String,
    val idLeague: Int,
    val strLeague2: String,
    val idLeague2: Int,
    val strLeague3: String,
    val idLeague3: Int,
    val strLeague4: String,
    val idLeague4: Int,
    val strLeague5: String,
    val idLeague5: Int,
    val strLeague6: String,
    val idLeague6: Int,
    val strLeague7: String,
    val idLeague7: Int,
    val strStadium: String,
    val strStadiumLocation: String,
    val intStadiumCapacity: Int,
    val strWebsite: String
)
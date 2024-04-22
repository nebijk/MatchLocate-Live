package mobappdev.example.apiapplication.data
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object TeamStorage {
    private const val PREFS_NAME_DETAILS = "TeamDetailsPrefs"
    private const val KEY_TEAM_DETAILS = "savedTeamDetails"

    // Save a list of followed teams
    fun saveFollowedTeams(context: Context, teams: List<TeamDetails>) {
        val prefs = context.getSharedPreferences(PREFS_NAME_DETAILS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(teams)
        editor.putString(KEY_TEAM_DETAILS, json)
        editor.apply()
    }

    // Load the list of followed teams
    fun loadFollowedTeams(context: Context): List<TeamDetails>? {
        val prefs = context.getSharedPreferences(PREFS_NAME_DETAILS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(KEY_TEAM_DETAILS, null)
        return gson.fromJson(json, object : TypeToken<List<TeamDetails>?>() {}.type)
    }

    // ... existing code ...

    private const val PREFS_NAME_CURRENT = "FavoriteTeamPrefs"
    private const val KEY_FAVORITE_TEAM = "saveFavoriteTeam"

    // Save a list of followed teams
    fun saveFavoriteTeams(context: Context, teams: Set<Teams>) {
        val prefs = context.getSharedPreferences(PREFS_NAME_CURRENT, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(teams)
        editor.putString(KEY_FAVORITE_TEAM, json)
        editor.apply()
    }

    // Load the list of followed teams
    fun loadFavoriteTeams(context: Context): Set<Teams>? {
        val prefs = context.getSharedPreferences(PREFS_NAME_CURRENT, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(KEY_FAVORITE_TEAM, null)
        return gson.fromJson(json, object : TypeToken<Set<Teams>?>() {}.type)
    }
}


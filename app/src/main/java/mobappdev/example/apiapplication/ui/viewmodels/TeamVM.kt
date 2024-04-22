package mobappdev.example.apiapplication.ui.viewmodels

import LiveData
import LiveDataEvents
import PastAndUpcomingMatches
import PastMatches
import UpcomingMatches
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobappdev.example.apiapplication.data.FetchLocation
import mobappdev.example.apiapplication.data.TeamDetails
import mobappdev.example.apiapplication.data.TeamStorage
import mobappdev.example.apiapplication.data.TeamsDetails
import mobappdev.example.apiapplication.lookup.LookupTeamClient
import mobappdev.example.apiapplication.lookup.impl.LookupTeamClientImpl
import mobappdev.example.apiapplication.search.MatchesClient
import mobappdev.example.apiapplication.search.impl.MatchDetailsImpl
import mobappdev.example.apiapplication.search.impl.MatchesClientImpl
import mobappdev.example.apiapplication.utils.Result


interface TeamViewModel {
    val team: StateFlow<TeamsDetails?>
    fun fetchTeam(id: Int)
    val upcomingMatches: StateFlow<UpcomingMatches?>
    fun fetchUpcomingMatches(id: Int)
    val pastMatches: StateFlow<PastMatches?>

    fun fetchPastMatches(id: Int)

    val followedTeams: StateFlow<List<TeamDetails>?>
    fun loadFollowedTeams()
    fun followTeam(team: TeamDetails)
    fun unFollowTeam(team: TeamDetails)
    val combinedMatches: StateFlow<Map<Int, PastAndUpcomingMatches>?>
    fun fetchPastAndUpcomingMatches(ids: List<Int>)
}

class TeamVM (application: Application
) : AndroidViewModel(application), TeamViewModel {
    private val lookupTeamClient: LookupTeamClient = LookupTeamClientImpl()
    private val _team = MutableStateFlow<TeamsDetails?>(null)
    override val team: StateFlow<TeamsDetails?> = _team.asStateFlow()



    private val _teamState = MutableStateFlow<Result<String>>(Result.Loading)
    val teamState: StateFlow<Result<String>> = _teamState

    private val matchesClient: MatchesClient = MatchesClientImpl()
    private val _upcomingMatchState = MutableStateFlow<Result<String>>(Result.Loading)
    val upcomingMatchState: StateFlow<Result<String>> = _upcomingMatchState
    private val _upcomingMatches = MutableStateFlow<UpcomingMatches?>(null)
    override val upcomingMatches: StateFlow<UpcomingMatches?> = _upcomingMatches.asStateFlow()

    private val _pastMatchState = MutableStateFlow<Result<String>>(Result.Loading)
    val pastMatchState: StateFlow<Result<String>> = _pastMatchState
    private val _pastMatches = MutableStateFlow<PastMatches?>(null)
    override val pastMatches: StateFlow<PastMatches?> = _pastMatches.asStateFlow()

    private val _followedTeams = MutableStateFlow<List<TeamDetails>?>(null)
    override val followedTeams: StateFlow<List<TeamDetails>?> = _followedTeams.asStateFlow()

    private val _combinedMatchesState = MutableStateFlow<Result<String>>(Result.Loading)
    val combinedMatchesState: StateFlow<Result<String>> = _combinedMatchesState

    private val _combinedMatches = MutableStateFlow<Map<Int, PastAndUpcomingMatches>?>(null)
    override val combinedMatches: StateFlow<Map<Int, PastAndUpcomingMatches>?> = _combinedMatches.asStateFlow()

    private val matchDetailsImpl: MatchDetailsImpl = MatchDetailsImpl()
    private val _liveDataState = MutableStateFlow<Result<String>>(Result.Loading)
    private val _liveData = MutableStateFlow<LiveDataEvents?>(null)
    val liveData: StateFlow<LiveDataEvents?> = _liveData.asStateFlow()

    private val _liveDataTemp = MutableStateFlow(LiveDataEvents(mutableListOf()))
    val liveDataTemp: StateFlow<LiveDataEvents?> = _liveDataTemp.asStateFlow()



    // Method to fetch both past and upcoming matches
    override fun fetchPastAndUpcomingMatches(ids: List<Int>) {
        viewModelScope.launch {
            _combinedMatchesState.value = Result.Loading

            try {
                // Call the existing method in MatchesClientImpl
                val combinedMatchesResult = matchesClient.getPastAndUpcomingMatchesForMultipleTeams(ids)
                if (combinedMatchesResult != null) {
                    _combinedMatches.update { combinedMatchesResult.getOrNull()}
                    Log.e("PAST AND UPCOMING", _combinedMatches.value.toString())
                } else {
                    _combinedMatchesState.value = Result.Error(Exception("Failed to fetch past and upcoming matches"))
                }
            } catch (e: Exception) {
                _combinedMatchesState.value = Result.Error(e)
            }
        }
    }


    // Method to add a followed team
    override fun followTeam(team: TeamDetails) {
        _followedTeams.value = (_followedTeams.value ?: emptyList()) + team
        TeamStorage.saveFollowedTeams(getApplication(), _followedTeams.value!!)
    }
    fun fetchLiveData(){
        viewModelScope.launch {
            _liveDataState.value = Result.Loading
            try {
                val result = matchDetailsImpl.getLiveData()
                Log.e("live data ", "Live DATA $result")
                if (result != null){
                    _liveData.update {
                        result.getOrNull()
                    }
                    val teamName = _team.value?.teams?.get(0)?.strTeam;
                    for (event in _liveData.value?.events!!){
                        if (event.strHomeTeam == teamName || event.strAwayTeam == teamName)
                        {
                            _liveDataTemp.value.events.add(event)
                        }
                    }
                }else{
                    _liveDataState.value = Result.Error(Exception("Failed to fetch liveData"))
                }
            }catch (e: Exception){
                _liveDataState.value = Result.Error(e)
            }
        }
    }

    override fun unFollowTeam(team: TeamDetails) {
        _followedTeams.value = _followedTeams.value?.toMutableList()?.apply {
            remove(team)
        }
        TeamStorage.saveFollowedTeams(getApplication(), _followedTeams.value ?: emptyList())
    }


    // Method to load followed teams
    override fun loadFollowedTeams() {
        Log.e("VMMM", "IM HERE")
        _followedTeams.value = TeamStorage.loadFollowedTeams(getApplication())
        Log.e("TEAMS VMM", _followedTeams.value.toString())
        val teamIds = _followedTeams.value?.map { it.idTeam } ?: emptyList()
        fetchPastAndUpcomingMatches(teamIds)
    }

    override fun fetchTeam(id: Int) {
        viewModelScope.launch {
            _teamState.value = Result.Loading

            try {
                val result = lookupTeamClient.byId(id)
                Log.e("TEEST ", "TEST $result")
                if (result != null) {
                    _team.update { result.getOrNull() }
                    // Save weather
                    //_leagueState.value = Result.Success(result.data.timezone)
                    //WeatherStorage.saveWeather(getApplication<Application>().applicationContext,result.data)
                } else {
                    _teamState.value = Result.Error(Exception("Failed to fetch team"))
                }
            } catch (e: Exception) {
                _teamState.value = Result.Error(e)
            }
        }
    }
   override fun fetchUpcomingMatches(id: Int)
    {
        viewModelScope.launch {
            _upcomingMatchState.value = Result.Loading
            try {
                val result = matchesClient.searchUpcomingMatchesById(id)
                Log.e("upcoming mathces ", "upcoming matches $result")
                if (result != null){
                    _upcomingMatches.update { result.getOrNull() }
                }else{
                    _upcomingMatchState.value = Result.Error(Exception("Failed to fetch past matches"))
                }
            }catch (e: Exception)
            {
                _upcomingMatchState.value = Result.Error(e)
            }
        }
    }

    override fun fetchPastMatches(id: Int)
    {
        viewModelScope.launch {
            _pastMatchState.value = Result.Loading
            try {
                val result = matchesClient.searchPastMatchesById(id)
                Log.e("past mathces ", "past matches $result")
                if (result != null){
                    _pastMatches.update { result.getOrNull() }
                }else{
                    _pastMatchState.value = Result.Error(Exception("Failed to fetch past matches"))
                }
            }catch (e: Exception)
            {
                _pastMatchState.value = Result.Error(e)
            }
        }
    }


}

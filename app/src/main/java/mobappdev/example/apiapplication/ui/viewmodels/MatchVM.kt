package mobappdev.example.apiapplication.ui.viewmodels

import LiveDataEvents
import MatchDetails
import Matches
import Stats
import TimeLines
import UpcomingMatches
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

import mobappdev.example.apiapplication.search.impl.SearchEventClientImpl
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobappdev.example.apiapplication.search.impl.MatchDetailsImpl
import mobappdev.example.apiapplication.search.impl.MatchesClientImpl
import mobappdev.example.apiapplication.utils.Result

interface MatchViewModel{

}
class MatchVM(
application: Application

): AndroidViewModel(application) {
    private val searchEventClient: SearchEventClientImpl = SearchEventClientImpl()
    private val _matchState = MutableStateFlow<Result<String>>(Result.Loading)
    val matchState: StateFlow<Result<String>> = _matchState
    private val _matches = MutableStateFlow<Matches?>(null)
    val matches: StateFlow<Matches?> = _matches.asStateFlow()

    private val upcomingMatchesClientImpl: MatchesClientImpl = MatchesClientImpl()
    private val _upcomingMatchState = MutableStateFlow<Result<String>>(Result.Loading)
    private val _upcomingMatches = MutableStateFlow<UpcomingMatches?>(null)
    val upcomingMatches: StateFlow<UpcomingMatches?> = _upcomingMatches.asStateFlow()

    private val matchDetailsImpl: MatchDetailsImpl = MatchDetailsImpl()
    private val _matchDetailsState = MutableStateFlow<Result<String>>(Result.Loading)
    private val _matchDetails = MutableStateFlow<MatchDetails?>(null)
    val matchDetails: StateFlow<MatchDetails?> = _matchDetails.asStateFlow()

    private val _statsState = MutableStateFlow<Result<String>>(Result.Loading)
    private val _stats = MutableStateFlow<Stats?>(null)
    val stats: StateFlow<Stats?> = _stats.asStateFlow()

    private val _timeLineState = MutableStateFlow<Result<String>>(Result.Loading)
    private val _timeLine = MutableStateFlow<TimeLines?>(null)
    val timeline: StateFlow<TimeLines?> = _timeLine.asStateFlow()


    init {
        fetchaMatch()
        fetchMatchDetails(441613)
    }
    fun fetchUpcomingMatches(id: Int)
    {
        viewModelScope.launch {
            _upcomingMatchState.value = Result.Loading
            try {
                val result = upcomingMatchesClientImpl.searchUpcomingMatchesById(id)
                Log.e("upcoming mathces ", "upcoming matches $result")
                if (result != null){
                    _upcomingMatches.update { result.getOrNull() }
                }else{
                    _upcomingMatchState.value = Result.Error(Exception("Failed to fetch weather"))
                }
            }catch (e: Exception)
            {
                _upcomingMatchState.value = Result.Error(e)
            }
        }
    }


    fun fetchStats(id: Int)
    {
        viewModelScope.launch {
            _statsState.value = Result.Loading
            try {
                val result = matchDetailsImpl.getStats(id)
                Log.e("stat ", "SELECTED STATS $result")

                if (result != null){
                    _stats.update {
                        result.getOrNull()
                    }
                }else{
                    _statsState.value = Result.Error(Exception("Failed to fetch weather"))
                }
            }catch (e: Exception)
            {
                _statsState.value = Result.Error(e)
            }
        }
    }
    fun fetchTimeLine(id: Int)
    {
        viewModelScope.launch {
            _timeLineState.value = Result.Loading
            try {
                val result = matchDetailsImpl.getTimeLine(id)
                Log.e("selected MATCH ", "SELECTED MATCH $result")

                if (result != null){
                    _timeLine.update {
                        result.getOrNull()
                    }
                }else{
                    _timeLineState.value = Result.Error(Exception("Failed to fetch weather"))
                }
            }catch (e: Exception)
            {
                _timeLineState.value = Result.Error(e)
            }
        }
    }
    fun fetchMatchDetails(id: Int)
    {
        viewModelScope.launch {
            _matchDetailsState.value = Result.Loading
            try {
                val result = matchDetailsImpl.getMatchDetail(id)
                Log.e("selected MATCH ", "SELECTED MATCH $result")

                if (result != null){
                    _matchDetails.update {
                        result.getOrNull()
                    }
                }else{
                    _matchDetailsState.value = Result.Error(Exception("Failed to fetch weather"))
                }
            }catch (e: Exception)
            {
                _matchDetailsState.value = Result.Error(e)
            }
        }
    }
    fun fetchaMatch() {
        viewModelScope.launch {
            _matchState.value = Result.Loading

            try {
                val result = searchEventClient.byNameAndSeason("Arsenal_vs_Chelsea", "2016-2017")
                Log.e("TEEST ", "TEST $result")
                if (result != null) {
                    _matches.update { result.getOrNull() }

                } else {
                    _matchState.value = Result.Error(Exception("Failed to fetch weather"))
                }
            } catch (e: Exception) {
                _matchState.value = Result.Error(e)
            }
        }
    }
}

package mobappdev.example.apiapplication.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobappdev.example.apiapplication.data.Leagues
import mobappdev.example.apiapplication.list.ListLeaguesClient
import mobappdev.example.apiapplication.list.impl.ListLeaguesClientImpl
import mobappdev.example.apiapplication.utils.Result


interface LeagueViewModel {
    val leagues: StateFlow<Leagues?>
    fun fetchLeagues()
}

class LeagueVM (application: Application
) : AndroidViewModel(application), LeagueViewModel {
    private val listLeaguesClient: ListLeaguesClient = ListLeaguesClientImpl()
    private val _leagues = MutableStateFlow<Leagues?>(null)
    override val leagues: StateFlow<Leagues?> = _leagues.asStateFlow()

    private val _leaguesState = MutableStateFlow<Result<String>>(Result.Loading)
    val leagueState: StateFlow<Result<String>> = _leaguesState
    init {
        fetchLeagues()
    }
    override fun fetchLeagues() {
        viewModelScope.launch {
            _leaguesState.value = Result.Loading

            try {
                val result = listLeaguesClient.all()
                Log.e("TEEST ", "TEST $result")
                if (result != null) {
                    _leagues.update { result.getOrNull() }
                    // Save weather
                    //_leagueState.value = Result.Success(result.data.timezone)
                    //WeatherStorage.saveWeather(getApplication<Application>().applicationContext,result.data)
                } else {
                    _leaguesState.value = Result.Error(Exception("Failed to fetch weather"))
                }
            } catch (e: Exception) {
                _leaguesState.value = Result.Error(e)
            }
        }    }
}

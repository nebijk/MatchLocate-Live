package mobappdev.example.apiapplication

import FootballScreen
import MatchDetailScreen
import SearchScreen
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mobappdev.example.apiapplication.data.FetchLocation
import mobappdev.example.apiapplication.ui.screens.LiveMatchScreen
import mobappdev.example.apiapplication.ui.screens.TeamScreen
import mobappdev.example.apiapplication.ui.theme.JokeGeneratorTheme
import mobappdev.example.apiapplication.ui.viewmodels.LeagueVM
import mobappdev.example.apiapplication.ui.viewmodels.MatchVM
import mobappdev.example.apiapplication.ui.viewmodels.SearchVM
import mobappdev.example.apiapplication.ui.viewmodels.TeamVM

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokeGeneratorTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val leaguesVM = LeagueVM(application = application)
                    val searchVM = SearchVM(application = application)
                    val teamVM = TeamVM(application = application)
                    val matchVM = MatchVM(application=application)

                    Scaffold(
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                NavHost(navController, startDestination = "home") {
                                    composable("home") {
                                        FootballScreen(vm = teamVM, navController = navController)
                                    }
                                    composable("search") {
                                        // Replace with the content of your search screen
                                        SearchScreen(vm = searchVM, navController = navController)
                                    }
                                    composable("Live"){
                                        LiveMatchScreen(vm = teamVM)
                                    }

                                    composable(
                                        route = "team/{teamId}",
                                        arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                                    ) { backStackEntry ->
                                        val teamId = backStackEntry.arguments?.getString("teamId")
                                        if (teamId != null) {
                                            TeamScreen(vm = teamVM, teamId = teamId.toInt(), navController = navController)
                                        } else {
                                            // Handle the case where teamId is null
                                            Text("Error: Invalid teamId")
                                        }
                                    }
                                    composable(
                                        route = "match/{matchId}?imageA={imageA}&imageB={imageB}&imageC={imageC}",
                                        arguments = listOf(
                                            navArgument("matchId") { type = NavType.StringType },
                                            navArgument("imageA") { type = NavType.StringType },
                                            navArgument("imageB") { type = NavType.StringType },
                                            navArgument("imageC") { type = NavType.StringType }
                                        )
                                    ) { backStackEntry ->
                                        val matchId = backStackEntry.arguments?.getString("matchId")
                                        val imageA = backStackEntry.arguments?.getString("imageA") ?: ""
                                        val imageB = backStackEntry.arguments?.getString("imageB") ?: ""
                                        val imageC = backStackEntry.arguments?.getString("imageC") ?: ""

                                        if (matchId != null) {
                                            MatchDetailScreen(vm = matchVM, matchId = matchId.toInt(), awayTeamBadge = imageA, homeTeamBadge = imageB, teamBadge = imageC)
                                        } else {
                                            // Handle the case where matchId is null
                                            Text("Error: Invalid matchId")
                                        }
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            Spacer(modifier = Modifier.height(56.dp))
                            NavigationBar {
                                val items = listOf("Home", "Search", "Live matches")
                                var selectedItem by remember { mutableStateOf(0) }

                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        icon = {
                                            when (index) {
                                                0 -> Icon(
                                                    Icons.Default.Home,
                                                    contentDescription = item
                                                )

                                                1 -> Icon(
                                                    Icons.Default.Search,
                                                    contentDescription = item
                                                )

                                                2 -> Icon(
                                                    Icons.Default.PlayArrow, // Choose an appropriate icon for "Live matcher"
                                                    contentDescription = item
                                                )

                                                else -> Icon(
                                                    Icons.Default.Info,
                                                    contentDescription = item
                                                )
                                            }
                                        },
                                        label = { Text(item) },
                                        selected = selectedItem == index,
                                        onClick = {
                                            if (selectedItem != index) {
                                                selectedItem = index
                                                when (index) {
                                                    0 -> navController.navigate("home")
                                                    1 -> navController.navigate("search")
                                                    2 -> navController.navigate("Live")
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JokeGeneratorTheme {
        Greeting("Android")
    }
}


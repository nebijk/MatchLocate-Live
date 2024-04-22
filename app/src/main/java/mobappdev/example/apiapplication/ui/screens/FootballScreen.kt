import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import mobappdev.example.apiapplication.data.TeamDetails
import mobappdev.example.apiapplication.ui.screens.BadgeImage
import mobappdev.example.apiapplication.ui.viewmodels.TeamVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FootballScreen(
    vm: TeamVM,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        vm.loadFollowedTeams()
    }
    // Define color palette
    val primaryColor = Color(0xFF1976D2)
    val secondaryColor = Color(0xFF90CAF9)
    val textColor = Color.Black
    val teams = vm.followedTeams.collectAsState()
    val combinedMatches = vm.combinedMatches.collectAsState()

    // Apply color scheme
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            onPrimary = textColor,
            onSecondary = textColor
        )
    ) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            // Landscape orientation
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(primaryColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    teams.value?.let { DisplayFollowedTeams(it, vm, combinedMatches.value) }
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        } else {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(primaryColor)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.05f)
                            .fillMaxHeight()
                    ) {
                        FootballHeader(navController = navController)
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        teams.value?.let { DisplayFollowedTeams(it, vm, combinedMatches.value) }
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayFollowedTeams(teams: List<TeamDetails>, vm: TeamVM, combinedMatches: Map<Int, PastAndUpcomingMatches>?) {
    LazyColumn {
        items(teams) { team ->
            // Display team name
            TeamCard(team = team, vm = vm, combinedMatches?.get(team.idTeam))
        }
    }
}

@Composable
fun TeamCard(team: TeamDetails, vm: TeamVM, pastAndUpcomingMatches: PastAndUpcomingMatches?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display team name and badge
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BadgeImage(badge = team.strTeamBadge, teamBadge = team.strTeamBadge, size = 48.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = team.strTeam,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            // Display upcoming and past matches
            pastAndUpcomingMatches?.let { combinedMatches ->
                if (combinedMatches.results.results.isNotEmpty()) {

                    val pastMatch = combinedMatches.results.results[0]

                    PastMatchCard(match = pastMatch, teamBadge = team.strTeamBadge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (combinedMatches.events.events.isNotEmpty()) {

                    val upcomingMatch = combinedMatches.events.events[0]
                    UpcomingMatchCard(match = upcomingMatch, teamBadge = team.strTeamBadge)
                }

            }
        }
    }
}

@Composable
fun UpcomingMatchCard(match: UpcomingMatch, teamBadge: String) {
    Column {
        // Display home and away team badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgeImage(match.strHomeTeamBadge, teamBadge, size = 48.dp)
            Text(
                text = match.strTime.substring(0,5),
                modifier = Modifier.align(Alignment.CenterVertically),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            BadgeImage(match.strAwayTeamBadge, teamBadge, size = 48.dp)
        }

        // Display individual match details
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${match.strHomeTeam} vs ${match.strAwayTeam}")
                }
            },
            onClick = { offset ->
                // Handle click on team names if needed
            },
        )
    }
}

@Composable
fun PastMatchCard(match: PastMatch, teamBadge: String) {
    Column {
        // Display home and away team badges and scores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgeImage(match.strHomeTeamBadge, teamBadge, size = 48.dp)
            Text(
                text = "${match.intHomeScore} - ${match.intAwayScore}",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            BadgeImage(match.strAwayTeamBadge, teamBadge, size = 48.dp)
        }

        // Display individual match details
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${match.strHomeTeam} vs ${match.strAwayTeam}")
                }
            },
            onClick = { offset ->
                // Handle click on team names if needed
            },
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FootballHeader(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Home",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

            }
        }
    )
}

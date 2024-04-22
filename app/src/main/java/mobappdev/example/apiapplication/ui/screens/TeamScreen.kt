package mobappdev.example.apiapplication.ui.screens

import LiveData
import LiveDataEvents
import PastMatch
import PastMatches
import UpcomingMatch
import UpcomingMatches
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import mobappdev.example.apiapplication.ui.viewmodels.TeamVM
import mobappdev.example.apiapplication.data.TeamDetails

@Composable
fun TeamScreen(vm: TeamVM, teamId: Int,navController: NavController) {
    LaunchedEffect(teamId) {
        vm.fetchTeam(teamId)
        vm.fetchPastMatches(teamId)
        vm.fetchUpcomingMatches(teamId)
        vm.fetchLiveData()
    }
    val primaryColor = Color(0xFF1976D2)
    val textColor = Color.White
    val teamDetails = vm.team.collectAsState()
    val upcomingMatches = vm.upcomingMatches.collectAsState()
    val pastMatches = vm.pastMatches.collectAsState()
    val teamBadge = teamDetails.value?.teams?.get(0)?.strTeamBadge
    val followingTeams = vm.followedTeams.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor)
    ) {
        teamDetails.value?.let { team ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(team.teams.size) { index ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(16.dp)
                        ) {
                            // Display team badge
                            Image(
                                painter = rememberImagePainter(data = team.teams[index].strTeamBadge),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(16.dp)
                            )
                            Text(
                                text = team.teams[index].strTeam,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }

                        // Display team name
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(16.dp)
                        ) {
                            // Display team details beside the badge and team name
                            TeamDetailsSection(team = team.teams[index])
                        }
                    }

                    val isFollowing = followingTeams.value != null && followingTeams.value?.contains(team.teams[0]) == true
                    Button(
                        onClick = {
                            // Toggle the follow state
                            if (!isFollowing) {
                                Log.e("INSERTING", followingTeams.value.toString())
                                vm.followTeam(team.teams[0])
                            }
                            if (isFollowing){
                                vm.unFollowTeam(team.teams[0])
                            }
                            // You can add additional logic here, such as making an API call to update the follow status
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowing) Color.Gray else Color.Cyan,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = if (isFollowing) "Following" else "Follow")
                    }                }
            }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(0.2f)
            .fillMaxHeight()
            .background(primaryColor)
        )  {
            pastMatches.value?.let { past ->
                if (past.results.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        PastMatchesScreen(pastMatches = past, teamId = teamId, teamBadge = teamBadge.toString(),navController)
                    }
                } else {
                    Text(
                        text = "No upcoming matches available",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = textColor
                    )
                }
            }
        }

        /*Column(modifier = Modifier
            .fillMaxSize()
            .weight(0.2f)
            .fillMaxHeight()
            .background(primaryColor)
        ) {
            upcomingMatches.value?.let { upcoming ->
                if (upcoming.events.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        UpcomingMatchesScreen(
                            upcomingMatches = upcoming,
                            teamBadge = teamBadge.toString()
                        )
                    }
                } else {
                    Text(
                        text = "No upcoming matches available",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = textColor
                    )
                }
            }
        }*/
    }
}

@Composable
fun TeamDetailsSection(team: TeamDetails) {
        // Display other team details here...
        TeamDetailItem(label = "Country", value = team.strCountry)
        TeamDetailItem(label = "Formed Year", value = team.intFormedYear)
        TeamDetailItem(label = "League", value = team.strLeague)
        TeamDetailItem(label = "Stadium", value = team.strStadium)
        // Add more details as needed...
}

@Composable
fun TeamDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}


@Composable
fun UpcomingMatchesScreen(upcomingMatches: UpcomingMatches, teamBadge: String) {
    upcomingMatches.let { matches ->
        LazyColumn {
            items(matches.events) { match ->
                UpcomingMatchItem(match = match, teamBadge = teamBadge)
            }
        }
    }
}
@Composable
fun liveMatchItem(match: LiveDataEvents, teamBadge: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display home team badge as an image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                BadgeImage(match.events.get(0).strHomeTeamBadge, teamBadge, 100.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.events.get(0).intHomeScore.toString(),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "-",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.events.get(0).intAwayScore.toString(),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BadgeImage(match.events.get(0).strAwayTeamBadge, teamBadge, 100.dp)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Red)
                        .align(Alignment.CenterVertically)
                )

            }

            // Display individual match details
            Spacer(modifier = Modifier.height(16.dp))
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${match.events.get(0).strHomeTeam} vs ${match.events.get(0).strAwayTeam}")
                    }
                },
                onClick = { offset -> },
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PastMatchItem(match: PastMatch, teamBadge: String,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display home team badge as an image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                BadgeImage(match.strHomeTeamBadge, teamBadge, 100.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.intHomeScore.toString(),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "-",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.intAwayScore.toString(),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BadgeImage(match.strAwayTeamBadge, teamBadge, 100.dp)
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
                    navController.navigate("match/${match.idEvent}?imageA=${match.strAwayTeamBadge}&imageB=${match.strHomeTeamBadge}&imageC=${teamBadge}")
                },
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            //MatchDetailItem(Icons.Default.Place, match.strVenue)
            //MatchDetailItem(Icons.Default.DateRange, match.dateEvent)
            //MatchDetailItem(Icons.Default.Info, match.strTime)
        }
    }
}

@Composable
fun UpcomingMatchItem(match: UpcomingMatch, teamBadge: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {},
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display home team badge as an image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                BadgeImage(match.strHomeTeamBadge, teamBadge, 100.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "-",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BadgeImage(match.strAwayTeamBadge, teamBadge, 100.dp)
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
                //modifier = Modifier.padding(8.dp)
            )
            //Spacer(modifier = Modifier.height(8.dp))

            //MatchDetailItem(Icons.Default.Place, match.strVenue)
            //MatchDetailItem(Icons.Default.DateRange, match.dateEvent)
            //MatchDetailItem(Icons.Default.Info, match.strTime)
        }
    }
}

@Composable
fun BadgeImage(badge: String, teamBadge: String, size: Dp) {
    Image(
        painter = if (badge == "BadgeURL") {
            rememberImagePainter(data = teamBadge)
        } else {
            rememberImagePainter(data = badge)
        },
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .padding(16.dp),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun MatchDetailItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = Color.Gray,
            modifier = Modifier.padding(4.dp)
        )
    }
}


fun getMatchResultColor(match: PastMatch, teamId: Int): Color {
    return when {
        match.intHomeScore > match.intAwayScore && match.idHomeTeam == teamId -> Color.Green
        match.intAwayScore > match.intHomeScore && match.idAwayTeam == teamId -> Color.Green
        match.intHomeScore == match.intAwayScore -> Color.Gray
        else -> Color.Red
    }
}

fun getMatchResultIcon(match: PastMatch, teamId: Int): String {
    return when {
        match.intHomeScore > match.intAwayScore && match.idHomeTeam == teamId -> "W"
        match.intAwayScore > match.intHomeScore && match.idAwayTeam == teamId -> "W"
        match.intHomeScore == match.intAwayScore -> "D"
        else -> "L"
    }
}



@Composable
fun MatchResultCircle(index: Int, match: PastMatch, teamId: Int, onClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(getMatchResultColor(match, teamId))
            .clickable {
                onClick(index) // Pass the index of the clicked match
            }
    ) {
        // Display V, O, or F based on match result
        Text(
            text = getMatchResultIcon(match, teamId),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PastMatchesScreen(pastMatches: PastMatches, teamId: Int, teamBadge: String,navController: NavController) {
    val scope = rememberCoroutineScope()
    val selectedIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        initialPage = selectedIndex,
        initialPageOffsetFraction = 0f
    ) {
        pastMatches.results.size
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            reverseLayout = true
        ) { page ->
            PastMatchItem(match = pastMatches.results[page], teamBadge = teamBadge,navController)
        }
        // Display circles for each past match
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            reverseLayout = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            items(pastMatches.results.size) { index ->
                MatchResultCircle(
                    index = index,
                    match = pastMatches.results[index],
                    teamId = teamId,
                    onClick = { clickedIndex ->
                        scope.launch {
                            pagerState.animateScrollToPage(clickedIndex)
                        }
                    }
                )
            }
        }
    }
}

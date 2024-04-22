import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import coil.compose.rememberImagePainter
import mobappdev.example.apiapplication.ui.viewmodels.MatchVM

enum class DetailMode {
    STATS, TIMELINES
}
@Composable
fun MatchDetailScreen(vm: MatchVM, matchId: Int, awayTeamBadge: String ,homeTeamBadge: String,teamBadge: String)
{
    LaunchedEffect(matchId){
        vm.fetchMatchDetails(matchId)
        vm.fetchStats(matchId)
        vm.fetchTimeLine(matchId)
    }
    val matchDetails = vm.matchDetails.collectAsState()
    val matchStats = vm.stats.collectAsState()
    val matchTimeLine = vm.timeline.collectAsState()
    var detailMode by remember { mutableStateOf(DetailMode.STATS) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {},
    ) {
        // Match title
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(
                        "${matchDetails.value?.events?.get(0)?.strHomeTeam} vs ${
                            matchDetails.value?.events?.get(0)?.strAwayTeam
                        }"
                    )
                }
            },
            onClick = { offset -> /* Handle click event if needed */ },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        // Row with team badges and scores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgeImage(homeTeamBadge, teamBadge)
            Text(
                text = matchDetails.value?.events?.get(0)?.intHomeScore.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            Text(
                text = "-",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            Text(
                text = matchDetails.value?.events?.get(0)?.intAwayScore.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            BadgeImage(awayTeamBadge, teamBadge)
        }

        // Match details
        matchDetails.value?.events?.get(0)?.let { event ->
            Text(
                text = "${event.dateEvent}" ?: "",
                modifier = Modifier.padding(vertical = 4.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            Text(
                text = "${event.strLeague}" ?: "",
                modifier = Modifier.padding(vertical = 4.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            Text(
                text =  "${event.strVenue}" ?: "",
                modifier = Modifier.padding(vertical = 4.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            Text(
                text = "${event.strSeason}" ?: "",
                modifier = Modifier.padding(vertical = 4.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
            Text(
                text = "Round ${event.intRound.toString()}" ,
                modifier = Modifier.padding(vertical = 4.dp),
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
        }

        // Display individual match details
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                detailMode = when (detailMode) {
                    DetailMode.STATS -> DetailMode.TIMELINES
                    DetailMode.TIMELINES -> DetailMode.STATS
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = if (detailMode == DetailMode.STATS) "Timeline" else "Matchstats"
            )
        }

        // Display individual match details
        Spacer(modifier = Modifier.height(16.dp))
        when (detailMode) {
            DetailMode.STATS -> matchStats.value?.let { MatchStats(stats = it) }
            DetailMode.TIMELINES -> matchTimeLine.value?.let { TimeLineList(timeLines = it) }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun BadgeImage(badge: String, teamBadge: String) {
    Image(
        painter = if (badge == "BadgeURL") {
            rememberImagePainter(data = teamBadge)
        } else {
            rememberImagePainter(data = badge)
        },
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MatchStats(stats: Stats) {
    val eventStats = stats.eventstats

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(eventStats) { stat ->
            StatItem(stat)
        }
    }
}

@Composable
fun StatItem(stat: Stat) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = stat.strStat,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Home: ${stat.intHome}",
                fontSize = 16.sp
            )
            Text(
                text = "Away: ${stat.intAway}",
                fontSize = 16.sp
            )
        }
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
fun TimeLineList(timeLines: TimeLines) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "Timeline",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(timeLines.timeline) { timeLine ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "${timeLine.intTime}'",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .width(40.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = "${timeLine.strTeam}: ${timeLine.strTimelineDetail}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (timeLine.strAssist.isNotEmpty() && timeLine.strTimeline.contains("Goal")) {
                        Text(
                            text = "Scorer: ${timeLine.strPlayer}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Assist: ${timeLine.strAssist}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    if (timeLine.strTimeline.contains("subst")) {
                        Text(
                            text = "In: ${timeLine.strAssist}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Out: ${timeLine.strPlayer}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    if (timeLine.strTimelineDetail.contains("Yellow")) {
                        Text(
                            text = "${timeLine.strPlayer}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
            Divider(color = Color.Gray, modifier = Modifier.fillMaxWidth())
        }
    }
}


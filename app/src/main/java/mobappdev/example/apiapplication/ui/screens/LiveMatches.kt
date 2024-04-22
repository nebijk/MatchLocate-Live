package mobappdev.example.apiapplication.ui.screens

import LiveData
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import coil.compose.rememberImagePainter
import mobappdev.example.apiapplication.ui.viewmodels.TeamVM
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect

@Composable
fun LiveMatchScreen(vm: TeamVM)
{
    LaunchedEffect(Unit) {
        vm.fetchLiveData()
    }

    val liveMatches = vm.liveData.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        liveMatches.value?.events?.let {
            items(it.size) { index ->
                val liveData = liveMatches.value!!.events[index]
                LiveItem(liveData)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}
@Composable
fun LiveItem(match: LiveData) {
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
                BadgeImage(match.strHomeTeamBadge, 100.dp)
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
                BadgeImage(match.strAwayTeamBadge, 100.dp)
            }

            // Display individual match details
            Spacer(modifier = Modifier.height(16.dp))
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${match.strHomeTeam} vs ${match.strAwayTeam}")
                    }
                },
                onClick = { offset -> },
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
fun BadgeImage(badge: String, size: Dp) {
    Image(

        rememberImagePainter(data = badge),

        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp),
        contentScale = ContentScale.Crop
    )
}

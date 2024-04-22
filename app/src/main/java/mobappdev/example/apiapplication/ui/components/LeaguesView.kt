import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobappdev.example.apiapplication.ui.viewmodels.LeagueVM

@Composable
fun LeaguesView(
    vm: LeagueVM
) {

    // Define color palette
    val primaryColor = Color(0xFF1976D2)
    val secondaryColor = Color(0xFF90CAF9)
    val textColor = Color.Black
    val leagues = vm.leagues.collectAsState()
    // Apply color sche
        //val configuration = LocalConfiguration.current
        //val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            // Landscape orientation
    leagues.value?.let { league ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = primaryColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(league.countries.size) { index ->
                // Display league information in a vertically scrollable way
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = league.countries[index].strLeague,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = textColor
                    )

                    Text(
                        text = "Sport: ${league.countries[index].strSport}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )

                    Text(
                        text = "Alternate Name: ${league.countries[index].strLeagueAlternate}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )

                    // Add a Divider to separate league entries
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // Set width to 80% of the parent
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

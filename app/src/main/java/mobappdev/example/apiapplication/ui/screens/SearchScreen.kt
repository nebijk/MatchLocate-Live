import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.android.gms.location.LocationServices
import mobappdev.example.apiapplication.data.Teams
import mobappdev.example.apiapplication.ui.viewmodels.SearchVM
import mobappdev.example.apiapplication.data.TeamDetails
import java.util.Locale


@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(vm: SearchVM, navController: NavController) {
    // Define color palette
    val primaryColor = Color(0xFF1976D2)
    val secondaryColor = Color(0xFF90CAF9)
    val textColor = Color.Black
    val teams = vm.teams.collectAsState()
    val locationTeams = vm.cityTeams.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Teams", "Nearby you")

    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var locationPermissionGranted by remember { mutableStateOf(false) }
    // Request location permissions
    DisposableEffect(context) {
        locationPermissionGranted = (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                )

        if (!locationPermissionGranted) {
            // Request permissions
            requestPermissions(context as Activity)
        }

        onDispose { }
    }
    // Apply color scheme
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            onPrimary = textColor,
            onSecondary = textColor
        )
    ) {
        // Add tabs to select between Teams and Locations outside of Scaffold
        NavigationBar(
            contentColor = textColor
        ) {
            tabs.forEachIndexed { index, title ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        if (selectedTab == 1) { // Check if the "Locations" tab is selected
                            if (locationPermissionGranted) {
                                // Get the location only if permission is granted
                                locationClient.lastLocation
                                    .addOnSuccessListener { location ->
                                        // Handle location here
                                        // Get city and country from location
                                        val addresses: List<Address>? = Geocoder(context, Locale.getDefault()).getFromLocation(
                                            location.latitude,
                                            location.longitude,
                                            1
                                        )

                                        if (!addresses.isNullOrEmpty()) {
                                            val cityPart = addresses[0].getAddressLine(0).split(",")[1].trim()
                                            val city = cityPart.split(" ").lastOrNull()
                                            val country = addresses[0].countryName

                                            // Set current city and country in ViewModel
                                            vm.setCurrentLocation(city.toString(), country)

                                            vm.fetchTeamByCountry(country.toString())
                                            // Fetch location-based teams with new values
                                            vm.filterTeamsByCity(city.toString())
                                            Log.e("LOCATION", addresses.toString())
                                            Log.e("LOCATION", city.toString())
                                        }

                                    }
                            } else {
                                // Request permissions if not granted
                                requestPermissions(context as Activity)
                            }
                        }
                    },
                    icon = { Icons.Default.Search },
                    label = { Text(text = title) }
                )
            }
        }

        // Use Box to overlay content over the NavigationBar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp) // Adjust top padding based on your needs
        ) {
            when (selectedTab) {
                0 -> DisplayTeams(teams.value, navController, vm)
                1 -> DisplayLocationTeams(locationTeams.value, navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForm(vm: SearchVM) {
    var query by remember { mutableStateOf(TextFieldValue()) }

    Column {
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(text = "Search for teams")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                vm.fetchTeams(query.text)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Search")
        }
    }
}
@Composable
fun DisplayTeams(teams: Teams?, navController: NavController,vm: SearchVM) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Search Form
        SearchForm(vm)

        // Display teams information
        teams?.teams?.let { teamsList ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(teamsList.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                // Handle click on the team
                                navController.navigate("team/${teamsList[index].idTeam}")
                            }
                    ) {
                        Image(
                            painter = rememberImagePainter(data = teams.teams[index].strTeamBadge),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp) // Adjust the size as needed
                                .padding(vertical = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    // Handle click on the team
                                    navController.navigate("team/${teamsList[index].idTeam}")
                                }
                        ) {
                            Text(
                                text = teamsList[index].strTeam,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Color.Black
                            )
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                    // Display teams information in a vertically scrollable way

                }
            }
        }
    }
}

@Composable
fun DisplayLocationTeams(locationTeams: List<TeamDetails>?,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Teams nearby you",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Display location-based teams information
        locationTeams?.let { teams ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(teams.size) { index ->
                    // Display teams information in a vertically scrollable way
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                // Handle click on the team
                                navController.navigate("team/${locationTeams[index].idTeam}")
                            }
                    ) {
                        Image(
                            painter = rememberImagePainter(data = locationTeams[index].strTeamBadge),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp) // Adjust the size as needed
                                .padding(vertical = 8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    // Handle click on the team
                                    navController.navigate("team/${locationTeams[index].idTeam}")
                                }
                        ) {
                            Text(
                                text = locationTeams[index].strTeam,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Color.Black
                            )
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun requestPermissions(activity: Activity) {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    ActivityCompat.requestPermissions(activity, permissions, 42)
}
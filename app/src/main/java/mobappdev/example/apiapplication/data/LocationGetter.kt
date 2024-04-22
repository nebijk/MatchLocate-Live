package mobappdev.example.apiapplication.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.util.Locale
import java.util.concurrent.Executor

class LocationGetter(
    private val context: Context
) {

    private var location: Location? = null

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Handle location here
                this.location = location

                if (location != null) {
                    // Use the obtained latitude and longitude to get city and country
                    val addresses: List<Address>? = Geocoder(context, Locale.getDefault()).getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    Log.e("ADDDREESS", addresses.toString())
                    if (addresses != null && addresses.isNotEmpty()) {
                        val addressLine = addresses[0].getAddressLine(0)
                        val parts = addressLine.split(",") // Split the address using comma as a delimiter
                        val cityPart = parts.getOrNull(1)?.trim() // Take the third part (index 2) and trim any leading/trailing spaces
                        val city = cityPart?.split(" ")?.lastOrNull()
                        val country = addresses[0].countryName

                        // Now you have the city and country, you can update your ViewModel or perform any other actions
                        Log.d("LocationGetter", "City: $city, Country: $country")
                    } else {
                        Log.e("LocationGetter", "No address found for the given location.")
                    }
                }
            }.addOnFailureListener { e ->
                // Handle failure
                Log.e("LocationGetter", "Error getting location", e)
            }
    }

    fun handleLocation(location: Location?) {
        Log.e("PAULLA", location.toString())
        // Handle location here, update ViewModel or perform other actions
        // ...
    }

}

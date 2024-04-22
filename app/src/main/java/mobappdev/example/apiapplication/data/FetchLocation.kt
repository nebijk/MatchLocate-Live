package mobappdev.example.apiapplication.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import java.util.Locale

class FetchLocation(private val context: Context) : LocationListener {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val _city= MutableStateFlow<String?>(null);
    val city: StateFlow<String?> = _city.asStateFlow()


    private val _country= MutableStateFlow<String?>(null);
    val country: StateFlow<String?> = _country.asStateFlow()

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Access location data (latitude and longitude)
                    val lat = it.latitude
                    val lng = it.longitude
                    Log.e( "TAG",  "cityyyy $lat")
                    Log.e( "TAG",  "lng $lng")

                    // Use Google Maps Geocoding API to get address details
                }
            }
            .addOnFailureListener { e: Exception ->
                println("ewjpifoewpojgwpojgpoiwejowejfowjgowiej")
                e.printStackTrace()
            }
    }

    private fun getAddressDetails(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? =
                geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                     _city.value = addresses[0]?.locality
                     _country.value = addresses[0]?.countryName
                    // Handle city and country details here
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }


}
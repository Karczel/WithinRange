package org.karczelapp.withinrange

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import org.karczelapp.withinrange.dataclass.PathHistory
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

val mockHistory = listOf(
    PathHistory(GeoPoint(13.7441, 100.5321), Timestamp.now()),
    PathHistory(GeoPoint(13.7450, 100.5370), Timestamp.now()),
    PathHistory(GeoPoint(13.7460, 100.5340), Timestamp.now()),
)

@Composable
fun PathHistoryScreen(modifier: Modifier = Modifier) {

    val screenContext = LocalContext.current
    val locationProvider = LocationServices.getFusedLocationProviderClient(screenContext)

    var latValue:Double? by remember { mutableStateOf(13.7461) }
    var lonValue:Double? by remember { mutableStateOf(100.5341) }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            latValue = p0.lastLocation?.latitude
            lonValue = p0.lastLocation?.longitude
        }
    }

    val permissionDialog = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted: Boolean ->
            if(isGranted){
                /* Get User location */
                getCurrentUserLocation(locationProvider, locationCallback)
            }
        }
    )

    DisposableEffect(key1 = locationProvider) {
        val permissionStatus = ContextCompat.checkSelfPermission( screenContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(permissionStatus==PackageManager.PERMISSION_GRANTED){
            /* Get user location */
            getCurrentUserLocation(locationProvider, locationCallback)
        } else {
            permissionDialog.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        onDispose {
            // remove observer and clean
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }

    WithinRangeTheme {
        Surface (
            modifier = Modifier.fillMaxSize().padding(4.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column ( modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally){
                if(latValue!=null && lonValue!=null)
                    MapDisplayWithPath(lat = latValue?: 13.74466, lon = lonValue?: 100.53291, history = mockHistory)
                else MapDisplayWithPath()
            }
        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(location: LatLng) = animate(
    update = CameraUpdateFactory.newLatLngZoom(location, 13f),
    durationMs = 1500
)

@Composable
fun MapDisplayWithPath(lat:Double = 13.74466, lon:Double = 100.53291,
                       zoomLevel:Float = 13f, mapType: MapType = MapType.NORMAL,
                       history: List<PathHistory> = emptyList())
{
    val location = LatLng(lat, lon)
    val selectedLocation = remember { mutableStateOf(LatLng(lat, lon)) }
    val currentLocation = LatLng(lat, lon) //get current location from auth.current user

    val cameraState = rememberCameraPositionState()
    val hasMovedCamera = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = location) {
        if (!hasMovedCamera.value) {
            cameraState.move(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
            hasMovedCamera.value = true
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(mapType = mapType),
        cameraPositionState = cameraState,
        onMapClick = { latLng ->
            // Handle map click
            selectedLocation.value = latLng
        }
    ) {
        // Polyline for path history
        if (history.size >= 2) {
            Polyline(
                points = history.map {
                    LatLng(it.location.latitude, it.location.longitude)
                },
                color = androidx.compose.ui.graphics.Color.Blue,
                width = 6f
            )
        }

        // Start and end markers
        history.firstOrNull()?.let {
            Marker(
                state = MarkerState(position = LatLng(it.location.latitude, it.location.longitude)),
                title = "Start",
                snippet = "Path started here"
            )
        }

        history.lastOrNull()?.let {
            Marker(
                state = MarkerState(position = LatLng(it.location.latitude, it.location.longitude)),
                title = "Last",
                snippet = "Most recent path point"
            )
        }


        // Current location marker
        Marker(
            state = MarkerState(position = currentLocation),
            title = "Current Location",
            snippet = "You are here"
        )

        // Show marker at selected location
        Marker(
            state = MarkerState(position = selectedLocation.value),
            title = "This",
            snippet = "Selected Location"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PathHistoryScreenPreview() {
    PathHistoryScreen()
}

@SuppressLint("MissingPermission")
private fun getCurrentUserLocation(localProvider : FusedLocationProviderClient,
                                   locationCb : LocationCallback)
{
    val locationRequest = LocationRequest.Builder( Priority.PRIORITY_HIGH_ACCURACY, 0 ).build()
    localProvider.requestLocationUpdates(locationRequest, locationCb, null)
}
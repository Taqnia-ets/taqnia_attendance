package com.example.taqniaattendance.util

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.taqniaattendance.App
import com.example.taqniaattendance.R
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

object LocationUtil {

    private var mLocationCallback: LocationCallback? = null

    private val TAG = LocationUtil::class.java.simpleName

    /**
     * This will be used only when provider has request and doing work to draw his marker on the map.
     *
     * It is the responsibility of the caller to call [stopCurrentLocationUpdates] after finishing using
     * this function to prevent memory leak and battery consumption.
     */
    fun getCurrentLocation(
        context: AppCompatActivity,
        body: (newLocation: Location?) -> Unit
    ) {
        // Permission Checks.
        if (!PermissionUtil.isLocationPermissionGranted(context)) {
            PermissionUtil.requestLocationPermission(context, Constants.UserLocation.REQUEST_CODE_LOCATION)
            return
        }
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // todo these should be the same as what is used with location service.
            interval = 0
            fastestInterval = interval / 2
        }

        val builder = LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
        }

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(builder.build())

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?)
                    = body(locationResult?.lastLocation)
        }

        getFusedLocationProviderClient(context)?.requestLocationUpdates(
            locationRequest,
            mLocationCallback,
            Looper.myLooper()
        )

        LogsUtil.printErrorLog(TAG, "Current Location Updates STARTED Successfully.")
    }

    fun stopCurrentLocationUpdates(context: AppCompatActivity) =
        mLocationCallback?.let {
            getFusedLocationProviderClient(context)?.removeLocationUpdates(it)
            LogsUtil.printErrorLog(TAG, "Current Location Updates STOPPED Successfully.")
        }

    fun getLastKnownLocation(
        context: AppCompatActivity,
        body: (location: Location?) -> Unit
    ) {
        // Permission Checks.
        if (!PermissionUtil.isLocationPermissionGranted(context)) {
            PermissionUtil.requestLocationPermission(context, Constants.UserLocation.REQUEST_CODE_LOCATION)
            return
        }
        getFusedLocationProviderClient(context)
            .lastLocation?.addOnCompleteListener(context) {
                it.run {
                    if (isSuccessful && result != null) body(result)
                    else body(null)
                }
        }
    }

    /**
     * This will get the location button from the mapview and place it in specific place.
     */
    fun changeLocationBtnPosition(mapView: View?) {
        val view = mapView?.findViewById<View>(Integer.parseInt("1"))
        val locationBtn = (view?.parent as View).findViewById<View>(Integer.parseInt("2"))
        locationBtn.apply {
            (layoutParams as RelativeLayout.LayoutParams).apply {
                addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                setMargins(0, 0, 0, 770)
            }
            (this as ImageView).setImageResource(R.drawable.ic_gps)
        }
    }

    fun navigateUser(context: Context, latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return
        val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", latitude, longitude)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        context.startActivity(intent)
    }

    /**
     * Get Location address by lat and lng
     * @param lat latitude
     * @param lng longitude
     * @return
     */
    fun getLocationName(lat: Double?, lng: Double?): String {
        val gcd = Geocoder(App.getInstance(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = gcd.getFromLocation(
                lat ?: return "",
                lng ?: return "",
                1
            )
            return if (addresses != null && addresses.isNotEmpty())
                addresses[0].getAddressLine(0) else ""
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }
}

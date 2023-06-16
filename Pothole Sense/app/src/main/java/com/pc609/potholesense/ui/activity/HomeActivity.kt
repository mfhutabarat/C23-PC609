package com.pc609.potholesense.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pc609.potholesense.R

class HomeActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var profileLayout: LinearLayout
    private lateinit var issueLayout: LinearLayout
    private lateinit var predictLayout: LinearLayout
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.setTitle(R.string.issue_activity_home)

        profileLayout = findViewById(R.id.profileLL)
        issueLayout = findViewById(R.id.issueLL)
        predictLayout = findViewById(R.id.predictLL)

        profileLayout.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        issueLayout.setOnClickListener {
            startActivity(Intent(this, IssueActivity::class.java))
        }

        predictLayout.setOnClickListener {
            startActivity(Intent(this, PredictActivity::class.java))
        }

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchLocation(query)
            }
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchLocation(query)
                }
                true
            } else {
                false
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMap()
    }

    private fun setupMap() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun enableLocation() {
        // Mengaktifkan penggunaan lokasi pada peta
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

            // Mendapatkan lokasi pengguna saat ini dan memperbarui peta
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                this
            )

            // Dapatkan lokasi terakhir yang diketahui dan perbarui peta
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation?.let {
                updateLocation(it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation()
            } else {
                // Izin lokasi ditolak atau tidak diberikan
                Toast.makeText(
                    this,
                    "Izin lokasi ditolak. Fitur penentuan lokasi tidak akan berfungsi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun searchLocation(query: String) {
        val latitude = -6.2088
        val longitude = 106.8456

        val markerOptions = MarkerOptions()
            .position(LatLng(latitude, longitude))
            .title(query)
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                markerOptions.position,
                DEFAULT_ZOOM
            )
        )
    }

    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }

    private fun updateLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        googleMap.clear()

        val markerOptions = MarkerOptions()
            .position(currentLatLng)
            .title("Lokasi Saya")
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
        private const val DEFAULT_ZOOM = 15f
        private const val MIN_TIME_BETWEEN_UPDATES: Long = 1000
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 1f
    }
}

package com.example.covid_vaccine_map

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.covid_vaccine_map.databinding.ActivityMainBinding
import com.example.covid_vaccine_map.model.CentersModel
import com.example.covid_vaccine_map.model.MapData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MainActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {

    lateinit var binding: ActivityMainBinding
    private var centerData: ArrayList<CentersModel> = arrayListOf()
    private val infoWindow = InfoWindow()
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMap()
        MapData.centersApi?.let {
            centerData.addAll(it.data)
        }
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        binding.myLocation.setOnClickListener(this)

    }

    private fun setMap() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naverMap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMap, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    private fun openCenterInfo(data: CentersModel, marker: Marker) {

        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(this) {
            override fun getContentView(p0: InfoWindow): View {
                val view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.center_info_window, null)

                val address = view.findViewById<TextView>(R.id.address)
                val centerName = view.findViewById<TextView>(R.id.centerName)
                val facilityName = view.findViewById<TextView>(R.id.facilityName)
                val phoneNumber = view.findViewById<TextView>(R.id.phoneNumber)
                val updatedAt = view.findViewById<TextView>(R.id.updatedAt)

                address.text = data.address
                centerName.text = data.centerName
                facilityName.text = data.facilityName
                phoneNumber.text = data.phoneNumber
                updatedAt.text = data.updatedAt

                return view
            }
        }
        infoWindow.open(marker)
    }

    override fun onMapReady(naverMap: NaverMap) {

        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        centerData.forEach {

            Marker().apply {
                position = LatLng(it.lat.toDouble(), it.lng.toDouble())

                when (it.centerType) {
                    "지역" -> {
                        this.icon = MarkerIcons.BLACK
                        this.iconTintColor = Color.BLUE
                        this.zIndex = 0
                    }
                    "중앙/권역" -> {
                        this.icon = MarkerIcons.BLACK
                        this.iconTintColor = Color.RED
                        this.zIndex = 100
                    }
                    else -> {
                        this.icon = MarkerIcons.BLACK
                        this.iconTintColor = Color.GREEN
                        this.zIndex = -10
                    }
                }



                map = naverMap
                setOnClickListener { overlay ->
                    if (overlay is Marker) {
                        if (overlay.infoWindow != null) {
                            overlay.infoWindow?.close()
                        } else {
                            openCenterInfo(it, this)
                            map?.moveCamera(
                                CameraUpdate.scrollTo(
                                    LatLng(
                                        it.lat.toDouble(),
                                        it.lng.toDouble()
                                    )
                                )
                            )
                        }
                    }
                    false
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.myLocation -> {
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
        }
    }


}
package com.example.aplicacionexamen


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GGoogleMaps : AppCompatActivity() {
    private lateinit var mapa: GoogleMap


    val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION

    fun solicitarPermisos(){
        if(!tengoPermisos()){
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
            )
        }
    }

    fun tengoPermisos(): Boolean{
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        return tienePermisos
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggoogle_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar los datos enviados desde la actividad CiudadActivity
        val latitud = intent.getDoubleExtra("LATITUD", 0.0)
        val longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        val nombreCiudad = intent.getStringExtra("NOMBRE_CIUDAD") ?: "Ciudad Desconocida"

        solicitarPermisos()
        inicializarLogicaMapa(latitud, longitud, nombreCiudad)
    }

    fun inicializarLogicaMapa(latitud: Double, longitud: Double, nombreCiudad: String) {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa()
                moverUbicacionCiudad(latitud, longitud, nombreCiudad)  // Pasar los datos a moverUbicacionTienda
                //escucharListeners()
            }
        }
    }

    fun moverUbicacionCiudad(latitud: Double, longitud: Double, nombreCiudad: String) {
        val ubicacionCiudad = LatLng(latitud, longitud)  // Usamos la latitud y longitud que se pasaron
        // Creacion del marcador para la ubicacion de la ciudad
        val titulo = nombreCiudad
        val marcadorCiudad = anadirMarcador(ubicacionCiudad, titulo)
        marcadorCiudad.tag = titulo  // Usar el nombre ingresado de la ciudad

        // Mover la cámara al marcador y hacer zoom
        moverCamaraConZoom(ubicacionCiudad)
    }

    @SuppressLint("MissingPermission")
    fun establecerConfiguracionMapa(){
        with(mapa){
            if(tengoPermisos()){
                mapa.isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }

    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 18f) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    fun anadirMarcador(latLang: LatLng, title: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
    }

}
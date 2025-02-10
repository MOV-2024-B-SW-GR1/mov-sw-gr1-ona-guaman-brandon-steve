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

        // Recuperar los datos enviados desde la actividad anterior
        val latitud = intent.getDoubleExtra("LATITUD", 0.0)
        val longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        val nombreTienda = intent.getStringExtra("NOMBRE_TIENDA") ?: "Tienda Desconocida"

        solicitarPermisos()
        inicializarLogicaMapa(latitud, longitud, nombreTienda)
    }

    fun inicializarLogicaMapa(latitud: Double, longitud: Double, nombreTienda: String) {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa()
                moverUbicacionTienda(latitud, longitud, nombreTienda)  // Pasar los datos a moverUbicacionTienda
                //escucharListeners()
            }
        }
    }

    fun moverUbicacionTienda(latitud: Double, longitud: Double, nombreTienda: String) {
        val ubicacionTienda = LatLng(latitud, longitud)  // Usar la latitud y longitud que se pasaron
        // Crear el marcador para la tienda
        val titulo = nombreTienda
        val marcadorTienda = anadirMarcador(ubicacionTienda, titulo)
        marcadorTienda.tag = titulo  // Usar el nombre de la tienda

        // Mover la cámara al marcador y hacer zoom
        moverCamaraConZoom(ubicacionTienda)
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

    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 17f) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    fun anadirMarcador(latLang: LatLng, title: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
    }

}
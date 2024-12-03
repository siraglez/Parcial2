package com.example.parcial2.farmacia

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R

class MapaFarmaciaActivity : AppCompatActivity() {

    private var nombre: String? = null
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_farmacia)

        supportActionBar?.title = "Mapa farmacias"

        // Recuperar datos de la farmacia
        nombre = intent.getStringExtra("nombre")
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        val imgMapa = findViewById<ImageView>(R.id.imgMapa)
        val imgMarker = findViewById<ImageView>(R.id.imgMarker)

        // Simular la ubicación del marcador
        colocarMarcador(imgMarker, latitud, longitud)
    }

    private fun colocarMarcador(marker: ImageView, lat: Double, lng: Double) {
        // Simulación: convertir latitud y longitud a posición relativa en el mapa
        val posicionX = calcularPosicionX(lng)
        val posicionY = calcularPosicionY(lat)

        marker.x = posicionX
        marker.y = posicionY
        marker.visibility = ImageView.VISIBLE
    }

    private fun calcularPosicionX(longitud: Double): Float {
        // Simulación: ajustar según el rango de longitud en la imagen
        val minLng = -0.9 // Longitud mínima en Zaragoza (simulada)
        val maxLng = 0.2  // Longitud máxima en Zaragoza (simulada)
        val anchoMapa = resources.getDrawable(R.drawable.static_map).intrinsicWidth
        return ((longitud - minLng) / (maxLng - minLng) * anchoMapa).toFloat()
    }

    private fun calcularPosicionY(latitud: Double): Float {
        // Simulación: ajustar según el rango de latitud en la imagen
        val minLat = 41.6 // Latitud mínima en Zaragoza (simulada)
        val maxLat = 41.8 // Latitud máxima en Zaragoza (simulada)
        val altoMapa = resources.getDrawable(R.drawable.static_map).intrinsicHeight
        return ((1 - (latitud - minLat) / (maxLat - minLat)) * altoMapa).toFloat()
    }
}

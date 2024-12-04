package com.example.parcial2.farmacia

import android.os.Bundle
import android.widget.Button
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

        supportActionBar?.title = "Mapa de Farmacia"

        nombre = intent.getStringExtra("nombre")
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        val imgMapa = findViewById<ImageView>(R.id.imgMapa)
        val imgMarker = findViewById<ImageView>(R.id.imgMarker)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        imgMapa.post {
            colocarMarcador(imgMarker, latitud, longitud)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun colocarMarcador(marker: ImageView, lat: Double, lng: Double) {
        val minLat = 41.606 // Latitud mínima en Zaragoza
        val maxLat = 41.700 // Latitud máxima en Zaragoza
        val minLng = -0.92  // Longitud mínima en Zaragoza
        val maxLng = -0.82  // Longitud máxima en Zaragoza

        // Calcular la posición relativa en píxeles
        val xPos = ((lng - minLng) / (maxLng - minLng) * 1155).toFloat()
        val yPos = ((1 - (lat - minLat) / (maxLat - minLat)) * 792).toFloat()

        // Ajustar posición del marcador
        marker.x = xPos - marker.width / 2
        marker.y = yPos - marker.height / 2
        marker.visibility = ImageView.VISIBLE
    }
}

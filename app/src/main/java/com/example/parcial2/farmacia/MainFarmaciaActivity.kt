package com.example.parcial2.farmacia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class MainFarmaciaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var listViewFarmacias: ListView
    private val farmaciasList = mutableListOf<Farmacia>()
    private lateinit var btnCargarFarmacias: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_farmacia)

        supportActionBar?.title = "Farmacias en Zaragoza"
        listViewFarmacias = findViewById(R.id.lvFarmacias)
        btnCargarFarmacias = findViewById(R.id.btnCargarFarmacias)

        val adapter = FarmaciaAdapter(this, farmaciasList)
        listViewFarmacias.adapter = adapter

        // Recuperar farmacias desde Firebase y actualizar la lista
        cargarFarmaciasDesdeFirebase(adapter)

        // Configurar botón para cargar farmacias desde JSON
        btnCargarFarmacias.setOnClickListener {
            loadFarmaciasToFirestore()
            btnCargarFarmacias.visibility = View.GONE // Ocultar botón después de cargar
        }

        // Navegar a MapaFarmaciaActivity al pulsar un elemento
        listViewFarmacias.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val farmacia = farmaciasList[position]
            val intent = Intent(this, MapaFarmaciaActivity::class.java)
            intent.putExtra("nombre", farmacia.nombre)
            intent.putExtra("latitud", farmacia.geometry.coordinates[1])
            intent.putExtra("longitud", farmacia.geometry.coordinates[0])
            startActivity(intent)
        }
    }

    private fun cargarFarmaciasDesdeFirebase(adapter: FarmaciaAdapter) {
        db.collection("farmacias")
            .get()
            .addOnSuccessListener { documents ->
                farmaciasList.clear()
                for (document in documents) {
                    val nombre = document.getString("title") ?: "Sin nombre"
                    val telefono = document.getString("telefono") ?: "Sin teléfono"
                    val latitud = document.getDouble("coordinates.latitude") ?: 0.0
                    val longitud = document.getDouble("coordinates.longitude") ?: 0.0

                    val farmacia = Farmacia(nombre, telefono, Geometry(listOf(longitud, latitud)))
                    farmaciasList.add(farmacia)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar farmacias: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadFarmaciasToFirestore() {
        FirebaseApp.initializeApp(this)
        val firestore = FirebaseFirestore.getInstance()

        val inputStream = resources.openRawResource(R.raw.farmacias_equipamiento)
        val jsonData = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

        try {
            val jsonObject = JSONObject(jsonData)
            val featuresArray = jsonObject.getJSONArray("features")

            for (i in 0 until featuresArray.length()) {
                val feature = featuresArray.getJSONObject(i)
                val properties = feature.getJSONObject("properties")
                val geometry = feature.getJSONObject("geometry")
                val coordinates = geometry.getJSONArray("coordinates")

                val description = properties.getString("description")
                val telefono = extraerTelefono(description) ?: "Sin teléfono"

                val data = mapOf(
                    "title" to properties.getString("title"),
                    "description" to description,
                    "telefono" to telefono, // Cambiar aquí
                    "icon" to properties.getString("icon"),
                    "coordinates" to mapOf(
                        "latitude" to coordinates.getDouble(1),
                        "longitude" to coordinates.getDouble(0)
                    )
                )

                firestore.collection("farmacias")
                    .add(data)
                    .addOnSuccessListener { documentRef ->
                        println("Documento añadido con ID: ${documentRef.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error al añadir documento: $e")
                    }
            }

            Toast.makeText(this, "Farmacias cargadas desde JSON y subidas a Firebase", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun extraerTelefono(description: String): String? {
        val regex = "\\d{9}".toRegex() // Busca un número de 9 dígitos
        return regex.find(description)?.value
    }
}

package com.example.parcial2.farmacia

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainFarmaciaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var listViewFarmacias: ListView
    private val farmaciasList = mutableListOf<Farmacia>()
    private val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_farmacia)

        supportActionBar?.title = "Farmacias en Zaragoza"
        listViewFarmacias = findViewById(R.id.lvFarmacias)

        val adapter = FarmaciaAdapter(this, farmaciasList)
        listViewFarmacias.adapter = adapter

        // Recuperar farmacias desde Firebase
        db.collection("farmacias")
            .get()
            .addOnSuccessListener { documents ->
                farmaciasList.clear()
                for (document in documents) {
                    val nombre = document.getString("nombre") ?: "Sin nombre"
                    val telefono = document.getString("telefono") ?: "Sin teléfono"
                    val latitud = document.getDouble("latitud") ?: 0.0
                    val longitud = document.getDouble("longitud") ?: 0.0

                    val farmacia = Farmacia(nombre, telefono, Geometry(listOf(longitud, latitud)))
                    farmaciasList.add(farmacia)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar farmacias desde Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        // Llamar a la API para obtener las farmacias y subirlas a Firebase
        obtenerFarmaciasDeLaApi()
    }

    private fun obtenerFarmaciasDeLaApi() {
        thread {
            try {
                val url = URL("https://www.zaragoza.es/georref/json/hilo/farmacias_Equipamiento")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                Log.d("API", "Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    Log.d("API Response", response)

                    // Parsear el JSON recibido
                    val jsonArray = JSONArray(response)
                    val farmacias = mutableListOf<Farmacia>()
                    for (i in 0 until jsonArray.length()) {
                        val farmaciaJson = jsonArray.getJSONObject(i)
                        val nombre = farmaciaJson.getString("nombre")
                        val telefono = farmaciaJson.getString("telefono")
                        val latitud = farmaciaJson.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1)
                        val longitud = farmaciaJson.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0)

                        // Crear la farmacia y agregarla a la lista
                        val farmacia = Farmacia(nombre, telefono, Geometry(listOf(longitud, latitud)))
                        farmacias.add(farmacia)
                    }

                    // Subir las farmacias a Firebase
                    firebaseService.subirFarmaciasAFirebase(farmacias)

                    runOnUiThread {
                        Toast.makeText(this, "Farmacias cargadas y subidas a Firebase", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al cargar los datos de la API", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error al conectarse con la API: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("API Error", "Exception: ${e.message}")
                }
            }
        }
    }
}

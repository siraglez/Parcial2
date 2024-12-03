package com.example.parcial2.farmacia

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.firestore.FirebaseFirestore

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

        // Llamar a la API para obtener las farmacias
        obtenerFarmaciasDeLaApi()

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
    }

    private fun obtenerFarmaciasDeLaApi() {
        RetrofitClient.instance.getFarmacias().enqueue(object : Callback<List<Farmacia>> {
            override fun onResponse(call: Call<List<Farmacia>>, response: Response<List<Farmacia>>) {
                if (response.isSuccessful) {
                    val farmacias = response.body() ?: emptyList()
                    // Subir farmacias a Firebase
                    firebaseService.subirFarmaciasAFirebase(farmacias)
                } else {
                    Toast.makeText(this@MainFarmaciaActivity, "Error al cargar datos de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Farmacia>>, t: Throwable) {
                Toast.makeText(this@MainFarmaciaActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

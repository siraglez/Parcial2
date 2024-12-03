package com.example.parcial2.farmacia

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseService {

    private val db = FirebaseFirestore.getInstance()

    // Subir farmacias a Firebase
    fun subirFarmaciasAFirebase(farmacias: List<Farmacia>) {
        for (farmacia in farmacias) {
            val farmaciaMap = mapOf(
                "nombre" to farmacia.nombre,
                "telefono" to farmacia.telefono,
                "latitud" to farmacia.geometry.coordinates[1],
                "longitud" to farmacia.geometry.coordinates[0]
            )

            db.collection("farmacias")
                .add(farmaciaMap)
                .addOnSuccessListener {
                    Log.d("Firebase", "Farmacia añadida: ${farmacia.nombre}")
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error al añadir farmacia", e)
                }
        }
    }
}

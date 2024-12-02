package com.example.parcial2.horario

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class AgregarAsignaturaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_asignatura)

        val etNombreClase = findViewById<EditText>(R.id.etNombreClase)
        val spinnerDias = findViewById<Spinner>(R.id.spinnerDias)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        spinnerDias.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        btnAgregar.setOnClickListener {
            val nombreClase = etNombreClase.text.toString()
            val diaSeleccionado = spinnerDias.selectedItem.toString()
            val horaActual = Timestamp.now()

            if (nombreClase.isNotEmpty()) {
                val dataClase = hashMapOf(
                    "nombre" to nombreClase,
                    "dia" to listOf(diaSeleccionado),
                    "hora" to horaActual
                )

                db.collection("horario")
                    .add(dataClase)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Clase añadida con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al añadir clase: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, ingresa el nombre de la clase", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
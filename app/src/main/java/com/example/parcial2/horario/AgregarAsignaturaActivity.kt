package com.example.parcial2.horario

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore

class AgregarAsignaturaActivity : AppCompatActivity() {
   private lateinit var etNombreClase: EditText
    private lateinit var spinnerDias: Spinner
    private lateinit var etFecha: TextView
    private lateinit var etHora: TextView
    private lateinit var btnAgregar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_asignatura)

        supportActionBar?.title = "Mi horario - Añadir asignatura"

        etNombreClase = findViewById(R.id.etNombreClase)
        spinnerDias = findViewById(R.id.spinnerDias)
        etFecha = findViewById(R.id.etFecha)
        etHora = findViewById(R.id.etHora)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnCancelar = findViewById(R.id.btnCancelar)

        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, dias)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerDias.adapter = adapter

        btnAgregar.setOnClickListener {
            val nombreClase = etNombreClase.text.toString()
            val diaSeleccionado = spinnerDias.selectedItem.toString()
            val fecha = etFecha.text.toString()
            val hora = etHora.text.toString()

            if (nombreClase.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar la clase en Firebase
            val db = FirebaseFirestore.getInstance()
            val dataClase = hashMapOf(
                "nombre" to nombreClase,
                "dia" to diaSeleccionado,
                "fecha" to fecha,
                "hora" to hora
            )

            db.collection("horario")
                .add(dataClase)
                .addOnSuccessListener {
                    Toast.makeText(this, "Clase añadida", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al añadir la clase", Toast.LENGTH_SHORT).show()
                }
        }

        btnCancelar.setOnClickListener { finish() }
    }
}
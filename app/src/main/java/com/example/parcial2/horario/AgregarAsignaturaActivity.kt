package com.example.parcial2.horario

import android.app.*
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AgregarAsignaturaActivity : AppCompatActivity() {
   private lateinit var etNombreClase: EditText
    private lateinit var spinnerDias: Spinner
    private lateinit var tvHoraSeleccionada: TextView
    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var btnAgregar: Button
    private lateinit var btnCancelar: Button

    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_asignatura)

        supportActionBar?.title = "Mi horario - Añadir asignatura"

        etNombreClase = findViewById(R.id.etNombreClase)
        spinnerDias = findViewById(R.id.spinnerDias)
        tvHoraSeleccionada = findViewById(R.id.tvHoraSeleccionada)
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnCancelar = findViewById(R.id.btnCancelar)

        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, dias)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerDias.adapter = adapter

        // Selección de fecha
        tvFechaSeleccionada.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val sdf = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "ES"))
                    fechaSeleccionada = sdf.format(calendar.time)
                    tvFechaSeleccionada.text = fechaSeleccionada
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Selección de hora
        tvHoraSeleccionada.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute)
                    tvHoraSeleccionada.text = "$horaSeleccionada h"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        btnAgregar.setOnClickListener {
            val nombreClase = etNombreClase.text.toString()
            val diaSeleccionado = spinnerDias.selectedItem.toString()

            if (nombreClase.isEmpty() || fechaSeleccionada == null || horaSeleccionada == null) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = FirebaseFirestore.getInstance()
            val dataClase = hashMapOf(
                "nombre" to nombreClase,
                "dia" to diaSeleccionado,
                "fecha" to fechaSeleccionada,
                "hora" to horaSeleccionada
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
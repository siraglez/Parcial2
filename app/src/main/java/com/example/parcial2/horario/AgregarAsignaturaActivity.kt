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
    private var selectedTimestamp: Date? = null

    private lateinit var etNombreClase: EditText
    private lateinit var spinnerDias: Spinner
    private lateinit var tvHoraSeleccionada: TextView
    private lateinit var btnHoraSeleccionada: Button
    private lateinit var btnAgregar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_asignatura)

        supportActionBar?.title = "Mi horario - Añadir asignatura"

        etNombreClase = findViewById(R.id.etNombreClase)
        spinnerDias = findViewById(R.id.spinnerDias)
        tvHoraSeleccionada = findViewById(R.id.tvHoraSeleccionada)
        btnHoraSeleccionada = findViewById(R.id.btnHoraSeleccionada)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnCancelar = findViewById(R.id.btnCancelar)

        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dias)
        spinnerDias.adapter = adapter

        btnHoraSeleccionada.setOnClickListener { showDateTimePicker() }

        btnAgregar.setOnClickListener {
            val nombreClase = etNombreClase.text.toString()
            val diaSeleccionado = spinnerDias.selectedItem.toString()

            if (nombreClase.isEmpty() || selectedTimestamp == null) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Guardar la clase en Firebase
            val db = FirebaseFirestore.getInstance()
            val dataClase = hashMapOf(
                "nombre" to nombreClase,
                "dia" to diaSeleccionado,
                "hora" to selectedTimestamp
            )

            db.collection("horario")
                .add(dataClase)
                .addOnSuccessListener {
                    Toast.makeText(this, "Clase añadida", Toast.LENGTH_SHORT).show()
                }
        }

        btnCancelar.setOnClickListener { finish() }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                selectedTimestamp = calendar.time

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                tvHoraSeleccionada.text = sdf.format(selectedTimestamp)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
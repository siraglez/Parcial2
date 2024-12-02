package com.example.parcial2.horario

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore

class VerHorarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_horario)

        supportActionBar?.title = "Mi horario - Ver horario"

        val spinnerDias = findViewById<Spinner>(R.id.spinnerDiasView)
        val lvClases = findViewById<ListView>(R.id.lvClases)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }

        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, dias)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerDias.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        spinnerDias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val diaSeleccionado = dias[position]
                db.collection("horario")
                    .whereEqualTo("dia", diaSeleccionado)
                    .get()
                    .addOnSuccessListener { documents ->
                        val clases =
                            documents.map { doc -> doc.getString("nombre") ?: "Clase desconocida" }
                        val listAdapter = ArrayAdapter(
                            this@VerHorarioActivity,
                            R.layout.list_item,
                            clases
                        )
                        lvClases.adapter = listAdapter
                    }
                    .addOnFailureListener { exception ->
                        // Manejar errores, ejemplo: mostrar un mensaje
                        lvClases.adapter = ArrayAdapter(
                            this@VerHorarioActivity,
                            R.layout.list_item,
                            listOf("Error al cargar datos")
                        )
                    }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }
    }
}
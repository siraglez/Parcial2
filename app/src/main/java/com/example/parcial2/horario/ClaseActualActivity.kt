package com.example.parcial2.horario

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ClaseActualActivity : AppCompatActivity() {
    private lateinit var tvFechaHora: TextView
    private lateinit var tvClaseActual: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clase_actual)

        supportActionBar?.title = "Mi horario - ¿Qué toca ahora?"

        tvFechaHora = findViewById(R.id.tvFechaHora)
        tvClaseActual = findViewById(R.id.tvClaseActual)

        actualizarFechaHora()
        getClaseActual()

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun actualizarFechaHora() {
        val sdf = SimpleDateFormat("EEEE d 'de' MMMM HH:mm'h'", Locale.getDefault())
        val fechaHoraActual = sdf.format(Date())
        tvFechaHora.text = fechaHoraActual
    }

    private fun getClaseActual() {
        val db = FirebaseFirestore.getInstance()
        val diaActual = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        val horaActual = Date()

        db.collection("horario")
            .whereEqualTo("dia", diaActual)
            .get()
            .addOnSuccessListener { documents ->
                var claseEncontrada = false

                for (document in documents) {
                    val horaClase = document.getTimestamp("hora")?.toDate()
                    if (horaClase != null && horaClase.before(horaActual)) {
                        val nombreClase = document.getString("nombre") ?: "Clase desconocida"
                        tvClaseActual.text = "Estás en clase de $nombreClase"
                        tvClaseActual.setTextColor(ContextCompat.getColor(this, R.color.text_red))
                        claseEncontrada = true
                        break
                    }
                }

                if (!claseEncontrada) {
                    tvClaseActual.text = "No tienes clase ahora mismo"
                    tvClaseActual.setTextColor(ContextCompat.getColor(this, R.color.text_black))
                }
            }
            .addOnFailureListener {
                tvClaseActual.text = "Error al cargar la clase"
                tvClaseActual.setTextColor(ContextCompat.getColor(this, R.color.text_black))
            }
    }
}
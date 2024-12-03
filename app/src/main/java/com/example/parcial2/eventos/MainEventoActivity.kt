package com.example.parcial2.eventos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainEventoActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var listViewEventos: ListView
    private val eventosList = mutableListOf<Map<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_evento)

        supportActionBar?.title = "Eventos"

        listViewEventos = findViewById(R.id.lvEventos)
        val btnAgregarEvento = findViewById<ImageButton>(R.id.btnAgregarEvento)

        val adapter = EventoAdapter(this, eventosList.map {
            Evento(
                it["nombre"] as String,
                it["descripcion"] as String,
                it["direccion"] as String,
                it["fecha"] as String,
                (it["precio"] as Number).toInt(),
                (it["aforo"] as Number).toInt()
            )
        })

        //Recuperar eventos desde Firestore
        db.collection("eventos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error al cargar eventos: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    actualizarListaEventos(snapshot, adapter)
                }
            }

        btnAgregarEvento.setOnClickListener {
            val intent = Intent(this, AgregarEventoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarListaEventos(snapshot: QuerySnapshot, adapter: EventoAdapter) {
        eventosList.clear()
        for (document in snapshot.documents) {
            val nombre = document.getString("nombre") ?: "Sin nombre"
            val descripcion = document.getString("descripcion") ?: "Sin descripción"
            val precio = document.getDouble("precio") ?: 0.0

            val evento = mapOf(
                "nombre" to nombre,
                "descripcion" to descripcion,
                "precio" to precio
            )
            eventosList.add(evento)
        }
        adapter.notifyDataSetChanged()
    }
}

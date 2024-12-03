package com.example.parcial2.eventos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.database.*

class MainEventoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var listViewEventos: ListView
    private val eventosList = mutableListOf<Map<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_evento)

        supportActionBar?.title = "Eventos"

        listViewEventos = findViewById(R.id.lvEventos)
        val btnAgregarEvento = findViewById<ImageButton>(R.id.btnAgregarEvento)

        //Inicializar Firebase
        database = FirebaseDatabase.getInstance().getReference("eventos")

        val adapter = EventoAdapter(this, eventosList)
        listViewEventos.adapter = adapter

        //Recuperar eventos desde Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventosList.clear()
                for (eventoSnapshot in snapshot.children) {
                    val nombre = eventoSnapshot.child("nombre").getValue(String::class.java) ?: "Sin nombre"
                    val descripcion = eventoSnapshot.child("descripcion").getValue(String::class.java) ?: "Sin descripción"
                    val precio = eventoSnapshot.child("precio").getValue(Int::class.java) ?: 0

                    val evento = mapOf(
                        "nombre" to nombre,
                        "descripcion" to descripcion,
                        "precio" to precio
                    )
                    eventosList.add(evento)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //Manejo de errores
            }
        })

        btnAgregarEvento.setOnClickListener {
            val intent = Intent(this, AgregarEventoActivity::class.java)
            startActivity(intent)
        }
    }
}

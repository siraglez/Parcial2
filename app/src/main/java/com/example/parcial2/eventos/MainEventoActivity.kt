package com.example.parcial2.eventos

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Locale

class MainEventoActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var listViewEventos: ListView
    private val eventosList = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_evento)

        supportActionBar?.title = "Eventos"

        listViewEventos = findViewById(R.id.lvEventos)
        val btnAgregarEvento = findViewById<ImageButton>(R.id.btnAgregarEvento)

        val adapter = EventoAdapter(this, eventosList)
        listViewEventos.adapter = adapter

        // Recuperar eventos desde Firestore
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

        // Configurar el botón para cambiar idioma
        val btnChangeLanguage = findViewById<Button>(R.id.btnChangeLanguage)
        btnChangeLanguage.setOnClickListener {
            cambiarIdioma()
        }
    }

    private fun cambiarIdioma() {
        // Cambiar el idioma a español o inglés según la configuración
        val currentLocale = Locale.getDefault().language
        val newLocale = if (currentLocale == "es") {
            Locale("en") // Cambiar a inglés
        } else {
            Locale("es") // Cambiar a español
        }

        // Establecer la nueva configuración regional
        val config = Configuration(resources.configuration)
        config.setLocale(newLocale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Reiniciar la actividad para aplicar el cambio de idioma
        val intent = Intent(this, MainEventoActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun actualizarListaEventos(snapshot: QuerySnapshot, adapter: EventoAdapter) {
        eventosList.clear() // Limpiar la lista antes de actualizarla
        for (document in snapshot.documents) {
            val nombre = document.getString("nombre") ?: "Sin nombre"
            val descripcion = document.getString("descripcion") ?: "Sin descripción"
            val direccion = document.getString("direccion") ?: "Sin dirección"
            val fecha = document.getString("fecha") ?: "Sin fecha"
            val precio = document.getDouble("precio")?.toInt() ?: 0
            val aforo = document.getLong("aforo")?.toInt() ?: 0

            // Crear un objeto Evento y agregarlo a la lista
            val evento = Evento(nombre, descripcion, direccion, fecha, precio, aforo)
            eventosList.add(evento)
        }
        adapter.notifyDataSetChanged()
    }
}

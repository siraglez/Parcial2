package com.example.parcial2.eventos

import android.content.Context
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

        // Aplicar idioma según preferencia guardada
        aplicarIdioma(getSavedLanguage())

        setContentView(R.layout.activity_main_evento)

        supportActionBar?.title = getString(R.string.app_name)

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

    private fun actualizarListaEventos(snapshot: QuerySnapshot, adapter: EventoAdapter) {
        eventosList.clear()
        for (document in snapshot.documents) {
            val nombre = document.getString("nombre") ?: "Sin nombre"
            val descripcion = document.getString("descripcion") ?: "Sin descripción"
            val direccion = document.getString("direccion") ?: "Sin dirección"
            val fecha = document.getString("fecha") ?: "Sin fecha"
            val precio = document.getDouble("precio")?.toInt() ?: 0
            val aforo = document.getLong("aforo")?.toInt() ?: 0

            val evento = Evento(nombre, descripcion, direccion, fecha, precio, aforo)
            eventosList.add(evento)
        }
        adapter.notifyDataSetChanged()
    }

    // Cambiar el idioma y guardar la preferencia
    private fun cambiarIdioma() {
        val newLocale = if (getSavedLanguage() == "es") "en" else "es"
        saveLanguage(newLocale)
        aplicarIdioma(newLocale)
        reiniciarActividad()
    }

    // Aplicar el idioma configurado
    private fun aplicarIdioma(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // Guardar el idioma en SharedPreferences
    private fun saveLanguage(language: String) {
        val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        preferences.edit().putString("language", language).apply()
    }

    // Obtener el idioma guardado
    private fun getSavedLanguage(): String {
        val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return preferences.getString("language", Locale.getDefault().language) ?: "en"
    }

    // Reiniciar la actividad para aplicar cambios de idioma
    private fun reiniciarActividad() {
        val intent = Intent(this, MainEventoActivity::class.java)
        startActivity(intent)
        finish()
    }
}

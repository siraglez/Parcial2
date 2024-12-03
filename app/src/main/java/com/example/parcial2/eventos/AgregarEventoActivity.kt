package com.example.parcial2.eventos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AgregarEventoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_evento)

        supportActionBar?.title = "Registro de Eventos"

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etAforo = findViewById<EditText>(R.id.etAforo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCerrar = findViewById<Button>(R.id.btnCerrar)

        //Inicializar Firebase
        database = FirebaseDatabase.getInstance().getReference("eventos")

        btnCerrar.setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val descripcion = etDescripcion.text.toString()
            val direccion = etDireccion.text.toString()
            val precio = etPrecio.text.toString().toIntOrNull() ?: 0
            val fecha = etFecha.text.toString()
            val aforo = etAforo.text.toString().toIntOrNull() ?: 0

            if (nombre.isNotEmpty() && descripcion.isNotEmpty()) {
                val eventoId = database.push().key ?: ""
                val evento = Evento(nombre, descripcion, direccion, fecha, precio, aforo)

                database.child(eventoId).setValue(evento).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Evento agregado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al agregar evento", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

data class Evento(
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val fecha: String,
    val precio: Int,
    val aforo: Int
)

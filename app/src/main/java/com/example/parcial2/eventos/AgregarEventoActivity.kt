package com.example.parcial2.eventos

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AgregarEventoActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etFecha: EditText
    private lateinit var etAforo: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCerrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_evento)

        supportActionBar?.title = "Registro de Eventos"

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etDireccion = findViewById(R.id.etDireccion)
        etPrecio = findViewById(R.id.etPrecio)
        etFecha = findViewById(R.id.etFecha)
        etAforo = findViewById(R.id.etAforo)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCerrar = findViewById(R.id.btnCerrar)

        // Botón de guardar evento
        btnGuardar.setOnClickListener {
            guardarEvento()
        }

        // Botón de cerrar
        btnCerrar.setOnClickListener {
            finish()
        }

        // Configurar campo de fecha para mostrar calendario
        etFecha.inputType = InputType.TYPE_NULL
        etFecha.isFocusable = false
        etFecha.setOnClickListener {
            mostrarCalendario()
        }
    }

    // Mostrar el calendario para seleccionar una fecha
    fun mostrarCalendario() {
        val dialog = AlertDialog.Builder(this)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL

        val pickerDia = NumberPicker(this)
        pickerDia.minValue = 1
        pickerDia.maxValue = 31
        pickerDia.value = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val pickerMes = NumberPicker(this)
        pickerMes.minValue = 1
        pickerMes.maxValue = 12
        pickerMes.value = Calendar.getInstance().get(Calendar.MONTH) + 1

        val pickerAnio = NumberPicker(this)
        val anioActual = Calendar.getInstance().get(Calendar.YEAR)
        pickerAnio.minValue = anioActual - 100
        pickerAnio.maxValue = anioActual + 100
        pickerAnio.value = anioActual

        layout.addView(pickerDia)
        layout.addView(pickerMes)
        layout.addView(pickerAnio)

        dialog.setTitle("Selecciona una fecha")
        dialog.setView(layout)

        dialog.setPositiveButton("Aceptar") { _, _ ->
            val dia = pickerDia.value
            val mes = pickerMes.value
            val anio = pickerAnio.value
            val fechaSeleccionada = "$dia/$mes/$anio"
            etFecha.setText(fechaSeleccionada)
        }

        dialog.setNegativeButton("Cancelar", null)
        dialog.show()
    }

    // Guardar evento en Firebase
    private fun guardarEvento() {
        // Recuperar valores de los campos
        val nombre = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val fecha = etFecha.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()
        val aforoStr = etAforo.text.toString().trim()

        // Validar campos
        if (nombre.isEmpty() || descripcion.isEmpty() || direccion.isEmpty() || fecha.isEmpty() ||
            precioStr.isEmpty() || aforoStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir a tipos correctos
        val precio = precioStr.toIntOrNull()
        val aforo = aforoStr.toIntOrNull()

        if (precio == null || aforo == null) {
            Toast.makeText(this, "Precio y Aforo deben ser números válidos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un mapa de evento
        val eventoNuevo = mapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "direccion" to direccion,
            "fecha" to fecha,
            "precio" to precio,
            "aforo" to aforo
        )

        // Intentar guardar en Firebase
        try {
            db.collection("eventos")
                .add(eventoNuevo)
                .addOnSuccessListener {
                    Toast.makeText(this, "Evento guardado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar el evento: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            // Manejar cualquier excepción
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

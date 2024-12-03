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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Calendar

class AgregarEventoActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore

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

        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etDireccion = findViewById(R.id.etDireccion)
        etPrecio = findViewById(R.id.etPrecio)
        etFecha = findViewById(R.id.etFecha)
        etAforo = findViewById(R.id.etAforo)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCerrar = findViewById(R.id.btnCerrar)

        btnGuardar.setOnClickListener {
            guardarEvento()
        }

        btnCerrar.setOnClickListener {
            finish()
        }

        etFecha.inputType = InputType.TYPE_NULL
        etFecha.isFocusable = false

        etFecha.setOnClickListener {
            mostrarCalendario()
        }
    }

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
        pickerAnio.minValue = 1000
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

        //Personalizar los botones
        val alertDialog = dialog.create()
        alertDialog.setOnShowListener {
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            //Cambiar color del texto de los botones
            positiveButton.setTextColor(resources.getColor(R.color.text_black))
            negativeButton.setTextColor(resources.getColor(R.color.text_black))
        }

        dialog.show()
    }

    private fun guardarEvento(){
        val nombre = etNombre.text.toString()
        val descripcion = etDescripcion.text.toString()
        val direccion = etDireccion.text.toString()
        val fecha = etFecha.text.toString()
        val precio = etPrecio.text.toString().toInt()
        val aforo = etAforo.text.toString().toInt()
        val eventoNuevo = Evento(nombre, descripcion, direccion, fecha, precio, aforo)

        db.collection("eventos")
            .add(eventoNuevo)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Evento guardado: ${eventoNuevo.nombre}", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar el evento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

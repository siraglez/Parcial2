package com.example.parcial2.horario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R

class MainHorarioActivity : AppCompatActivity() {
    private lateinit var btnAgregarClase: Button
    private lateinit var btnVerHorario: Button
    private lateinit var btnClaseActual: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_horario)

        //Configurar el botón para agregar una clase
        btnAgregarClase.setOnClickListener {
            startActivity(Intent(this, AgregarAsignaturaActivity::class.java))
        }

        //Configurar el botón para ver el horario
        btnVerHorario.setOnClickListener {
            startActivity(Intent(this, VerHorarioActivity::class.java))
        }

        //configurar el botón para ver la clase actual
        btnClaseActual.setOnClickListener {
            startActivity(Intent(this, ClaseActualActivity::class.java))
        }
    }
}
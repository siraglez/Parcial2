package com.example.parcial2.horario

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.MainActivity
import com.example.parcial2.R

class MainHorarioActivity : AppCompatActivity() {
    private lateinit var btnAgregarClase: Button
    private lateinit var btnVerHorario: Button
    private lateinit var btnClaseActual: Button
    private lateinit var btnVolver: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_horario)

        supportActionBar?.title = "Mi horario"

        btnAgregarClase = findViewById(R.id.btnAgregarClase)
        btnVerHorario = findViewById(R.id.btnVerHorario)
        btnClaseActual = findViewById(R.id.btnClaseActual)

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

        btnVolver = findViewById(R.id.btnVolver)

        //Configurar el botón para volver a la pantalla de inicio
        btnVolver.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
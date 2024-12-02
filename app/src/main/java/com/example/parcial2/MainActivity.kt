package com.example.parcial2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.farmacia.FarmaciaInicio
import com.example.parcial2.horario.HorarioInicio
import com.example.parcial2.listadoEventos.ListadoInicio

class MainActivity : AppCompatActivity() {
    private lateinit var btnHorario: Button
    private lateinit var btnListado: Button
    private lateinit var btnFarmacia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configurar el botón para la aplicación del horario
        btnHorario.setOnClickListener {
            startActivity(Intent(this, HorarioInicio::class.java))
        }

        //Configurar el botón para la aplicación del listado de eventos
        btnListado.setOnClickListener {
            startActivity(Intent(this, ListadoInicio::class.java))
        }

        //Configurar el botón para la aplicación de las farmacias de Zaragoza
        btnFarmacia.setOnClickListener {
            startActivity(Intent(this, FarmaciaInicio::class.java))
        }
    }
}

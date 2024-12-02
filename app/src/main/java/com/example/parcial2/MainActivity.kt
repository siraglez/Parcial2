package com.example.parcial2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.farmacia.MainFarmaciaActivity
import com.example.parcial2.horario.MainHorarioActivity
import com.example.parcial2.eventos.MainEventoActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnHorario: Button
    private lateinit var btnListado: Button
    private lateinit var btnFarmacia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configurar el botón para la aplicación del horario
        btnHorario.setOnClickListener {
            startActivity(Intent(this, MainHorarioActivity::class.java))
        }

        //Configurar el botón para la aplicación del listado de eventos
        btnListado.setOnClickListener {
            startActivity(Intent(this, MainEventoActivity::class.java))
        }

        //Configurar el botón para la aplicación de las farmacias de Zaragoza
        btnFarmacia.setOnClickListener {
            startActivity(Intent(this, MainFarmaciaActivity::class.java))
        }
    }
}

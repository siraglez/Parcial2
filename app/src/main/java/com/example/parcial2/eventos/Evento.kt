package com.example.parcial2.eventos

data class Evento(
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val fecha: String,
    val precio: Int,
    val aforo: Int
)
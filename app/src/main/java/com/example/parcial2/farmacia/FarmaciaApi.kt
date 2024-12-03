package com.example.parcial2.farmacia

import retrofit2.Call
import retrofit2.http.GET

interface FarmaciaApi {
    @GET("georref/json/hilo/farmacias_Equipamiento")
    fun getFarmacias(): Call<List<Farmacia>>
}

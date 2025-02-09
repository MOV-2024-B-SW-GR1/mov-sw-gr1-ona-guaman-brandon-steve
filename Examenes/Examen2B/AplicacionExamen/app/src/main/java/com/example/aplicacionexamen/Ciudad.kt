package com.example.aplicacionexamen

data class Ciudad(
    val id: Int,
    val nombreCiudad: String,
    val poblacion: Double,
    val esCapital: String,
    val tieneAereopuerto: String,
    val paisId: Int,
    val latitud: Double,  // Nuevo campo
    val longitud: Double  // Nuevo campo
)
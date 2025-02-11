package com.example.proyectoIIB

import java.util.Date

data class Tarea(
    val id: Int,
    val nombreTarea: String,
    val descricionT: String,
    val fechaVencimiento: Date,
    val prioridad: String,   //Baja Media Alta
    val estado: String,     //Pendiente, en Progeso, Completada
    val categoriaId: Int
)
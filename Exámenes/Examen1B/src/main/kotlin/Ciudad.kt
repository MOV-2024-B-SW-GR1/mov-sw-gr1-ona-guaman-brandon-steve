package org.example

import java.util.Date

data class Ciudad(
    var id : Int,
    var nombre: String,
    var poblacion: Double,
    var tieneAeropuerto: Boolean,
    var fechaFundacionC: Date,
    var esCapital: Boolean,
    var idPais: Int
) {
}
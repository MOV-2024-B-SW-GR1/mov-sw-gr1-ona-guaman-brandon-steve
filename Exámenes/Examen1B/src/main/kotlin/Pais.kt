package org.example

import java.util.*

data class Pais (
    var id : Int,
    var nombre: String,
    var codigo: String,
    var fechaFundacion: Date,
    var areaTotal: Double,
    var idiomaOficial: String,
    var ciudades : MutableList<Ciudad> = mutableListOf()
) {
    override fun toString(): String {
        return "\n" +
                "\t" + nombre.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + "\n" +
                "Código: " + codigo + "\n" +
                "Fecha de Fundación: " + Fecha.formatoFecha(fechaFundacion)  + "\n" +
                "Area Total: " + areaTotal + "\n" +
                "Idioma oficial: " + idiomaOficial + "\n" +
                "Ciudades:\n" + "{" + ciudades.joinToString() { it.toString() + "\n}" } + "\n"
    }

}
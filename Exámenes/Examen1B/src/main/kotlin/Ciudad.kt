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
    override fun toString(): String {
        val paisNombre = obtenerNombrePais(idPais)
        return "\n"+"""
            |Ciudad Número: $id
            |Nombre Cuidad: $nombre
            |Poblacion (millones de personas): $poblacion
            |Aeropuerto: $tieneAeropuerto
            |Fecha de Fundación: $fechaFundacionC
            |Es Capital: $esCapital
            |País: $paisNombre
        """.trimMargin()+"\n"
    }
    //Funcion para obtener el nombre del pais mediante la intancia de PaisMetodos
    private fun obtenerNombrePais(PaisId:Int): String{
        val paisCrud =PaisMetodos("pais.csv", CiudadMetodos("ciudad.csv"))
        val pais = paisCrud.readPais().find {it.id.toInt()==PaisId}
        return pais?.nombre?: "Desconocido"
    }
}
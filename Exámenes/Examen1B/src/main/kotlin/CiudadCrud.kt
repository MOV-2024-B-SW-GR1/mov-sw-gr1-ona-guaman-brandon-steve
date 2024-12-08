package org.example

import java.io.File
import java.text.SimpleDateFormat

class CiudadCrud (
    val csvFile : String
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun createC(ciudad:Ciudad){
        val file = File(csvFile)
        if (!file.exists()) {
            file.createNewFile()
        }
        val newId = generateNewId()
        val newLine ="${newId},${ciudad.nombre},${ciudad.poblacion},${ciudad.tieneAeropuerto},${dateFormat.format(ciudad.fechaFundacionC)},${ciudad.esCapital},${ciudad.idPais}\n"
        file.appendText(newLine)
    }
    fun readCiudades(): List<Ciudad>{
        val file = File(csvFile)
        if(!file.exists()){
            return emptyList()
        }
        val ciudadesLeidas = mutableListOf<Ciudad>()
        file.forEachLine { line ->
            val tokens= line.split(",")
            if (tokens.size==7){
                val id = tokens[0].toInt()
                val nombre = tokens[1]
                val poblacion = tokens[2].toDouble()
                val tieneAeropurto = tokens[3].toBoolean()
                val fechaFundacionCStr = tokens[4]
                val fechaFundacionC = dateFormat.parse(fechaFundacionCStr)
                val esCapital = tokens[5].toBoolean()
                val idPais = tokens[6].toInt()
                val ciudad = Ciudad(id, nombre, poblacion, tieneAeropurto, fechaFundacionC,
                    esCapital,idPais)
                ciudadesLeidas.add((ciudad))
            }
        }
        return ciudadesLeidas
    }
    fun updateCiudad(ciudad: Ciudad){
        val ciudades = readCiudades().toMutableList()
        val index = ciudades.indexOfFirst { it.id == ciudad.id }
        if (index !=-1){
            ciudades[index] = ciudad
            saveAllCiudad(ciudades)
        }
    }
    fun deleteCiudad(id: Int){
        val ciudades =readCiudades().filter { it.id != id }
        saveAllCiudad(ciudades)
    }
    private fun saveAllCiudad(ciudades: List<Ciudad>){
        val file = File(csvFile)
        file.writeText(ciudades.joinToString("\n"){ciudad ->
            "${ciudad.id},${ciudad.nombre},${ciudad.poblacion},${ciudad.tieneAeropuerto},${dateFormat.format(ciudad.fechaFundacionC)}," +
                    "${ciudad.esCapital},${ciudad.idPais}"
        }+"\n")
    }
    private fun generateNewId():Int{
        val ciudades = readCiudades()
        return if (ciudades.isEmpty()){
            1
        }else{
            ciudades.maxOf { it.id } +1
        }
    }
}
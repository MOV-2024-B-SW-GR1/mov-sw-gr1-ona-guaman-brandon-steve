package org.example

import java.io.File
import java.text.SimpleDateFormat

class PaisCrud (
    private val paisCsvFile: String,
    private val ciudadCrud: CiudadCrud
) {
    private val dateFormatInput = SimpleDateFormat("yyyy-MM-dd")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun crearPais(pais:Pais){
        val file =File(paisCsvFile)
        if (!file.exists()){
            file.createNewFile()
        }
        val newId=generateNewId()
        val newLine ="${newId},${pais.nombre},${pais.codigo},${dateFormatInput.format(pais.fechaFundacion)},${pais.areaTotal},${pais.idiomaOficial}\n"
        file.appendText(newLine)
    }
    fun readPais(): List<Pais>{
        val file =File(paisCsvFile)
        if(!file.exists()){
            return emptyList()
        }
        val paises = mutableListOf<Pais>()
        file.bufferedReader().useLines { lines ->
            lines.forEach { line->
                val tokens = line.split(",")
                if (tokens.size >= 6) {
                    try {
                        val id = tokens[0].toInt()
                        val nombre = tokens[1]
                        val codigo = tokens[2]
                        val fechaFundacionStr = tokens[3]
                        val fechaFundacion = dateFormatInput.parse(fechaFundacionStr)
                        val areaTotal = tokens[4].toDouble()
                        val idiomaOficial = tokens[5]
                        val ciudades = ciudadCrud.readCiudades().filter { it.idPais == id }.toMutableList()
                        val pais = Pais(id, nombre, codigo, fechaFundacion, areaTotal, idiomaOficial, ciudades)
                        paises.add(pais)
                    } catch (e: Exception) {
                        println("Error al procesar la linea: $line. Saltando esta linea")
                    }
                }
            }
        }
        return paises
    }
    fun updatePais(pais:Pais){
        val paises= readPais().toMutableList()
        val index =paises.indexOfFirst { it.id == pais.id }
        if (index!=-1){
            paises[index]=pais
            saveAllPaises(paises)
        }
    }
    fun deletePais(id: Int){
        val paises = readPais().filter { it.id != id }
        saveAllPaises(paises)
    }

    private fun saveAllPaises(paises: List<Pais>){
        val file = File(paisCsvFile)
        file.writeText(paises.joinToString("\n") { pais ->
            "${pais.id},${pais.codigo},${dateFormatInput.format(pais.fechaFundacion)},${pais.areaTotal}, ${pais.idiomaOficial}"
        })
    }
    private fun generateNewId():Int{
        val cuidades = readPais()
        return if(cuidades.isEmpty()){
            1
        }else{
            cuidades.maxOf { it.id } +1
        }

    }

}
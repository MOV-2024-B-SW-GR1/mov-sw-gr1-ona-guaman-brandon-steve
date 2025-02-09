package org.example

import java.io.File
import java.text.SimpleDateFormat

class PaisMetodos (
    private val paisCsvFile: String,
    private val ciudadCrud: CiudadMetodos
) {
    private val dateFormatInput = SimpleDateFormat("yyyy-MM-dd")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    //Funcion Crear Paises
    fun crearPais(pais:Pais){
        val file =File(paisCsvFile)
        if (!file.exists()){
            file.createNewFile()
        }
        val newId=generateNewId()
        val newLine ="${newId},${pais.nombre},${pais.codigo},${dateFormatInput.format(pais.fechaFundacion)},${pais.areaTotal},${pais.idiomaOficial} \n"
        file.appendText(newLine)
    }
    //Funcion Leer Pais
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
                        println("Error en la linea: $line. Continuando porcesando las demás lineas")
                    }
                }
            }
        }
        return paises
    }
    //Funcion Actulizar Pais
    fun updatePais(pais:Pais){
        val paises= readPais().toMutableList()
        val index =paises.indexOfFirst { it.id == pais.id }
        if (index!=-1){
            paises[index]=pais
            saveAllPaises(paises)
        }
    }
    //Funcion Eliminar Pais
    fun deletePais(id: Int){
        val paises = readPais().filter { it.id != id }
        saveAllPaises(paises)
    }
    //Funcion para guardar los paises ingresados
    private fun saveAllPaises(paises: List<Pais>){
        val file = File(paisCsvFile)
        file.writeText(paises.joinToString("\n") { pais ->
            "${pais.id},${pais.nombre},${pais.codigo},${dateFormatInput.format(pais.fechaFundacion)},${pais.areaTotal}, ${pais.idiomaOficial}\n"
        })
    }
    //Generar un id automaticamente
    private fun generateNewId():Int{
        val paises = readPais()
        return if(paises.isEmpty()){
            1
        }else{
            paises.maxOf { it.id } +1
        }

    }

}
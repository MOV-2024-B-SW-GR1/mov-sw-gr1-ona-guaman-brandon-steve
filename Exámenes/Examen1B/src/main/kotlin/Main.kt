package org.example

import java.text.SimpleDateFormat
import java.util.*

fun main() {
    //println("Hello World!asdfffasd")
    val ciudadCrud = CiudadCrud("ciudad.csv")
    val paisCrud = PaisCrud("pais.csv", ciudadCrud)
    val scanner = Scanner(System.`in`)

    //    MENU
    var opcion: Int = 0;
    while(opcion != 9){
        mostrarMenu();
//        Leer opcion
        opcion = readLine()!!.toInt();
        when(opcion){
            1 -> {
//                Mostrar ciudades
                println(ciudadCrud.readCiudades())

            }
            2 -> {
//                Mostrar paises con ciudades
                println(paisCrud.readPais())
            }
            3 -> {
//                Mostrar lista de Paises
                val listaPaises = paisCrud.readPais()
                println("Paises Disponibles:")
                listaPaises.forEach { println("${ it.id }:${it.nombre}") }
            }
            4 -> {
//                Ingresar nueva ciudad
                println("Ingrese la nombre de la ciudad:")
                val nombre = scanner.nextLine()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                println("Ingrese la poblacion (Millones de personas):")
                val poblacion = scanner.nextDouble()
                scanner.nextLine() // Consumir el salto de línea residual después de leer el double

                println("La ciudad tiene Aeropuerto? (si-no)")
                val tieneAeropuertoStr = scanner.nextLine()
                val tieneAeropuerto = if (tieneAeropuertoStr == "si") true else false
                println("Ingrese la fecha de fundación (yyyy-MM-dd):")
                val fechaFundacionCStr = scanner.nextLine()
                val fechaFundacionC = SimpleDateFormat("yyyy-MM-dd").parse(fechaFundacionCStr)
                println("La ciudad es la capital? (si-no)")
                val esCapitalStr = scanner.nextLine()
                val esCapital = if (esCapitalStr == "si") true else false



                val listaPais = paisCrud.readPais()
                println("Paises:")
                listaPais.forEach { println("${ it.id }:${it.nombre}") }
                println("Selecciona el Pais: ")
                val pais = scanner.nextInt()
                scanner.nextLine()

                val nuevaCiudad = Ciudad(
                    id = 0,
                    nombre = nombre,
                    poblacion = poblacion,
                    tieneAeropuerto = tieneAeropuerto,
                    fechaFundacionC = fechaFundacionC,
                    esCapital = esCapital,
                    idPais = pais
                )
                ciudadCrud.createC(nuevaCiudad)
                println("!!Ciudad Ingresada correctamente!!")

                val paises = listaPais.find { it.id == pais }
                if (paises != null) {
                    paises.ciudades.add(nuevaCiudad)
                    paisCrud.updatePais(paises)
                }
            }
            5 -> {
//                Modificar ciudad
                println(ciudadCrud.readCiudades())
                println("Ingrese el N. de la cuidad para actualizarla:")
                val ciudadId = scanner.nextInt()
                scanner.nextLine() // Consumir la nueva línea

                val ciudad = ciudadCrud.readCiudades().find { it.id == ciudadId }
                if (ciudad != null) {
                    println("Ingrese la nueva poblacion de la ciudad (Presione Enter para no realizar cambios):")
                    val poblacionStr = scanner.nextLine()
                    val nuevaPoblacion = if (poblacionStr.isNotBlank()) poblacionStr.toDouble() else ciudad.poblacion

                    println("¿La ciudad tiene Aeropuerto? (si/no, o presione Enter para no realizar cambios):")
                    val tieneAeropuertoStr = scanner.nextLine()
                    val nuevoTieneAeropuerto = if (tieneAeropuertoStr.isNotBlank()) tieneAeropuertoStr.equals("si", ignoreCase = true) else ciudad.tieneAeropuerto

                    println("¿La ciudad es capital? (si/no, o presione Enter para no realizar cambios):")
                    val esCapitalStr = scanner.nextLine()
                    val nuevoEsCapital = if (esCapitalStr.isNotBlank()) esCapitalStr.equals("si", ignoreCase = true) else ciudad.esCapital

                    val ciudadActualizada = ciudad.copy(
                        poblacion = nuevaPoblacion,
                        tieneAeropuerto = nuevoTieneAeropuerto,
                        esCapital = nuevoEsCapital
                    )

                    ciudadCrud.updateCiudad(ciudadActualizada)
                    println("Ciudad actualizada correctamente.")
                } else {
                    println("No se encontró una ciudad con ID: $ciudadId")
                }

            }
            6 -> {
//                Eliminar ciudad
                println(ciudadCrud.readCiudades())
                println("Ingrese el N. de la ciudad que desea eliminar:")
                val usuarioId = scanner.nextInt()
                ciudadCrud.deleteCiudad(usuarioId)
                println("!!Ciudad eliminada correctamente!!")
            }
            7 -> {
//                Ingresar nuevo Pais
                println("Ingrese el nombre del Pais:")
                val nombre = scanner.nextLine().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                println("Ingrese el código del Pais:")
                val codigo = scanner.nextLine().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                println("Ingrese la fecha de fundación del país (yyyy-MM-dd):")
                val fechaFundacionStr = scanner.nextLine()
                val fechaFundacion = SimpleDateFormat("yyyy-MM-dd").parse(fechaFundacionStr)
                println("Ingrese el área total (millones de km^2):")
                val areaTotal = scanner.nextDouble()
                scanner.nextLine() // Consumir el salto de línea residual
                println("Ingrese el idioma oficial del Pais:")
                val idiomaOficial = scanner.nextLine().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                val nuevoPais = Pais(
                    id = 0,
                    nombre = nombre,
                    codigo = codigo,
                    fechaFundacion = fechaFundacion,
                    areaTotal = areaTotal,
                    idiomaOficial = idiomaOficial
                )
                paisCrud.crearPais(nuevoPais)
                println("Pais creado correctamente!")
            }
            8 -> {
//                Eliminar Pais
                println(paisCrud.readPais())
                println("Ingrese el N. del Pais  que desea eliminar:")
                val paisId = scanner.nextInt()
                paisCrud.deletePais(paisId)
                println("Pais eliminado correctamente")
            }
            9 -> {
//                Salir
                println("Gracias por su participación")
                break;
            }
            else -> continue
        }
    }

}

fun mostrarMenu(){
    println("\n\t"+ "******* Paises y Ciudades *******\n" +
            "1. Mostrar ciudades\n"+
            "2. Mostrar paises y ciudades\n"+
            "3. Mostrar Lista de los paises\n"+
            "4. Ingresar nueva ciudad\n"+
            "5. Modificar ciudad\n"+
            "6. Eliminar ciudad\n"+
            "7. Ingresar nuevo pais\n"+
            "8. Eliminar pais\n"+
            "9. Salir\n"+
            "Ingresa tu opcion: ")
}
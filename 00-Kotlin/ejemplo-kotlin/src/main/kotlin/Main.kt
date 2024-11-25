package org.example

import java.util.*

fun main() {
    println("Hello World!")

    //INMUTABLES (No se re asigna "=")
    val inmutable: String = "Brandon";
    //inmutable = "Steve" //ERROR!

    //MUTABLES
    var mutable: String = "Steve"
    mutable = "Steve1" //OK

    //VAL>VAR

    //Duck Typing
    val ejemploVariable = "Brandon Steve Oña Guaman"
    ejemploVariable.trim()
    val edadEjemplo: Int = 12
    //ejemploVariable= edadEjemplo //Error
    //Variables primitivas
    val nombreProfesor: String = "BrandonSteve onaguamansssssss"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    //Clases en Java
    val fechaNacimiento: Date = Date()

    //When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C")->{
            println("Casado")
        }
        "S" ->{
            println("Soltero")
        }
        else ->{
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if(esSoltero) "Si" else "No"

    //Llamar la funcion
    imprimirNombre("Brandon Stve Oña Guaman")

    calcularSueldo(10.00) //solo parametro requerido
    calcularSueldo(10.00,15.00, 20.00) //parametro requerido y sobreescribor parametros opcionales
    //Named parameters
    //calcularSueldo(sueldo, tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00) // usando el parametro bonoEspecial en 2da posicion
    //gracias a los parametros nombrados
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)
    //usando el parametro bonoEspecial en 1mera posicion
    //usando el parametro sueldo en 2da posicion
    //usando el parametro tasa en 3era posicion
    //gracias a los parametros nombrados
}

fun imprimirNombre(nombre:String):Unit{ //Unit es opcional, es similar al void

    fun otraFuncionAdentro(){
        print("Otra funcion Adentro")
    }
    println("Nombre: $nombre") //Uso sin llaves
    println("Nombre: ${nombre}") //Uso con llaves opcional
    println("Nombre: ${nombre+nombre}") //Uso con llaves concatenado
    println("Nombre: ${nombre.uppercase()}") //Uso con llaves (funcion)
    println("Nombre: $nombre.uppercase()") //INCORRECTO, NO SE PUEDE USAR SIN LLAVES

    otraFuncionAdentro()
}

fun calcularSueldo(
    sueldo:Double, //Requerido
    tasa: Double = 12.00, //Opcional (defecto)
    bonoEspecial: Double?= null //Opcional (nullable)
    //variable? -> "?" Es Nullable (Osea que puede en algun momento ser nulo)
): Double{
    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)
    if(bonoEspecial == null){
        return sueldo * (100/tasa)
    } else{
        return sueldo * (100/tasa) * bonoEspecial
    }
}
//Clases con sintaxis Kotlin y Java

//JAVA
abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

//KOTLIN
abstract class Numeros( //Constructor Primario
    //caso 1)  Parametro normal
    //uno:Int, (parametro sin modificar acceso)

    //Caso 2 parametro y propiedad (atributo) (protected)
    //private var uno: int (propiedad "instancia.uno")
    protected val numeroUno: Int, //instancia.numeroUno
    protected val numeroDos: Int, //instancia.numeroDos
    parametroNoUsadoNoPropiedadDeLaClase:Int?=null
){
    init { //Bloque constructor primario OPCIONAL
        this.numeroUno
        this.numeroDos
        println("Inicializando...")

    }
}

class Suma(
    unoParametro:Int, //Parametro
    dosParametro: Int, //Parametros
): Numeros( //Clase papa, numeros (extendiendo)
    unoParametro,
    dosParametro
){

}


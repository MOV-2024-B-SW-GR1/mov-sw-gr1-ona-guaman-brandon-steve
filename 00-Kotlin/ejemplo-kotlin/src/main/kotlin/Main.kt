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
    val nombreProfesor: String = "BrandonSteve ONAGUAMAN"
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

    //CLASES USO:
    val SumaA = Suma(1,1)
    val SumaB = Suma(null,1)
    val SumaC = Suma(1,null)
    val SumaD = Suma(null,null)

    SumaA.sumar()
    SumaB.sumar()
    SumaC.sumar()
    SumaD.sumar()

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    //ARREGLOS
    //Estarticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatico);
    //Dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    // FOR EACH => Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int -> // -> =>
            println("Valor actual: ${valorActual}");
        }

    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor Actual (it): ${it}")}

    // MAP→ MUTA (Modifica cambia) el arreglo
    // 1) Enviamos el nuevo valor de la iteracion
    // 2) Nos devuelve un NUEVO ARREGLO con valores
    // de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble()+ 100.00
        }
    println(respuestaMap)
    val respuestaMapDos=arregloDinamico.map {it + 15 }
    println(respuestaMapDos)

    //FILTER -> Filtrar el arreglo
    //1) Devolver una expresion TRUE or FALSE
    //2) Nuevo arreglo filtrado

    val respuestaFilter:List<Int> = arregloDinamico
        .filter { valorActual:Int->
            //Expresion o Cndicion
            val mayoresACinco: Boolean=valorActual>5
            return@filter mayoresACinco
        }
    val respuestaFilterDos = arregloDinamico.filter{it <6}
    println(respuestaFilter)
    println(respuestaFilterDos)

    //OR AND
    //OR -> ANY (Alguno Cumple?)
    //AND -> ALL (Todos Cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any { valorActual: Int->
            return@any (valorActual>5)
        }
    println(respuestaAny) //True
    val respuestaAll:Boolean = arregloDinamico
        .all { valorActual:Int ->
            return@all (valorActual>5)
        }
    println(respuestaAll)

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
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicite:String = "Publico Implicito"
    init { //bloque constructor primario
        this.numeroUno
        this.numeroDos
        numeroUno   // this. OPCIONAL [propiedades, metodos]
        numeroDos // this. OPCIONAL [propiedades, metodos]
        this.soyPublicoImplicite
        soyPublicoExplicito
    }

    constructor( //Constructor secundario
        uno:Int?, //Entero nullable
        dos: Int,
    ):this(
        if (uno==null) 0 else uno,dos
    ){
        //bloque de codigo del constructor secundario
    }
    constructor( //Constructor secundario
        uno: Int,
        dos: Int?, //Entero nullable
    ):this(
        uno,
        if (dos==null) 0 else dos
    ){
        //bloque de codigo del constructor secundario
    }

    constructor( //Constructor secundario
        uno: Int?, //Entero nullable
        dos: Int?,
    ):this(
        if (uno==null) 0 else uno,
        if (dos==null) 0 else dos,
    ){
        //bloque de codigo del constructor secundario
    }

    fun sumar (): Int {
        val total = numeroUno + numeroDos
        agregarHisotorial(total)
        return total
    }

    companion object{ //comparte entre todas las instancias, similar al Static
        //funciones, variables
        //NombreClase.metodo,NombreClase.funcion =>
        //Suma.pi
        val pi = 3.14
        //Suma.elevarAlCuadrado
        fun elevarAlCuadrado(num:Int):Int{return num*num}
        val historialSumas = arrayListOf<Int>()

        fun agregarHisotorial(valorTotalSuma:Int){
            historialSumas.add(valorTotalSuma)
        }

    }

}


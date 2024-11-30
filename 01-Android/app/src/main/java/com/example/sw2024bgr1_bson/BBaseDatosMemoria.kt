package com.example.sw2024bgr1_bson

class BBaseDatosMemoria {
    companion object{
        val arregloEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloEntrenador.add(BEntrenador(1,"Brandon", "a@a.com"))
            arregloEntrenador.add(BEntrenador(2,"Steve", "b@b.com"))
            arregloEntrenador.add(BEntrenador(3,"Messi", "c@c.com"))
        }
    }
}
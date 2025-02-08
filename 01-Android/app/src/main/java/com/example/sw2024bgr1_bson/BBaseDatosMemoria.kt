package com.example.sw2024bgr1_bson

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Brandon", "a@a.com"))
            arregloBEntrenador.add(BEntrenador(2,"Steve", "b@b.com"))
            arregloBEntrenador.add(BEntrenador(3,"Messi", "c@c.com"))
        }
    }
}
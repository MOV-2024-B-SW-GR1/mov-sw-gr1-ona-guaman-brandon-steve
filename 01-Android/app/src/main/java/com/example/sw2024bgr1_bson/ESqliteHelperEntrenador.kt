package com.example.sw2024bgr1_bson

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class ESqliteHelperEntrenador(
    contexto: Context? //this
    ): SQLiteOpenHelper(
        contexto,
        "moviles",
        null,
        1
    ){
        override fun onCreate(db: SQLiteDatabase?) {
            val scriptSQLCrearTablaEntrenador =
                """
                    CREATE TABLE ENTRENADOR(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre VARCHAR(50),
                        descripcion VARCHAR(50)
                    
                    )
                """.trimIndent()
            db?.execSQL(scriptSQLCrearTablaEntrenador)
        }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun crearEntrenador(nombre:String, descripcion: String): Boolean{
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("descripcion", descripcion)
        val resultadosGuardar = baseDatosEscritura.insert(
            "ENTRENADOR",
            null,
            valoresGuardar
        )
        baseDatosEscritura.close()
        return if (resultadosGuardar.toInt()==-1) false else true
    }

    fun eliminarEntrenador(id:Int):Boolean{
        val baseDatosEscritura = writableDatabase
        //where ..ID? and nombre=? [?=1, ?=2]
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura
            .delete(
                "ENTRENADOR", //TABLA
                "id=?", //CONSULTA
                parametrosConsultaDelete
            )
        baseDatosEscritura.close()
        return if (resultadoEliminar.toInt()==-1) false else true
    }

    fun actualizarEntrenador(nombre:String, descripcion: String, id: Int):Boolean{
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        //where
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update(
            "ENTRENADOR", //TABLA
            valoresAActualizar, //valores
            "id=?", //id=1
            parametrosConsultaActualizar // [1]
        )
        baseDatosEscritura.close()
        return if (resultadoActualizar.toInt()==-1) false else true
    }
    fun consultarEntrenadorPorId(id:Int):BEntrenador?{
        val baseDatosLectura=readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM ENTRENADOR WHERE ID=?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura
            .rawQuery(
                scriptConsultaLectura,
                parametrosConsultaLectura
            )
        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        if(existeAlMenosUno){
            val arregloRespuesta = arrayListOf<BEntrenador>()
            do{
                val entrenador = BEntrenador(
                    resultadoConsultaLectura.getInt(0), //0=id
                    resultadoConsultaLectura.getString(1), //1=nombre
                    resultadoConsultaLectura.getString(2), //2=descipcion
                )
                arregloRespuesta.add(entrenador)
            }while (resultadoConsultaLectura.moveToNext())
            return arregloRespuesta[0] //En otros casos devolvemos el arreglo completo
        }else{
            return null
        }
    }
}
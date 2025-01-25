package com.example.aplicacionexamen
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class GestorSQL(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_PAIS = """
            CREATE TABLE Pais (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombrePais TEXT,
                codigoPais TEXT,
                fechaFundacion DATE
            )
        """

        private const val SQL_CREATE_TABLE_CIUDAD = """
            CREATE TABLE Ciudad (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreCiudad TEXT,
                poblacion REAL,
                esCapital INTEGER,
                tieneAereopuerto INTEGER,
                paisId INTEGER,
                FOREIGN KEY(paisId) REFERENCES Pais(id)
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        //db.execSQL(SQL_CREATE_TABLE_PAIS)
        //db.execSQL(SQL_CREATE_TABLE_CIUDAD)
        try {
            db.execSQL(SQL_CREATE_TABLE_PAIS)
            db.execSQL(SQL_CREATE_TABLE_CIUDAD)
            Log.d("GestorSQL", "Tablas creadas correctamente")
        } catch (e: Exception) {
            Log.e("GestorSQL", "Error al crear las tablas: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar las actualizaciones de la base de datos
    }

    // CRUD Operaciones para Pais
    fun addPais(nombreP: String, codigoP: String, fechaFun: String): Long {
        val db = this.writableDatabase
        val formattedDate = try {
            val parsedDate = dateFormat.parse(fechaFun)
            dateFormat.format(parsedDate ?: Date())
        } catch (e: Exception) {
            dateFormat.format(Date()) // Usa la fecha actual si hay error
        }
        val values = ContentValues().apply {
            put("nombrePais", nombreP)
            put("codigoPais", codigoP)
            put("fechaFundacion", formattedDate)
        }
        return db.insert("Pais", null, values)
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    fun getPais(): MutableList<Pais> {
        val db = this.readableDatabase
        val projection = arrayOf("id", "nombrePais", "codigoPais", "fechaFundacion")
        val cursor = db.query("Pais", projection, null, null, null, null, null)
        val pais = mutableListOf<Pais>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombrePais = getString(getColumnIndexOrThrow("nombrePais"))
                val codigoPais = getString(getColumnIndexOrThrow("codigoPais"))
                val fechaFundacionString = getString(getColumnIndexOrThrow("fechaFundacion"))
                // Parsear la fecha desde el formato almacenado
                val fechaFundacion = dateFormat.parse(fechaFundacionString) ?: Date()

                pais.add(Pais(id, nombrePais, codigoPais, fechaFundacion))   //Posible error
            }
        }
        cursor.close()
        return pais
    }



    fun updatePais(id: Int, nombrePais: String, codigoPais: String, fechaFundacion:String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombrePais", nombrePais)
            put("codigoPais", codigoPais)
            put("fechaFundacion", fechaFundacion)
        }
        return db.update("Pais", values, "id=?", arrayOf(id.toString()))
    }

    fun deletePais(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Pais", "id=?", arrayOf(id.toString()))
    }
//----------------------------------------------------------------
    // CRUD Operaciones para Ciudad
    fun addCiudad(nombreCiudad: String, poblacion: Double, esCapital: String, tieneAereopuerto: String, paisId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombreCiudad", nombreCiudad) // Nombre de la ciudad
            put("poblacion", poblacion)       // Población de la ciudad
            //put("esCapital", if (esCapital) 1 else 0) // Guardar esCapital como 1 (true) o 0 (false)
            //put("tieneAereopuerto", if (tieneAereopuerto) 1 else 0) // Guardar tieneAereopuerto como 1 (true) o 0 (false)
            put("esCapital", esCapital)
            put("tieneAereopuerto", tieneAereopuerto)
            put("paisId", paisId)             // ID del país relacionado
        }
        return db.insert("Ciudad", null, values)
    }

    fun getCiudad(): MutableList<Ciudad> {
        val db = this.readableDatabase
        val projection = arrayOf("id", "nombreCiudad", "poblacion", "esCapital", "tieneAereopuerto", "paisId")
        val cursor = db.query("Ciudad", projection, null, null, null, null, null)
        val ciudads = mutableListOf<Ciudad>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombreCiudad = getString(getColumnIndexOrThrow("nombreCiudad"))
                val poblacion = getDouble(getColumnIndexOrThrow("poblacion"))
                //val esCapital = getInt(getColumnIndexOrThrow("esCapital")) == 1
                //val tieneAereopuerto = getInt(getColumnIndexOrThrow("tieneAereopuerto")) == 1
                val esCapital = getString(getColumnIndexOrThrow("esCapital"))
                val tieneAereopuerto = getString(getColumnIndexOrThrow("tieneAereopuerto"))
                val paisId = getInt(getColumnIndexOrThrow("paisId"))
                ciudads.add(Ciudad(id, nombreCiudad, poblacion, esCapital, tieneAereopuerto, paisId))
            }
        }
        cursor.close()
        return ciudads
    }

    fun updateCiudad(id: Int, nombreCiudad: String, poblacion: Double, esCapital: String, tieneAereopuerto: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombreCiudad", nombreCiudad) // Actualiza el nombre de la ciudad
            put("poblacion", poblacion)       // Actualiza la población
            //put("esCapital", if (esCapital) 1 else 0) // Convierte esCapital a 1 (true) o 0 (false)
            //put("tieneAereopuerto", if (tieneAereopuerto) 1 else 0) // Convierte tieneAereopuerto a 1 o 0
            put("esCapital", esCapital)
            put("tieneAereopuerto", tieneAereopuerto)
        }
        return db.update("Ciudad", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteCiudad(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Ciudad", "id=?", arrayOf(id.toString()))
    }
}

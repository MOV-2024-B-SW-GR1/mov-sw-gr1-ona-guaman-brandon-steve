package com.example.proyectoIIB
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.Date
import java.text.SimpleDateFormat

class GestorSQL(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_CATEGORIA = """
            CREATE TABLE Categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreCategoria TEXT,
                descripcion TEXT,
                fechaCreacion DATE
            )
        """

        private const val SQL_CREATE_TABLE_TAREA = """
            CREATE TABLE Tarea (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreTarea TEXT,
                descripcionT TEXT,
                fechaVencimiento DATE,
                prioridad TEXT,
                estado TEXT,
                categoriaId INTEGER,
                FOREIGN KEY(categoriaId) REFERENCES Categoria(id)
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        //db.execSQL(SQL_CREATE_TABLE_PAIS)
        //db.execSQL(SQL_CREATE_TABLE_CIUDAD)
        try {
            db.execSQL(SQL_CREATE_TABLE_CATEGORIA)
            db.execSQL(SQL_CREATE_TABLE_TAREA)
            Log.d("GestorSQL", "Tablas creadas correctamente")
        } catch (e: Exception) {
            Log.e("GestorSQL", "Error al crear las tablas: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar las actualizaciones de la base de datos
    }

    // CRUD Operaciones para Pais
    fun addCategoria(nombreCategoria: String, descripcion: String, fechaCreacion: String): Long {
        val db = this.writableDatabase
        val formattedDate = try {
            val parsedDate = dateFormat.parse(fechaCreacion)
            dateFormat.format(parsedDate ?: Date())
        } catch (e: Exception) {
            dateFormat.format(Date()) // Usa la fecha actual si hay error
        }
        val values = ContentValues().apply {
            put("nombreCategoria", nombreCategoria)
            put("descripcion", descripcion)
            put("fechaCreacion", formattedDate)
        }
        //return db.insert("Pais", null, values)
        val id = db.insert("Categoria", null, values)

        if (id == -1L) {
            Log.e("GestorSQL", "Error al insertar el país en la base de datos")
        } else {
            Log.d("GestorSQL", "País insertado correctamente con ID: $id")
        }

        return id
    }

    //private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    fun getCategoria(): MutableList<Categoria> {
        val db = this.readableDatabase
        val projection = arrayOf("id", "nombreCategoria", "descripcion", "fechaCreacion")
        val cursor = db.query("Categoria", projection, null, null, null, null, null)
        val pais = mutableListOf<Categoria>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombreCategoria = getString(getColumnIndexOrThrow("nombreCategoria"))
                val descripcion = getString(getColumnIndexOrThrow("descripcion"))
                val fechaCreacionString = getString(getColumnIndexOrThrow("fechaCreacion"))
                // Parsear la fecha desde el formato almacenado
                val fechaCreacion = dateFormat.parse(fechaCreacionString) ?: Date()

                pais.add(Categoria(id, nombreCategoria, descripcion, fechaCreacion))
            }
        }
        cursor.close()
        return pais
    }



    fun updateCategoria(id: Int, nombreCategoria: String, descripcion: String, fechaCreacion:String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombreCategoria", nombreCategoria)
            put("descripcion", descripcion)
            put("fechaCreacion", fechaCreacion)
        }
        return db.update("Categoria", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteCategoria(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Categoria", "id=?", arrayOf(id.toString()))
    }
//----------------------------------------------------------------
    // CRUD Operaciones para Tarea
    fun addTarea(nombreTarea: String, descripcionT: String, fechaVencimiento: String, prioridad: String, estado: String, categoriaId: Int): Long {
    if (categoriaId == -1) {
        Log.e("GestorSQL", "Error: paisId inválido al insertar ciudad")
        return -1
    }
        val db = this.writableDatabase

    val formattedDate = try {
        val parsedDate = dateFormat.parse(fechaVencimiento)
        dateFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        dateFormat.format(Date()) // Usa la fecha actual si hay error
    }

        val values = ContentValues().apply {
            put("nombreTarea", nombreTarea) // Nombre de la ciudad
            put("descripcionT", descripcionT)       // Población de la ciudad
            //put("esCapital", if (esCapital) 1 else 0) // Guardar esCapital como 1 (true) o 0 (false)
            //put("tieneAereopuerto", if (tieneAereopuerto) 1 else 0) // Guardar tieneAereopuerto como 1 (true) o 0 (false)
            put("fechaVencimiento", formattedDate)
            put("prioridad", prioridad)
            put("estado", estado)
            put("categoriaId", categoriaId)             // ID del país relacionado
        }
        //return db.insert("Ciudad", null, values)
        val id = db.insert("Tarea", null, values)

        if (id == -1L) {
            Log.e("GestorSQL", "Error al insertar la ciudad en la base de datos")
        } else {
            Log.d("GestorSQL", "Ciudad agregada correctamente con ID: $id")
        }
        return id
    }

    fun getTarea(categoriaId: Int): MutableList<Tarea> {
        Log.d("GestorSQL", "Obteniendo ciudades para paisId: $categoriaId")  // 🟢 Log de depuración

        val db = this.readableDatabase
        val projection = arrayOf("id", "nombreTarea", "descripcionT", "fechaVencimiento", "prioridad", "estado", "categoriaId")
        val cursor = db.query(
            "Tarea", projection,
            "categoriaId = ?", arrayOf(categoriaId.toString()), // Filtra por paisId
            null, null, null
        )

        val tareas = mutableListOf<Tarea>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombreTarea = getString(getColumnIndexOrThrow("nombreTarea"))
                val descripcionT = getString(getColumnIndexOrThrow("descripcionT"))
                val fechaVencimientoString = getString(getColumnIndexOrThrow("fechaVencimiento"))
                val fechaVencimiento = dateFormat.parse(fechaVencimientoString) ?: Date()
                val prioridad = getString(getColumnIndexOrThrow("prioridad"))
                val estado = getString(getColumnIndexOrThrow("estado"))
                val categoriadDb = getInt(getColumnIndexOrThrow("categoriaId"))


                Log.d("GestorSQL", "Ciudad encontrada en BD: ID=$id, Nombre=$nombreTarea, paisId=$categoriadDb")  // 🟢 Log para verificar si se están recuperando ciudades

                tareas.add(Tarea(id, nombreTarea, descripcionT, fechaVencimiento, prioridad, estado, categoriadDb))
            }
        }
        cursor.close()
        return tareas
    }

    fun updateTarea(id: Int, nombreTarea: String, descripcionT: String, fechaVencimiento: String, prioridad: String, estado: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombreTarea", nombreTarea) // Actualiza el nombre de la ciudad
            put("descripcionT", descripcionT)       // Actualiza la población
            //put("esCapital", if (esCapital) 1 else 0) // Convierte esCapital a 1 (true) o 0 (false)
            //put("tieneAereopuerto", if (tieneAereopuerto) 1 else 0) // Convierte tieneAereopuerto a 1 o 0
            put("fechaVencimiento", fechaVencimiento)
            put("prioridad", prioridad)
            put("estado", estado)
        }
        return db.update("Tarea", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteTarea(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Tarea", "id=?", arrayOf(id.toString()))
    }
}

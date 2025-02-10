package com.example.proyectoIIB

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat

class CrearTareaActivity : AppCompatActivity() {
    private lateinit var gestorSQL: GestorSQL

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)

        gestorSQL = GestorSQL(this)  // Instancia de GestorSQL

        val nombreCategoriaEditText = findViewById<EditText>(R.id.nombreTarea)
        val descripcionEditText = findViewById<EditText>(R.id.descripcionT)
        val fechaVencimientoEditText = findViewById<EditText>(R.id.fechaCreacion)
        val prioridadEditText = findViewById<EditText>(R.id.prioridad)
        val estadoEditText = findViewById<EditText>(R.id.estado)
        val guardarFacturaButton = findViewById<Button>(R.id.GuardarTarea)
        //val paisId = intent.getIntExtra("paisId", 0)  // Asumiendo que el paisId es pasado a esta actividad
        val categoriaId = intent.getIntExtra("categoriaId", -1)
        if (categoriaId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID del país", Toast.LENGTH_SHORT).show()
            //return@setOnClickListener
        }
        //val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        guardarFacturaButton.setOnClickListener {
            val nombreTarea = nombreCategoriaEditText.text.toString().trim()
            val descripcion = descripcionEditText.text.toString().trim()
            //val esCapital = esCapitaEditText.text.toString().trim().lowercase()=="si"
            //val tieneAeropuerto = tieneAereopuertoEditText.text.toString().trim().lowercase()=="si"
            val fechaVencimiento = fechaVencimientoEditText.text.toString().trim()
            val prioridad = prioridadEditText.text.toString()
            val estado = estadoEditText.text.toString()

            // Validar y convertir la población a Double
            try {
                dateFormat.parse(fechaVencimiento) // Intenta analizar la fecha
                // Guardar directamente en la base de datos
                val id = gestorSQL.addTarea(nombreTarea, descripcion, fechaVencimiento, prioridad, estado, categoriaId)
                if (id > 0) {
                    Log.d("CrearCiudadActivity", "Ciudad creada con ID: $id")
                    setResult(RESULT_OK)  // Indica que la ciudad fue creada con éxito
                } else {
                    Log.e("CrearCiudadActivity", "Error al guardar la ciudad")
                    setResult(RESULT_CANCELED)  // Indica que hubo un error
                }
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: Fecha inválida, usa formato yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

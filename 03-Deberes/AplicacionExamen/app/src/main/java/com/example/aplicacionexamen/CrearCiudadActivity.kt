package com.example.aplicacionexamen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CrearCiudadActivity : AppCompatActivity() {
    private lateinit var gestorSQL: GestorSQL

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ciudad)

        gestorSQL = GestorSQL(this)  // Instancia de GestorSQL

        val nombreCiudadEditText = findViewById<EditText>(R.id.nombreCiudad)
        val poblacionEditText = findViewById<EditText>(R.id.poblacionCiudad)
        val esCapitaEditText = findViewById<EditText>(R.id.esCapital)
        val tieneAereopuertoEditText = findViewById<EditText>(R.id.tieneAereoPuerto)
        val guardarFacturaButton = findViewById<Button>(R.id.GuardarFacturaBt)
        val paisId = intent.getIntExtra("paisId", 0)  // Asumiendo que el paisId es pasado a esta actividad

        guardarFacturaButton.setOnClickListener {
            val nombreCiudad = nombreCiudadEditText.text.toString().trim()
            val poblacionText = poblacionEditText.text.toString().trim()
            //val esCapital = esCapitaEditText.text.toString().trim().lowercase()=="si"
            //val tieneAeropuerto = tieneAereopuertoEditText.text.toString().trim().lowercase()=="si"
            val esCapital = esCapitaEditText.text.toString().trim()
            val tieneAeropuerto = tieneAereopuertoEditText.text.toString()

            // Validar y convertir la población a Double
            val poblacion: Double = try {
                poblacionText.toDouble()
            } catch (e: NumberFormatException) {
                // Si no es un número válido, muestra un mensaje o maneja el error
                Toast.makeText(this, "Ingrese un valor numérico para la población", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar directamente en la base de datos
            val id = gestorSQL.addCiudad(nombreCiudad, poblacion, esCapital, tieneAeropuerto, paisId)
            if (id > 0) {
                Log.d("CrearCiudadActivity", "Ciudad creada con ID: $id")
                setResult(RESULT_OK)  // Indica que la ciudad fue creada con éxito
            } else {
                Log.e("CrearCiudadActivity", "Error al guardar la ciudad")
                setResult(RESULT_CANCELED)  // Indica que hubo un error
            }
            finish()
        }
    }
}

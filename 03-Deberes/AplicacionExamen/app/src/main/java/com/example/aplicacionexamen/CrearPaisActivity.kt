package com.example.aplicacionexamen

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class CrearPaisActivity : AppCompatActivity() {
    private lateinit var gestorSQL: GestorSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pais)

        gestorSQL = GestorSQL(this)  // Instancia de GestorSQL

        val nombrePaisEditText = findViewById<EditText>(R.id.NombrePais)
        val codigoEditText = findViewById<EditText>(R.id.codigoPais)
        val fechaFEditText = findViewById<EditText>(R.id.fechaFundacion)
        val guardarButton = findViewById<Button>(R.id.guardarPais)

        //val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        guardarButton.setOnClickListener {
            val nombreP = nombrePaisEditText.text.toString().trim()
            val codigo = codigoEditText.text.toString().trim()
            val fechaFun = fechaFEditText.text.toString().trim()

            try {
                dateFormat.parse(fechaFun) // Intenta analizar la fecha
                val id = gestorSQL.addPais(nombreP, codigo, fechaFun)
                if (id > 0) {
                    Log.d("CrearPaisActivity", "País creado con ID: $id")
                    setResult(RESULT_OK)
                } else {
                    Log.e("CrearPaisActivity", "Error al guardar el país")
                    setResult(RESULT_CANCELED)
                }
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: Fecha inválida, usa formato yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

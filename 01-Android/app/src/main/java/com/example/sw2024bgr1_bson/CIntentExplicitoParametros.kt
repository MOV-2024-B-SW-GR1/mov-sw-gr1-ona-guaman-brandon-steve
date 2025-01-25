package com.example.sw2024bgr1_bson

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CIntentExplicitoParametros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cintent_explicito_parametros)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    val nombre = intent.getStringExtra("Nombre")
    val apellido = intent.getStringExtra("Apellido")
    val edad = intent.getIntExtra("Edad",0)
    val boton = findViewById<Button>(R.id.btn_devolver_respuesta)
    boton.setOnClickListener{
        val intentDevolverRespuesta = intent()
        intentDevolverRespuesta.putExtra(
            "nombreModificado", "$nombre", "$edad", "$apellido"
        )
        setResult(RESULT_OK, intentDevolverRespuesta)
        finish()
    }

}
package com.example.sw2024bgr1_bson

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ciclo_vida)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Inicializar la BDD
        EBaseDeDatos.tablaEntrenador=ESqliteHelperEntrenador(this)

        val botonCicloVida = findViewById<Button>(R.id.cl_ciclo_vida)
        botonCicloVida
            .setOnClickListener {
                irActividad(ACicloVida::class.java)
            }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonCicloVida
            .setOnClickListener {
                irActividad(BListView::class.java)
            }

        val botonImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonImplicito
            .setOnClickListener{
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                callbackContenidoIntentImplicito.launch(intentConRespuesta)
            }

        val botonExplicito = findViewById<Button>(R.id.btn_ir_intent_explicito)
        botonExplicito
            .setOnClickListener{
                val intentExplixito = Intent(
                    this, CIntentExplicitoParametros::class.java
                )
                intentExplixito.putExtra("Nombre", "Brandon")
                intentExplixito.putExtra("Apellido", "Oña")
                intentExplixito.putExtra("Edad", "34")
                intentExplixito.putExtra("entrenador",
                    BEntrenador(1,"Brandon", "Ejemplo"))
                callbackContenidoIntentExplicito.launch(intentExplixito)
            }
        val botonIrSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonIrSqlite
            .setOnClickListener {
                irActividad(ECrudEntrenador::class.java)
            }
        val botonRecyclerView = findViewById<Button>(R.id.btn_recycler_view)
        botonRecyclerView
            .setOnClickListener {
                irActividad(FRecyclerView::class.java)
            }
        val botonGMaps = findViewById<Button>(R.id.btn_google_maps)
        botonGMaps
            .setOnClickListener {
                irActividad(GGoogleMaps::class.java)
            }
        val botonAuth = findViewById<Button>(R.id.btn_intent_firebase_ui)
        botonAuth
            .setOnClickListener {
                irActividad(HFirebaseUIAuth::class.java)
            }

    }

    fun irActividad(clase:Class<*>){
        startActivity(Intent(this,clase))
    }
//comentario
    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.main_ciclo_vida),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
    val callbackContenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result->
        if(result.resultCode== Activity.RESULT_OK){
            if(result.data != null){
                val data = result.data?.getStringExtra("nombreModificado")
                mostrarSnackbar("$data")
            }
        }
    }

    val callbackContenidoIntentImplicito=
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    //validacion de contacto
                    if(result.data!!.data !=null){
                        val uri: Uri = result.data!!.data!!
                        val cursor = contentResolver.query(
                            uri, null, null, null, null, null
                        )
                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val telefono = cursor?.getString(indiceTelefono!!)
                        cursor?.close()
                        mostrarSnackbar("Telefono $telefono")
                    }
                }
            }
        }




}
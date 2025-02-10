package com.example.proyectoIIB


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class CiudadActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this)

    private var paisId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudad)

        listView = findViewById(R.id.CiudadesListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadCiudades())
        listView.adapter = adapter
        registerForContextMenu(listView)

        val crearFacturaButton = findViewById<Button>(R.id.CrearCiudadBt)
        crearFacturaButton.setOnClickListener {
            val paisId = intent.getIntExtra("paisId", -1)  // Obtiene el paisId correctamente
            val intent = Intent(this, CrearCiudadActivity::class.java)
            //intent.putExtra("paisId", intent.getIntExtra("paisId", -1)) // Pasar el paisId correcto
            intent.putExtra("paisId", paisId)
            startActivityForResult(intent, 2)
        }

        val regresarButton = findViewById<Button>(R.id.RegresarBt)
        regresarButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        updateListView() // Se asegura de actualizar la lista cuando se vuelve a la actividad
    }

    private fun loadCiudades(): List<String> {
        /*return gestorSQL.getCiudad().map { it.nombreCiudad + " - Población: " + it.poblacion +
                "M - Capital?: " + it.esCapital + " - Aereopuerto: "+it.tieneAereopuerto }*/
        val paisId = intent.getIntExtra("paisId", -1)  // Obtiene el paisId del intent
        if (paisId == -1) {
            Log.e("CiudadActivity", "Error: paisId no recibido")
            return emptyList()
        }
        return gestorSQL.getCiudad(paisId).map { it.nombreCiudad + " - Población: " + it.poblacion +
                "M - Capital?: " + it.esCapital + " - Aeropuerto: "+it.tieneAereopuerto }
    }

    private fun updateListView() {
        val paisId = intent.getIntExtra("paisId", -1)
        if (paisId == -1) {
            Log.e("CiudadActivity", "Error: No se pudo obtener el paisId en updateListView()")
            return
        }
        val ciudades = gestorSQL.getCiudad(paisId)
        adapter.clear()
        adapter.addAll(ciudades.map { it.nombreCiudad + " - Población: " + it.poblacion +
                "M - Capital?: " + it.esCapital + " - Aeropuerto: "+it.tieneAereopuerto })
        adapter.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.ciudad_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val paisId = intent.getIntExtra("paisId", -1)
        if (paisId == -1) {
            Log.e("CiudadActivity", "Error: paisId no recibido")
            return super.onContextItemSelected(item)
        }

        val ciudadesDelPais = gestorSQL.getCiudad(paisId)  // Obtener solo las ciudades del país
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        if (info.position < 0 || info.position >= ciudadesDelPais.size) {
            Log.e("CiudadActivity", "Error: Posición inválida en la lista de ciudades")
            return super.onContextItemSelected(item)
        }

        val ciudadSeleccionada = ciudadesDelPais[info.position]  // Obtener la ciudad correcta

        when (item.itemId) {
            R.id.edit -> editCiudad(info.position)  // Asegurar que edita la ciudad correcta
            R.id.delete -> {
                gestorSQL.deleteCiudad(ciudadSeleccionada.id)  // Ahora borra la ciudad correcta
                updateListView()
            }
            //R.id.map -> showMap(ciudadSeleccionada.nombreCiudad)  // Asegurar que muestra el mapa correcto
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun editCiudad(position: Int) {
        val paisId = intent.getIntExtra("paisId", -1)
        if (paisId == -1) {
            Log.e("CiudadActivity", "Error: paisId no recibido")
            return
        }

        val ciudadesDelPais = gestorSQL.getCiudad(paisId)  // Obtener solo las ciudades de este país
        if (position < 0 || position >= ciudadesDelPais.size) {
            Log.e("CiudadActivity", "Error: posición inválida en la lista de ciudades")
            return
        }

        val ciudad = ciudadesDelPais[position]  // Obtener la ciudad en la posición correcta

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Ciudad")

        val input = EditText(this)
        input.setText("${ciudad.nombreCiudad} - ${ciudad.poblacion} - ${ciudad.esCapital} - ${ciudad.tieneAereopuerto}")
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val parts = input.text.toString().split(" - ")
            if (parts.size >= 4) {
                val poblacion = parts[1].toDoubleOrNull() ?: 0.0  // Manejo seguro de conversión
                val esCapital = parts[2]
                val tieneAeropuerto = parts[3]

                gestorSQL.updateCiudad(ciudad.id, parts[0], poblacion, esCapital, tieneAeropuerto)
                updateListView()
            } else {
                Log.e("CiudadActivity", "Error: Formato de entrada inválido")
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    // ----------------------------------------------------====================


}

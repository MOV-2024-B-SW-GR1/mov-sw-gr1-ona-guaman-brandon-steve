package com.example.aplicacionexamen


import android.content.Intent
import android.net.Uri
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CiudadActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudad)

        listView = findViewById(R.id.AutosListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadCiudades())
        listView.adapter = adapter
        registerForContextMenu(listView)

        val crearFacturaButton = findViewById<Button>(R.id.CrearAutosBt)
        crearFacturaButton.setOnClickListener {
            val intent = Intent(this, CrearCiudadActivity::class.java)
            startActivityForResult(intent, 2)
        }

        val regresarButton = findViewById<Button>(R.id.RegresarBt)
        regresarButton.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            updateListView()
        }
    }

    private fun loadCiudades(): List<String> {
        return gestorSQL.getCiudad().map { it.nombreCiudad + " - Población: " + it.poblacion +
                "M - Capital?: " + it.esCapital + " - Aereopuerto: "+it.tieneAereopuerto }
    }

    private fun updateListView() {
        val ciudades = loadCiudades()
        adapter.clear()
        adapter.addAll(loadCiudades())
        adapter.notifyDataSetChanged()
        Log.d("CiudadActivity", "Lista actualizada con ${ciudades.size} ciudades")
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.ciudad_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.edit -> editCiudad(info.position)
            R.id.delete -> {
                gestorSQL.deleteCiudad(gestorSQL.getCiudad()[info.position].id)
                updateListView()
            }
            //R.id.map -> showMap(gestorSQL.getCiudad()[info.position].nombreCiudad)
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun editCiudad(position: Int) {
        val factura = gestorSQL.getCiudad()[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Factura")

        val input = EditText(this)
        input.setText(factura.nombreCiudad + " - " + factura.poblacion + " - " + factura.esCapital+ " - " +factura.tieneAereopuerto)
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, which ->
            val parts = input.text.toString().split(" - ")
            if (parts.size >= 4) {
                val poblacion = parts[1].toDouble()
                //val esCapital = parts[2].toBooleanStrict()
                //val tieneAeropuerto = parts[3].toBooleanStrict()
                val esCapital = parts[2]
                val tieneAeropuerto = parts[3]
                gestorSQL.updateCiudad(factura.id, parts[0], poblacion, esCapital, tieneAeropuerto)
                updateListView()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun showMap(ubicacion: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(ubicacion)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps no está instalado.", Toast.LENGTH_SHORT).show()
        }
    }
}

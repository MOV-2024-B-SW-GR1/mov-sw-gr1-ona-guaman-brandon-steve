package com.example.aplicacionexamen


import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this) // Instancia de la clase GestorSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewConcesionario)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadPais())
        listView.adapter = adapter

        val crearConcesionarioButton = findViewById<Button>(R.id.crearConcesionario)
        crearConcesionarioButton.setOnClickListener {
            val intent = Intent(this, CrearPaisActivity::class.java)
            startActivityForResult(intent, 1)  // Use request code to identify the result
        }

        registerForContextMenu(listView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            adapter.clear()
            adapter.addAll(loadPais())
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadPais(): MutableList<String> {
        return gestorSQL.getPais().map { it.nombrePais + "- Codigo: " + it.codigo + "- Fundación: " + it.fechaFundacion }.toMutableList()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.edit -> editPais(info.position)
            R.id.delete -> {
                gestorSQL.deletePais(gestorSQL.getPais()[info.position].id)
                updateListView()
            }
            R.id.view_paises -> viewCiudades(info.position)
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private fun editPais(position: Int) {
        val client = gestorSQL.getPais()[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Pais")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT

        // Formatea la fecha para mostrarla como cadena
        val formattedDate = dateFormat.format(client.fechaFundacion)
        //input.setText(client.nombrePais + " " + client.codigo + " "+ client.fechaFundacion)
        input.setText("${client.nombrePais} ${client.codigo} $formattedDate")
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, which ->
            val parts = input.text.toString().split(" ")
            gestorSQL.updatePais(client.id, parts[0], parts.getOrElse(1) { "" }, parts.getOrElse(2){""})
            updateListView()
        }
        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun updateListView() {
        adapter.clear()
        adapter.addAll(loadPais())
        adapter.notifyDataSetChanged()
    }

    private fun viewCiudades(position: Int) {
        val intent = Intent(this, CiudadActivity::class.java)
        intent.putExtra("paisId", gestorSQL.getPais()[position].id)
        startActivity(intent)
    }
}

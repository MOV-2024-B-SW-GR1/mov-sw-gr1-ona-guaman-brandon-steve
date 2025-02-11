package com.example.proyectoIIB


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this) // Instancia de la clase GestorSQL

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewPaises)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadCategorias())
        listView.adapter = adapter

        val crearConcesionarioButton = findViewById<Button>(R.id.crearCategorias)
        crearConcesionarioButton.setOnClickListener {
            val intent = Intent(this, CrearCategoriaActivity::class.java)
            startActivityForResult(intent, 1)  // Use request code to identify the result
        }

        registerForContextMenu(listView)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "Actualizando lista de países en onResume()") // Verifica si entra aquí
        updateListView()
    }

    private fun loadCategorias(): MutableList<String> {
        return gestorSQL.getCategoria().map { it.nombreCategoria + "- Descripcion: " + it.Descripcion + "- Fecha Creacion: " + it.fechaCreacion }.toMutableList()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.edit -> editCategoria(info.position)
            R.id.delete -> {
                gestorSQL.deleteCategoria(gestorSQL.getCategoria()[info.position].id)
                updateListView()
            }
            R.id.view_tareas -> viewTareas(info.position)
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    //private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private fun editCategoria(position: Int) {
        val client = gestorSQL.getCategoria()[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Pais")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT

        // Formatea la fecha para mostrarla como cadena
        val formattedDate = dateFormat.format(client.fechaCreacion)
        //input.setText(client.nombrePais + " " + client.codigo + " "+ client.fechaFundacion)
        input.setText("${client.nombreCategoria} - ${client.Descripcion} - $formattedDate")
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, which ->
            val parts = input.text.toString().split(" - ")
            gestorSQL.updateCategoria(client.id, parts[0], parts.getOrElse(1) { "" }, parts.getOrElse(2){""})
            updateListView()
        }
        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun updateListView() {
        val categorias = gestorSQL.getCategoria()
        if (categorias.isEmpty()) {
            Log.e("MainActivity", "No se encontraron países en la base de datos")
        } else {
            Log.d("MainActivity", "Se encontraron ${categorias.size} países en la base de datos")
        }

        adapter.clear()
        adapter.addAll(categorias.map { it.nombreCategoria + " - Descripcion: " + it.Descripcion + " - Fecha Creacion: " + it.fechaCreacion })
        adapter.notifyDataSetChanged()
    }

    private fun viewTareas(position: Int) {
        val categoria = gestorSQL.getCategoria()[position]
        Log.d("MainActivity", "Abriendo CiudadActivity con paisId: ${categoria.id}")  //Log de verificación

        val intent = Intent(this, TareaActivity::class.java)
        intent.putExtra("categoriaId", categoria.id)
        startActivity(intent)
    }

}

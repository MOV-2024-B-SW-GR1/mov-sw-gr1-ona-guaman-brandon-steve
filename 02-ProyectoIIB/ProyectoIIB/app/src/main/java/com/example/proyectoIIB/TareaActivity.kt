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
import java.text.SimpleDateFormat


class TareaActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this)

    private var paisId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        listView = findViewById(R.id.CiudadesListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadTareas())
        listView.adapter = adapter
        registerForContextMenu(listView)

        val crearFacturaButton = findViewById<Button>(R.id.CrearTarea)
        crearFacturaButton.setOnClickListener {
            val categoriaId = intent.getIntExtra("categoriaId", -1)  // Obtiene el paisId correctamente
            val intent = Intent(this, CrearTareaActivity::class.java)
            //intent.putExtra("paisId", intent.getIntExtra("paisId", -1)) // Pasar el paisId correcto
            intent.putExtra("categoriaId", categoriaId)
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

    private fun loadTareas(): List<String> {
        /*return gestorSQL.getCiudad().map { it.nombreCiudad + " - Población: " + it.poblacion +
                "M - Capital?: " + it.esCapital + " - Aereopuerto: "+it.tieneAereopuerto }*/
        val categoriaId = intent.getIntExtra("categoriaId", -1)  // Obtiene el paisId del intent
        if (categoriaId == -1) {
            Log.e("CiudadActivity", "Error: paisId no recibido")
            return emptyList()
        }
        return gestorSQL.getTarea(categoriaId).map { it.nombreTarea + " - Descripcion: " + it.descricionT + "Fecha Vencimiento: " + it.fechaVencimiento + " - Prioridad: "+it.prioridad + " - Estado: "+it.estado }
    }

    //private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private fun updateListView() {
        val categoriaId = intent.getIntExtra("categoriaId", -1)
        if (categoriaId == -1) {
            Log.e("CiudadActivity", "Error: No se pudo obtener el paisId en updateListView()")
            return
        }
        val tareas = gestorSQL.getTarea(categoriaId)
        adapter.clear()
        adapter.addAll(tareas.map { it.nombreTarea + " - Descripcion: " + it.descricionT + " - Fecha Vencimiento: " + it.fechaVencimiento + " - Prioridad: "+it.prioridad + " - Estado: " +it.estado })
        adapter.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.tarea_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val categoriaId = intent.getIntExtra("categoriaId", -1)
        if (categoriaId == -1) {
            Log.e("CiudadActivity", "Error: paisId no recibido")
            return super.onContextItemSelected(item)
        }

        val tareasDeCategoria = gestorSQL.getTarea(categoriaId)  // Obtener solo las ciudades del país
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        if (info.position < 0 || info.position >= tareasDeCategoria.size) {
            Log.e("CiudadActivity", "Error: Posición inválida en la lista de ciudades")
            return super.onContextItemSelected(item)
        }

        val tareaSeleccionada = tareasDeCategoria[info.position]  // Obtener la ciudad correcta

        when (item.itemId) {
            R.id.edit -> editTarea(info.position)  // Asegurar que edita la ciudad correcta
            R.id.delete -> {
                gestorSQL.deleteTarea(tareaSeleccionada.id)  // Ahora borra la ciudad correcta
                updateListView()
            }
            //R.id.map -> showMap(ciudadSeleccionada.nombreCiudad)  // Asegurar que muestra el mapa correcto
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun editTarea(position: Int) {
        val categoriaId = intent.getIntExtra("categoriaId", -1)
        if (categoriaId == -1) {
            Log.e("CiudadActivity", "Error: paisId no recibido")
            return
        }

        val tareasDeCategoria = gestorSQL.getTarea(categoriaId)  // Obtener solo las ciudades de este país
        if (position < 0 || position >= tareasDeCategoria.size) {
            Log.e("CiudadActivity", "Error: posición inválida en la lista de ciudades")
            return
        }

        val tarea = tareasDeCategoria[position]  // Obtener la ciudad en la posición correcta

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Ciudad")

        // Formatea la fecha para mostrarla como cadena
        val formattedDate = dateFormat.format(tarea.fechaVencimiento)

        val input = EditText(this)
        input.setText("${tarea.nombreTarea} - ${tarea.descricionT} - $formattedDate - ${tarea.prioridad} - ${tarea.estado}")
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val parts = input.text.toString().split(" - ")
            if (parts.size >= 5) {
                //val nombreTareas = parts[1]
                val descripcion = parts[1]
                val fechaVencimiento = parts[2]
                val prioridad = parts[3]
                val estado = parts[4]

                gestorSQL.updateTarea(tarea.id, parts[0],descripcion, fechaVencimiento, prioridad, estado)
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

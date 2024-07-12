package com.example.myapplication

import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class BListView : AppCompatActivity() {

    val arreglo = BBaseDatosMemoria.arregloBEntrenador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blist_view)

        val listView = findViewById<ListView>(R.id.lv_list_view)

        val adaptador = ArrayAdapter(
            this, //contexto
            android.R.layout.simple_list_item_1, //layout xml a usar
            arreglo
        )

        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val botonAnadirListView = findViewById<Button>(
            R.id.btn_anadir_list_view
        )
        botonAnadirListView.setOnClickListener{
            anadirEntrenador(adaptador)
        }

        registerForContextMenu(listView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_blist_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }// ACABA EL ON CREATE

    var posicionItemSeleccionado = 0

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu,v,menuInfo)
        //llenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        //obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(
        item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar -> {
                mostrarSnackbar("Editar $posicionItemSeleccionado")
                return true
            }
            R.id.mi_eliminar -> {
                mostrarSnackbar("Eliminar $posicionItemSeleccionado")
                abrirDialogo()
                return true
            }

            else -> super.onContextItemSelected(item)
        }

    }

    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{
                dialogInterface, i ->
                mostrarSnackbar(("Eliminar Aceptado"))
            }
        )

        builder.setNegativeButton("Cancelar", null)
        val ocpiones = resources.getStringArray(R.array.string_array_opciones)
        val seleccionPrevia = booleanArrayOf(
            true, false, false
        )

        builder.setMultiChoiceItems(
            ocpiones, seleccionPrevia,
            {dialog, which, isChecked ->
                mostrarSnackbar("Dio click en el item $which")
            }
        )

    }

    fun anadirEntrenador(adaptador:ArrayAdapter<BEntrenador>){
        arreglo.add(
            BEntrenador(4,"Wendy","d@d.com")
        )
        adaptador.notifyDataSetChanged()
    }

    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_blist_view),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}

package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    fun mostrarSnakbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.id_layout_main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    //logica negocio
                    val data = result.data;
                    mostrarSnakbar(
                        "${result.data}"
                    )
                    mostrarSnakbar((
                            "${data?.getStringExtra("nombreModificado")}"
                            ))
                }
            }
        }

    val callbackContenidoIntentImplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    //logica negocio
                    if(result.data!!.data != null){
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
                        mostrarSnakbar("Telefono $telefono")
                    }
                }
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.id_layout_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonACicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonACicloVida
            .setOnClickListener{
                irACtividad(ACicloVida::class.java)
            }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView
            .setOnClickListener{
                irACtividad(BListView::class.java)
            }
        val botonIntentImpicito = findViewById<Button>(
            R.id.btn_ir_intent_implicito
        )
        botonIntentImpicito
            .setOnClickListener{
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                callbackContenidoIntentImplicito.launch(intentConRespuesta)
            }
        val botonIntentExplicito = findViewById<Button>(
            R.id.btn_ir_intent_explicit
        )
        botonIntentExplicito
            .setOnClickListener{
                val intentExplicito = Intent(
                    this,
                    CIntentExplicitoParametros::class.java
                )
                intentExplicito.putExtra("nombre","Adrian")
                intentExplicito.putExtra("apellido","Eguez")
                intentExplicito.putExtra("edad",34)
                intentExplicito.putExtra(
                    "entrenador",
                    BEntrenador(10,"Adrian","Eguez")
                )
                callbackContenidoIntentExplicito.launch(intentExplicito)
            }
        //Inicializar base de datos

        EBaseDeDatos.tablaEntrenador = ESqliteHelperEntrenador(
            this
        )

        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite.setOnClickListener{
            irACtividad(ECrudEntrenador::class.java)
        }

    }



    fun irACtividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
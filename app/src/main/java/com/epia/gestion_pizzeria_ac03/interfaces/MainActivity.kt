package com.epia.gestion_pizzeria_ac03.interfaces

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epia.gestion_pizzeria_ac03.R
import com.epia.gestion_pizzeria_ac03.adapters.pizzasAdapter
import com.epia.gestion_pizzeria_ac03.room.IvaDao
import com.epia.gestion_pizzeria_ac03.room.IvaDb
import com.epia.gestion_pizzeria_ac03.room.PizzaDao
import com.epia.gestion_pizzeria_ac03.room.Pizza
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    // Llançador per gestionar el resultat retornat
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var menu: Menu
    private lateinit var pizzaDao: PizzaDao
    private lateinit var parentLayout: View
    lateinit var myRecyclerView: RecyclerView
    var mAdapter: pizzasAdapter? = null
    var listaPizzas: MutableList<Pizza> = ArrayList()
    private var dato1: String = ""
    private var dato2: String = ""
    private var dato3: String = ""
    private var dato4: Double = 0.0
    private lateinit var ivaDao: IvaDao
    private var ivaActual: Int = 0  // Almacenar IVA


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pizzas)
//        parentLayout = findViewById<View>(android.R.id.content)
        supportActionBar?.show()

        intentPizzas()
       // intentConfig()
//
       // traerIva()

        pizzaDao = (applicationContext as App).db.pizzaDao()
        parentLayout = findViewById<View>(android.R.id.content)

        setContentView(R.layout.activity_pizzas)


        mostrarPizzas()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pizzas, menu)
        if (menu != null) {
            this.menu = menu
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAgregarPizza -> {
                val intent = Intent(this, secondActivity::class.java)
                resultLauncher.launch(intent)
                mostrarPizzas()
                true
            }
            R.id.itemBuscar -> {
                mostrarDialogBusqueda()
                true
            }
            R.id.itemConfig -> {
                val intent = Intent(this, configActivity::class.java)
                resultLauncher.launch(intent)
               //ajustarIva()
                //traerIva()
                mostrarPizzas()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addPizzas() {
        // Realizamos la inserción en un hilo de fondo (no en el hilo principal)
        GlobalScope.launch(Dispatchers.IO) {

            // Construimos el objeto Pizza
            var pizza = Pizza(dato1, dato2, dato3, dato4)
            Log.d("DEBUG", "Intentando insertar pizza: $pizza")

            pizzaDao.insert(pizza)

            // Actualizamos la interfaz en el hilo principal después de la inserción
            withContext(Dispatchers.Main) {

                // Mostramos un mensaje de éxito
                Snackbar.make(parentLayout, "Pizza creada con id" + pizza.referencia, Snackbar.LENGTH_SHORT).show()
                mostrarPizzas()
            }
        }
    }



    fun refrescarRecyclerView() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pizzas = pizzaDao.getPizzas() // Accedir a la base de dades en un fil de segon pla
                withContext(Dispatchers.Main) {
                    // Actualitzar el RecyclerView en el hilo principal
                    if (mAdapter == null) {
                        myRecyclerView = findViewById(R.id.recycler_pizzas) as RecyclerView
                        myRecyclerView.setHasFixedSize(true)
                        myRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        mAdapter = pizzasAdapter(pizzas,ivaActual,this@MainActivity)
                        myRecyclerView.adapter = mAdapter
                    } else {
                        mAdapter?.updateItems(pizzas)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Snackbar.make(parentLayout, "Error carregant les dades: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun intentPizzas(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                if (data != null) {
                    dato1 = data.getStringExtra("FIELD1") ?: ""
                    dato2 = data.getStringExtra("FIELD2") ?: ""
                    dato3 = data.getStringExtra("FIELD3") ?: ""
                    dato4 = data.getStringExtra("FIELD4")?.toDoubleOrNull() ?: 0.0

                    if (dato1.isNotEmpty() && dato2.isNotEmpty() && dato3.isNotEmpty() && dato4 > 0.0) {
                        Log.d("DEBUG", "Datos recibidos antes de insertar: dato1=$dato1, dato2=$dato2, dato3=$dato3, dato4=$dato4")
                        addPizzas()
                    } else {
                        Log.e("ERROR", "Los datos no son válidos")
                        Snackbar.make(parentLayout, "Datos de la pizza no son válidos", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("ERROR", "No se recibieron datos de la actividad")
                }
            }
        }
    }


    private fun mostrarDialogBusqueda() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Buscar Pizzas")

        // Configurar un EditText per introduir la cerca
        val input = EditText(this)
        input.hint = "Escribe la referencia a buscar"
        builder.setView(input)

        // Configurar els botons del diàleg
        builder.setPositiveButton("Buscar") { _, _ ->
            var searchText = input.text.toString()
            if (searchText.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    refrescarRecyclerView2(pizzaDao.buscarPizzaReferencia(searchText))
                    }
            } else {
                Toast.makeText(this, "Introduce un texto!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        // Mostrar el diàleg
        builder.create().show()
    }




    fun refrescarRecyclerView2(list: MutableList<Pizza>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    // Actualitzar el RecyclerView en el fil principal
                    if (mAdapter == null) {
                        myRecyclerView = findViewById(R.id.recycler_pizzas) as RecyclerView
                        myRecyclerView.setHasFixedSize(true)
                        myRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        mAdapter = pizzasAdapter(list,ivaActual,this@MainActivity)
                        myRecyclerView.adapter = mAdapter
                    } else {
                        mAdapter?.updateItems(list)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Snackbar.make(parentLayout, "Error carregant les dades: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun mostrarPizzas(){
        refrescarRecyclerView()
    }

//    fun ajustarIva(): Int {
//        var iva = 0
//        GlobalScope.launch(Dispatchers.IO) {
//            var iva = 10
//            ivaDao.actualizarIva(1, iva)
//        }
//        return iva
//    }
//
//    fun traerIva() {
//        GlobalScope.launch(Dispatchers.IO) {
//            //traer database iva?
//            val ivaDb = IvaDb.getDatabase(this@MainActivity, CoroutineScope(Dispatchers.IO))
//            ivaDao = ivaDb.ivaDao()
//            // Obtener el IVA en segundo plano
//            CoroutineScope(Dispatchers.IO).launch {
//                val ivaRecuperado = ivaDao.getIva() ?: 0.0
//                withContext(Dispatchers.Main) {
//                    // agregar condicion de si el iva es 0 o darle un valor inicial
//                    if (ivaRecuperado == 0) {
//                        ivaActual = 21
//                    } else {
//                        ivaActual = ajustarIva()
//                    }
//                }
//            }
//        }
//    }
//
//
//    fun intentConfig(){
//        // Registrem el launcher per gestionar resultats
//        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) { // Comprovem que el resultat és correcte
//                val data = result.data
//                dato1 = data?.getStringExtra("FIELD1") ?: ""
//            }
//            else{Log.e("MainActivity", "El IVA no cumple con el formato esperado.")
//                return@registerForActivityResult Snackbar.make(parentLayout, "Error en el iva", Snackbar.LENGTH_SHORT).show()
//
//            }
//        }
//    }
}

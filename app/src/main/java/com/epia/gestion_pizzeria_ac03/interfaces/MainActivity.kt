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
import com.epia.gestion_pizzeria_ac03.room.PizzaDao
import com.epia.gestion_pizzeria_ac03.room.Pizza
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    // Llançador per gestionar el resultat retornat
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultLauncherConfig: ActivityResultLauncher<Intent>
    private lateinit var menu: Menu
    private lateinit var pizzaDao: PizzaDao
    private lateinit var parentLayout: View
    lateinit var myRecyclerView: RecyclerView
    var mAdapter: pizzasAdapter? = null
    var listaPizzas: MutableList<Pizza> = ArrayList()
    var listaFiltros: MutableList<Pizza> = ArrayList()
    private var dato1: String = ""
    private var dato2: String = ""
    private var dato3: String = ""
    private var dato4: Double = 0.0
    private lateinit var ivaDao: IvaDao
    private var ivaActual: Int = 0  // Almacenar IVA




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizzas)
        parentLayout = findViewById<View>(android.R.id.content)
        supportActionBar?.show()
        intentPizzas()
        intentConfig()

        pizzaDao = (applicationContext as App).db.pizzaDao()
        ivaDao = (applicationContext as App).dbIva.ivaDao()

        traerIva()
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
        // para seleccionar un orden u otro
        val itemOrdenar = menu.findItem(R.id.itemOrdenar)
        val itemRefDesc = menu.findItem(R.id.itemRefDesc)
        val itemPi = menu.findItem(R.id.itemPizzas)
        val itemPv = menu.findItem(R.id.itemPizzaVegana)
        val itemTo = menu.findItem(R.id.itemTopping)
        val itemPc = menu.findItem(R.id.itemPizzaCeliaca)




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
                resultLauncherConfig.launch(intent)
                true
            }
            R.id.itemOrdenar -> {

                item.isChecked = !item.isChecked
                itemRefDesc.isChecked = false

                if (item.isChecked &&
                    (itemPi.isChecked || itemPc.isChecked || itemPv.isChecked || itemTo.isChecked)) {

                    Toast.makeText(this, "Orden por referencia activado", Toast.LENGTH_SHORT).show()
                    listaPizzas = ordenarPorReferenciaFiltro(listaPizzas)
                    mostrarPizzasFiltros(listaPizzas)

                }else if (item.isChecked &&
                    (!itemPi.isChecked && !itemPc.isChecked && !itemPv.isChecked && !itemTo.isChecked)) {

                    listaFiltros.clear()
                    ordenarPorReferencia()

                }
                else if (!item.isChecked){
                    Toast.makeText(this, "Orden por referencia desactivado", Toast.LENGTH_SHORT).show()
                    listaFiltros.clear()
                    mostrarPizzas()
                }
                    true
            }
            R.id.itemRefDesc -> {

                item.isChecked = !item.isChecked
                itemOrdenar.isChecked = false

                if (item.isChecked &&
                    (itemPi.isChecked || itemPc.isChecked || itemPv.isChecked || itemTo.isChecked)) {

                    Toast.makeText(this, "Orden por referencia activado", Toast.LENGTH_SHORT).show()
                    listaPizzas = ordenarPorReferenciaDescripcionFiltro(listaPizzas)
                    mostrarPizzasFiltros(listaPizzas)

                }else if (item.isChecked &&
                    (!itemPi.isChecked && !itemPc.isChecked && !itemPv.isChecked && !itemTo.isChecked)) {

                    listaFiltros.clear()
                    ordenarPorReferenciaDescripcion()

                }
                else if (!item.isChecked){
                    Toast.makeText(this, "Orden por referencia desactivado", Toast.LENGTH_SHORT).show()
                    listaFiltros.clear()
                    mostrarPizzas()
                }
                true
            }
            R.id.itemTodos ->{
                deseleccionarTiposPizza(item)
                mostrarPizzas()
//                item.isChecked = !item.isChecked
//                if (item.isChecked){
//                mostrarPizzas()
//                }
//                else{
//                    deseleccionarTiposPizza(item)
//                }
                true
            }
            R.id.itemPizzas->{
                deseleccionarTiposPizza(item)
                mostrarRefPi()
//                item.isChecked = !item.isChecked
//                if (item.isChecked){
//                    Toast.makeText(this, "Referencias: PI", Toast.LENGTH_SHORT).show()
//                    mostrarRefPi()
//                }
//                else{
//                    deseleccionarTiposPizza(item)
//                    mostrarPizzas()
//                }
                true
            }
            R.id.itemPizzaVegana->{
                deseleccionarTiposPizza(item)
                mostrarRefPv()
                true
            }
            R.id.itemPizzaCeliaca->{
                deseleccionarTiposPizza(item)
                mostrarRefPc()
                true
            }
            R.id.itemTopping->{
                deseleccionarTiposPizza(item)
                mostrarRefTo()
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



    fun mostrarPizzas() {
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
                        mAdapter?.updateItems(pizzas,ivaActual)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Snackbar.make(parentLayout, "Error carrgando datos: ${e.message}", Snackbar.LENGTH_SHORT).show()
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

        // Configurar un EditText introducir la bisqueda
        val input = EditText(this)
        input.hint = "Escribe la referencia a buscar"
        builder.setView(input)

        // Configurar botones
        builder.setPositiveButton("Buscar") { _, _ ->
            var searchText = input.text.toString()
            if (searchText.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    mostrarPizzasFiltros(pizzaDao.buscarPizzaReferencia(searchText))
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




    fun mostrarPizzasFiltros(list: MutableList<Pizza>) {
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
                        mAdapter?.updateItems(list,ivaActual)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Snackbar.make(parentLayout, "Error carrgando datos: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

//    fun mostrarPizzas(){
//        mostrarPizzas()
//    }

    fun ordenarPorReferencia(){
        GlobalScope.launch(Dispatchers.IO){
            listaFiltros = pizzaDao.getPizzasPorReferencia()
            withContext(Dispatchers.Main){
                mostrarPizzasFiltros(listaFiltros)
            }
        }
    }

    fun ordenarPorReferenciaFiltro(pizzas: MutableList<Pizza>):MutableList<Pizza>{

        pizzas.sortWith(compareBy(
            { it.referencia.take(2) },
            { it.referencia.drop(2).toIntOrNull() ?: 0 }
        ))

        return pizzas
    }

    fun ordenarPorReferenciaDescripcion(){
        GlobalScope.launch(Dispatchers.IO){
            listaFiltros = pizzaDao.getPizzasReferenciaDecripcion()
            withContext(Dispatchers.Main){
                mostrarPizzasFiltros(listaFiltros)
            }
        }
    }

    fun ordenarPorReferenciaDescripcionFiltro(pizzas: MutableList<Pizza>):MutableList<Pizza>{

        listaPizzas.sortWith(compareBy(
            { it.referencia.take(2) },
            { it.referencia.drop(2).toIntOrNull() ?: 0 },
            { it.descripcion }
        ))

        return pizzas
    }

    fun mostrarRefPi(){
        GlobalScope.launch(Dispatchers.IO){
            listaPizzas = pizzaDao.getPizzasPorReferenciaPi()
            withContext(Dispatchers.Main){
                mostrarPizzasFiltros(listaPizzas)
            }
        }
    }

    fun mostrarRefPv(){
        GlobalScope.launch(Dispatchers.IO){
            listaPizzas = pizzaDao.getPizzasPorReferenciaPv()
            withContext(Dispatchers.Main){
                mostrarPizzasFiltros(listaPizzas)
            }
        }
    }

    fun mostrarRefPc(){
        GlobalScope.launch(Dispatchers.IO){
            listaPizzas = pizzaDao.getPizzasPorReferenciaPc()
            withContext(Dispatchers.Main){
                mostrarPizzasFiltros(listaPizzas)
            }
        }
    }

    fun mostrarRefTo(){
        GlobalScope.launch(Dispatchers.IO){
            listaPizzas = pizzaDao.getPizzasPorReferenciaTo()
            withContext(Dispatchers.Main){
                mostrarPizzasFiltros(listaPizzas)
            }
        }
    }

    // deseleccionar los otros items por el actual
    fun deseleccionarTiposPizza(item: MenuItem){
        val tiposPizza = listOf(
            R.id.itemTodos,
            R.id.itemPizzas,
            R.id.itemPizzaVegana,
            R.id.itemPizzaCeliaca,
            R.id.itemTopping
        )

        val idItem = item.itemId

        tiposPizza.forEach{ menuItemId ->
            menu.findItem(menuItemId)?.isChecked = (menuItemId == idItem)
        }
    }

    fun traerIva() {
        GlobalScope.launch(Dispatchers.IO) {
            var ivaRecuperado = ivaDao.getIva()
            // Si el IVA es 0 se le asinga 21%
            if (ivaRecuperado == 0) {
                ivaActual = 21
                ivaDao.insertPrecioIva(ivaActual)
            } else {
                    ivaActual = ivaRecuperado
            }
        }
    }


    fun intentConfig() {
        // Registramos el launcher para gestionar resultados
        resultLauncherConfig = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                var iva = data?.getStringExtra("IVA") ?: "0"
                ivaActual = iva.toInt()

                GlobalScope.launch(Dispatchers.IO) {
                    ivaDao.updatePrecioIva(ivaActual) // Actualizar IVA en base de datos
                    withContext(Dispatchers.Main) {
                        mostrarPizzas()
                    }
                }
            } else {
                Log.e("MainActivity", "El IVA no cumple con el formato esperado.")
                Snackbar.make(parentLayout, "Error en el iva", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}

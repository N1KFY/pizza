package com.epia.gestion_pizzeria_ac03.interfaces

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epia.gestion_pizzeria_ac03.R
import com.epia.gestion_pizzeria_ac03.adapters.pizzasAdapter
import com.epia.gestion_pizzeria_ac03.room.IvaDb
import com.epia.gestion_pizzeria_ac03.room.Pizza
import com.epia.gestion_pizzeria_ac03.room.PizzaDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class recyclerPizzas : AppCompatActivity()  {

    lateinit var myRecyclerView : RecyclerView
    var mAdapter : pizzasAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizzas)
        setUpRecyclerView()
    }
    
    fun setUpRecyclerView() {
        myRecyclerView = findViewById(R.id.recycler_pizzas) as RecyclerView
        myRecyclerView.setHasFixedSize(true)
        myRecyclerView.layoutManager = LinearLayoutManager(this)

        val db = PizzaDb.getDatabase(
            this,
            scope = TODO()
        )
        val pizzaDao = db.pizzaDao()


        val ivaDb = IvaDb.getDatabase(
            this,
            coroutineScope = TODO()
        ) // Accedemos a la base de datos IVA
        val ivaDao = ivaDb.ivaDao()


        lifecycleScope.launch {
            val pizzas = withContext(Dispatchers.IO){
                pizzaDao.getPizzas()
            }
            val iva = withContext(Dispatchers.IO) {
            }
        }

        mAdapter = pizzasAdapter(pizzaDao.getPizzas(),ivaDao.getIva(),this)
////////////////////////////////////////////
        //mAdapter.updateItems(pizzas)
//////////////////////////////////////////////
        //setUpRecyclerView()
        myRecyclerView.adapter = mAdapter
    }
    

    /*fun setUpRecyclerView() {
        myRecyclerView = findViewById(R.id.recycler_pizzas) as RecyclerView
        myRecyclerView.setHasFixedSize(true)
        myRecyclerView.layoutManager = LinearLayoutManager(this)

        // llamamos al 'constructor' para pasarle los datos
//        mAdapter = countriesAdapter(getPaises(),
//            this,
//            { it: Country -> doCall(it) },
//            // miniretoo
//            { pepe: Country -> doIntercambiar(pepe) }
//        )
        //mAdapter = countriesAdapter(getPaises(),this,{it: Country -> doCall(it)}

        mAdapter = pizzasAdapter(getPizzas(),this)

        myRecyclerView.adapter = mAdapter

    }
    fun getPizzas():MutableList<Pizza>{

        var pizzas: MutableList<Pizza> = pizzaDao.getPizzas()

    }*/
    
    
}

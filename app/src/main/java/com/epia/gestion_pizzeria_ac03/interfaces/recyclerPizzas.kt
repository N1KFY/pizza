package com.epia.gestion_pizzeria_ac03.interfaces

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epia.gestion_pizzeria_ac03.R
import com.epia.gestion_pizzeria_ac03.adapters.pizzasAdapter
import com.epia.gestion_pizzeria_ac03.room.IvaDb

import com.epia.gestion_pizzeria_ac03.room.PizzaDb


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
        ) // Acceso base datos IVA
        val ivaDao = ivaDb.ivaDao()

        

        mAdapter = pizzasAdapter(pizzaDao.getPizzas(),ivaDao.getIva(),this)
////////////////////////////////////////////
        //mAdapter.updateItems(pizzas)
//////////////////////////////////////////////
        //setUpRecyclerView()
        myRecyclerView.adapter = mAdapter
    }

    
    
}

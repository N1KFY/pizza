package com.epia.gestion_pizzeria_ac03.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epia.gestion_pizzeria_ac03.R
import com.epia.gestion_pizzeria_ac03.room.Pizza
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat



class pizzasAdapter
    (
    private var pizzas: MutableList<Pizza>,
    private var iva: Int,  // Agregar el IVA
    private var context: Context
    )
    : RecyclerView.Adapter<pizzasAdapter.ViewHolder>() {

    // sobrecarga obligada
    // coge cada una de las posiciones de la lista y la pasa a la clase
    // viewHolder para que este pinta
    // el '.bind' es un metodo que se encarga de pintar -->  mirar la classe
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pizzas.get(position)
        holder.bind(item, context)
    }
    // sobrecarga obligada
    // encargado de crear la View a través del layout que se haya creado para la ocasión.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(layoutInflater.inflate(R.layout.recycler_card_pizzas, parent, false))
    }
    // sobrecarga obligada
    // número de items que tiene la lista
    override fun getItemCount(): Int {
        return pizzas.size
    }

    fun updateItems(pizzasAMostrar: MutableList<Pizza>, newIva: Int) {
        pizzas.clear() // Limpiar lista
        pizzas.addAll(pizzasAMostrar) // Agrega
        iva = newIva // Actualizar IVA
        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val referencia = view.findViewById<TextView>(R.id.tvReferencia)
        val descripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val tipo = view.findViewById<TextView>(R.id.tvTipo)
        val precio_Sin_Iva = view.findViewById<TextView>(R.id.tvPrecio_sin_Iva)
        val precio_Con_Iva = view.findViewById<TextView>(R.id.tvPrecio_con_iva)

        // cogemos la CARD porque haremos temas de decoración.
        val card = view.findViewById<CardView>(R.id.card)


        fun bind(pizza: Pizza, context: Context) {
            referencia.text = pizza.referencia
            descripcion.text = pizza.descripcion
            tipo.text = pizza.tipo
            precio_Sin_Iva.text = pizza.precioSinIva.toString()
            // para meter iva
            val precioConIva = pizza.precioSinIva * (1 + iva.toDouble() / 100)
            precio_Con_Iva.text = String.format("%.2f", precioConIva) // Mostrar con 2 decimales


            val colorPi = ContextCompat.getColor(context, R.color.color_pi)
            val colorPv = ContextCompat.getColor(context, R.color.color_pv)
            val colorPc = ContextCompat.getColor(context, R.color.color_pc)
            val colorTo = ContextCompat.getColor(context, R.color.color_to)

            // Aplicar el color de fondo según el continente
            if (pizza.referencia.startsWith("PI")) {
                card.setCardBackgroundColor(colorPi)
            } else if (pizza.referencia.startsWith("PV")) {
                card.setCardBackgroundColor(colorPv)
            } else if (pizza.referencia.startsWith("PC")) {
                card.setCardBackgroundColor(colorPc)
            } else {
                card.setCardBackgroundColor(colorTo)
            }
        }

    }
    }

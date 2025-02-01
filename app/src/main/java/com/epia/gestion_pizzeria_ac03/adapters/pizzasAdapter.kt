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
import com.epia.gestion_pizzeria_ac03.room.IvaDao
import com.epia.gestion_pizzeria_ac03.room.PizzaDao


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

    fun updateItems(pizzasAMostrar: MutableList<Pizza>) {
        pizzas.clear() // Limpia la lista actual
        pizzas.addAll(pizzasAMostrar) // Agrega los nuevos items
        notifyDataSetChanged() // Notifica que los datos han cambiado
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val referencia = view.findViewById<TextView>(R.id.tvReferencia)
        val descripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val tipo = view.findViewById<TextView>(R.id.tvTipo)
        val precio_Sin_Iva = view.findViewById<TextView>(R.id.tvPrecio_sin_Iva)
        val precio_Con_Iva = view.findViewById<TextView>(R.id.tvPrecio_con_iva)

//        // cogemos la CARD porque haremos temas de decoración.
//        val card = view.findViewById<CardView>(R.id.card)
//
//        // nos guardamos la view para el Snackbar
//        val countriesView: View = view




        fun bind(pizza: Pizza, context: Context) {
            referencia.text = pizza.referencia
            descripcion.text = pizza.descripcion
            tipo.text = pizza.tipo
            precio_Sin_Iva.text = pizza.precioSinIva.toString()
            // para meter iva
            val precioConIva = pizza.precioSinIva * (1 + iva / 100)
            precio_Con_Iva.text = String.format("%.2f", precioConIva) // Mostrar con 2 decimales



//            if (pais.km2 <= 999999) {
//                km2.text = pais.km2.toString()
//                km2.setTextAppearance(R.style.TextoNormal)
//            }
//            else {
//                km2.text = pais.km2.toString()
//                km2.setTextAppearance(R.style.TextoNegrita)
//            }
//
//            emoji.text = pais.emoji


/////////////////////////////////////////////////////////////////////////////////////////////////
//            // Cambia la imagen según el estado actual de "favorito"
//            if (pais.favoritos) {
//                imvFavoritos.setImageResource(android.R.drawable.btn_star_big_on)
//            } else {
//                imvFavoritos.setImageResource(android.R.drawable.btn_star_big_off)
//            }
//
//            // Maneja el clic para alternar el estado de favorito
//            imvFavoritos.setOnClickListener {
//                pais.favoritos = !pais.favoritos // Alterna el estado
//                // informa agregado a favoritos
//                Snackbar.make(countriesView,
//                    pais.name_es + " Agregado a favoritos",
//                    Snackbar.LENGTH_LONG).show()
//                notifyItemChanged(adapterPosition) // Actualiza la tarjeta en el RecyclerView
//            }
//////////////////////////////////////////////////////////////////////////////////////////////////

//            emoji.setOnClickListener {
//                var nombre = pais.name_es.lowercase()
//                // Reemplazar espacios por guiones bajos
//                nombre = nombre.replace(" ", "_")
//                // Separa los acentos de las vocales -> á = a'
//                var sinAcentos = Normalizer.normalize(nombre, Normalizer.Form.NFD)
//                // elimina todos los acentos
//                sinAcentos = sinAcentos.replace("\\p{M}".toRegex(), "")
//
//                val url = "https://es.wikipedia.org/wiki/" + sinAcentos
//                // Crear un Intent para abrir la URL en el navegador
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                context.startActivity(intent)
//            }




//            // Obtener el color de fondo para la tarjeta según el continente
//            val colorEurope = ContextCompat.getColor(context, R.color.color_europe)
//            val colorAfrica = ContextCompat.getColor(context, R.color.color_africa)
//            val colorOceania = ContextCompat.getColor(context, R.color.color_oceania)
//            val colorAntartida = ContextCompat.getColor(context, R.color.color_antartida)
//            val colorAsia = ContextCompat.getColor(context, R.color.color_asia)
//            val colorAsur = ContextCompat.getColor(context, R.color.color_america_sur)
//            val colorAnorte = ContextCompat.getColor(context, R.color.color_america_norte)

//            // Aplicar el color de fondo según el continente
//            if (pais.continent_en.lowercase() == "europe") {
//                card.setCardBackgroundColor(colorEurope)
//            } else if (pais.continent_en.lowercase() == "africa") {
//                card.setCardBackgroundColor(colorAfrica)
//            } else if (pais.continent_en.lowercase() == "oceania") {
//                card.setCardBackgroundColor(colorOceania)
//            } else if (pais.continent_en.lowercase() == "asia") {
//                card.setCardBackgroundColor(colorAsia)
//            } else if (pais.continent_en.lowercase() == "south america") {
//                card.setCardBackgroundColor(colorAsur)
//            } else if (pais.continent_en.lowercase() == "north america") {
//                card.setCardBackgroundColor(colorAnorte)
//            } else {
//                card.setCardBackgroundColor(colorAntartida)
//            }
        }

    }
    }

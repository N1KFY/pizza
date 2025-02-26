package com.epia.gestion_pizzeria_ac03.interfaces

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.epia.gestion_pizzeria_ac03.R
import com.google.android.material.snackbar.Snackbar

class secondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intent_agregar)


        // Obtener el AutoCompleteTextView (Referencia)
        val autoCompleteRef = findViewById<AutoCompleteTextView>(R.id.editText1)
        // Definir las opciones sugeridas
        val opciones = arrayOf("PI", "PV", "PC", "TO")
        // Crear el adaptador usando un layout sencillo para ítems de la lista
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones)
        // Asignar el adaptador al AutoCompleteTextView
        autoCompleteRef.setAdapter(adapter)

        // Mostrar el dropdown al hacer clic
        autoCompleteRef.setOnClickListener {
            autoCompleteRef.showDropDown() // ¡Forzar que se muestre el dropdown!
        }

        // Configurar AutoCompleteTextView (Tipo)
        val autoCompleteTipo = findViewById<AutoCompleteTextView>(R.id.editText3)
        val opcionesTipo = arrayOf("PIZZA", "PIZZA VEGANA", "PIZZA CELIACA", "TOPPING")
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesTipo)
        autoCompleteTipo.setAdapter(adapterTipo)

        // Mostrar el dropdown al hacer clic ligado con focusable="false"
        autoCompleteTipo.setOnClickListener {
            autoCompleteTipo.showDropDown()
        }

        // El resto de vistas (editText2, editText3, editText4 y buttonSubmit)
        val editText2 = findViewById<EditText>(R.id.editText2)
        //val editText3 = findViewById<EditText>(R.id.editText3)
        val editText4 = findViewById<EditText>(R.id.editText4)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val dato1 = autoCompleteRef.text.toString()
            val dato2 = editText2.text.toString()
            val dato3 = autoCompleteTipo.text.toString()
            val dato4 = editText4.text.toString()

            if (dato1.isNotEmpty() && dato2.isNotEmpty() && dato3.isNotEmpty() && dato4.isNotEmpty()) {

                if (dato1.startsWith("PI") || dato1.startsWith("pi") ||
                    dato1.startsWith("PV") || dato1.startsWith("pv") ||
                    dato1.startsWith("PC") || dato1.startsWith("pc") ||
                    dato1.startsWith("TO") || dato1.startsWith("to")) {

                    if (dato1.length > 5){

                        if (dato3.equals("PIZZA")&& dato1.startsWith("PI") || dato3.equals("PIZZA")&& dato1.startsWith("pi") ||
                            dato3.equals("PIZZA VEGANA")&& dato1.startsWith("PV") || dato3.equals("PIZZA VEGANA")&& dato1.startsWith("pv") ||
                            dato3.equals("PIZZA CELIACA")&& dato1.startsWith("PC") || dato3.equals("PIZZA CELIACA")&& dato1.startsWith("pc") ||
                            dato3.equals("TOPPING")&& dato1.startsWith("TO") || dato3.equals("TOPPING")&& dato1.startsWith("to")) {

                            val resultIntent = Intent().apply {
                                putExtra("Referencia", dato1)
                                putExtra("Descripcion", dato2)
                                putExtra("Tipo", dato3)
                                putExtra("PrecioSinIva", dato4)
                            }
                            setResult(RESULT_OK, resultIntent)
                            finish()

                        }else{
                            //Snackbar.make(findViewById(android.R.id.content), "La referencia no coincide con el tipo ej.: Ref: PI => Tipo: PIZZA", Snackbar.LENGTH_LONG).show()
                            Toast.makeText(this, "Referencia y tipo no coincide ej.: Ref: PI => Tipo: PIZZA", Toast.LENGTH_LONG).show() // nu puedo mostrar un mensaje largo
                        }
                    }else{
                        Toast.makeText(this, "La referencia deb tener 6 caracteres", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "La referencia debe comenzar por: PI, PV, PC, TO.", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Debes rellenar, todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }



    }
}

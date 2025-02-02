package com.epia.gestion_pizzeria_ac03.interfaces

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.epia.gestion_pizzeria_ac03.R

//class secondActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.intent_agregar)
//
//
//        // Obtenim els camps de text i el botó
//        val editText1 = findViewById<EditText>(R.id.editText1)
//        val editText2 = findViewById<EditText>(R.id.editText2)
//        val editText3 = findViewById<EditText>(R.id.editText3)
//        val editText4 = findViewById<EditText>(R.id.editText4)
//        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
//
//        // Acció per enviar les dades de tornada o guardar-les
//        buttonSubmit.setOnClickListener {
//            val dato1 = editText1.text.toString()
//            val dato2 = editText2.text.toString()
//            val dato3 = editText3.text.toString()
//            val dato4 = editText4.text.toString()
//
//            // Validem que els camps no estiguin buits
//            if (dato1.isNotEmpty() && dato2.isNotEmpty() && dato3.isNotEmpty() && dato4.isNotEmpty()) {
//                Toast.makeText(this, "Dades enviades correctament!", Toast.LENGTH_SHORT).show()
//
//                // Si cal, envia les dades de tornada a MainActivity (opcional)
//                val resultIntent = Intent().apply {
//                    putExtra("FIELD1", dato1)
//                    putExtra("FIELD2", dato2)
//                    putExtra("FIELD3", dato3)
//                    putExtra("FIELD4", dato4)
//                }
//                setResult(RESULT_OK, resultIntent)
//                finish() // Tanca aquesta activitat
//            } else {
//                Toast.makeText(this, "Si us plau, omple tots els camps.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}

class secondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intent_agregar)

        // Obtener el AutoCompleteTextView (que ahora tiene el id editText1)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.editText1)

        // Definir las opciones sugeridas
        val opciones = arrayOf("PI", "PV", "PC", "TO")

        // Crear el adaptador usando un layout sencillo para ítems de la lista
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones)

        // Asignar el adaptador al AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter)

        // El resto de vistas (editText2, editText3, editText4 y buttonSubmit)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val editText3 = findViewById<EditText>(R.id.editText3)
        val editText4 = findViewById<EditText>(R.id.editText4)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val dato1 = autoCompleteTextView.text.toString()  // Campo con opciones sugeridas
            val dato2 = editText2.text.toString()
            val dato3 = editText3.text.toString()
            val dato4 = editText4.text.toString()

            if (dato1.isNotEmpty() && dato2.isNotEmpty() && dato3.isNotEmpty() && dato4.isNotEmpty()) {

                if (dato1.startsWith("PI") || dato1.startsWith("PV") ||
                    dato1.startsWith("PC") || dato1.startsWith("TO")) {

                    if (dato1.length > 5){

                        val resultIntent = Intent().apply {
                            putExtra("FIELD1", dato1)
                            putExtra("FIELD2", dato2)
                            putExtra("FIELD3", dato3)
                            putExtra("FIELD4", dato4)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()

                    }else{
                        Toast.makeText(this, "La referencia tener 6 caracteres", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "La referencia debe comenzar por: PI, PV, PC, TO.", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Si us plau, omple tots els camps.", Toast.LENGTH_SHORT).show()
            }
        }



    }
}

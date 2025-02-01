package com.epia.gestion_pizzeria_ac03.interfaces

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epia.gestion_pizzeria_ac03.R

class configActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intent_config)

        // obtener campos iva
        val edTIva = findViewById<EditText>(R.id.edTIva)
        val buttonSubmit = findViewById<Button>(R.id.btnModIva)

        //enviar datos para guardarlos
        buttonSubmit.setOnClickListener{
            val precioIva = edTIva.text.toString()

            if (precioIva.isNotEmpty()){

                val resultIntent = Intent().apply {
                    putExtra("IVA",precioIva)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Has dejado el campo vacio si quieres actualizarlo vuelve a entrar.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
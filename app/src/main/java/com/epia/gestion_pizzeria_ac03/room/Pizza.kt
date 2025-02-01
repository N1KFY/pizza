package com.epia.gestion_pizzeria_ac03.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.w3c.dom.Text

@Entity(tableName = "pizza")
data class Pizza(
    @PrimaryKey var referencia: String,
    var descripcion: String,
    var tipo: String,
    var precioSinIva: Double,
)

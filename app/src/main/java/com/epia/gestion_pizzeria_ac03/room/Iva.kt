package com.epia.gestion_pizzeria_ac03.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "iva")
data class Iva(
    @PrimaryKey val id: Int = 1,  //usamos un ID fijo
    val precioIva: Int
)
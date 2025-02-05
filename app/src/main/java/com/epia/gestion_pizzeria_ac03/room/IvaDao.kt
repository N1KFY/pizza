package com.epia.gestion_pizzeria_ac03.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface IvaDao {
    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT precioIva FROM iva WHERE id = 1")
    fun getIva(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(iva: Iva)

    fun insertPrecioIva(precioIva: Int) {
        // Inserta un valor de IVA, asegurándose de usar un objeto Iva con el ID 1.
        insert(Iva(precioIva = precioIva))
    }

    @Query("UPDATE iva SET precioIva = :precioIva WHERE id = 1")
    fun updatePrecioIva(precioIva: Int)

    @Delete
    fun delete(iva: Iva)

    @Update()
    fun update(iva: Iva)


    @Query("UPDATE iva SET precioIva = :nuevoIva WHERE id = :id")
    fun actualizarIva(id: Int, nuevoIva: Int)
}
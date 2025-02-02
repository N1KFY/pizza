package com.epia.gestion_pizzeria_ac03.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.w3c.dom.Text

@Dao
interface PizzaDao{
    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM pizza")
    fun getPizzas(): MutableList<Pizza>

    @Query("SELECT * FROM pizza WHERE descripcion = :referencia")
    fun getPizza(referencia : String): Pizza

    @Query("SELECT * FROM pizza ORDER BY referencia ASC")
    fun getPizzasPorReferencia(): MutableList<Pizza>

    @Query("SELECT * FROM pizza WHERE descripcion LIKE '%' || :descripcion || '%'")
    fun buscarPizzaReferencia(descripcion: String): MutableList<Pizza>

    @Query("SELECT * FROM pizza ORDER BY descripcion ASC, referencia ASC")
    fun getPizzasReferenciaDecripcion(): MutableList<Pizza>

    @Query("SELECT * FROM pizza WHERE referencia LIKE 'PI%'")
    fun getPizzasPorReferenciaPi(): MutableList<Pizza>

    @Query("SELECT * FROM pizza WHERE referencia LIKE 'PV%'")
    fun getPizzasPorReferenciaPv(): MutableList<Pizza>

    @Query("SELECT * FROM pizza WHERE referencia LIKE 'PC%'")
    fun getPizzasPorReferenciaPc(): MutableList<Pizza>

    @Query("SELECT * FROM pizza WHERE referencia LIKE 'TO%'")
    fun getPizzasPorReferenciaTo(): MutableList<Pizza>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pizza: Pizza)

    @Delete
    fun delete(pizza: Pizza)

    @Update
    fun update(pizza: Pizza)

    @Query("DELETE FROM pizza")
    fun deleteAll()

//    @Query("select coalesce(max(referencia),0) from pizza")
//    fun getLastId(): String
}
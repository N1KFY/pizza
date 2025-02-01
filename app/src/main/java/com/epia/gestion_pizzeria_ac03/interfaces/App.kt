package com.epia.gestion_pizzeria_ac03.interfaces

import android.app.Application
import androidx.room.Room
import com.epia.gestion_pizzeria_ac03.room.IvaDao
import com.epia.gestion_pizzeria_ac03.room.IvaDb
import com.epia.gestion_pizzeria_ac03.room.PizzaDb

class App: Application() {
    lateinit var db: PizzaDb
    lateinit var dbIva: IvaDao
    //lateinit var dbIva: IvaDb  // Canvia de IvaDao a IvaDb

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            PizzaDb::class.java,
            "room-db"
        ).fallbackToDestructiveMigration() .build()
    }
}

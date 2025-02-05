package com.epia.gestion_pizzeria_ac03.interfaces

import android.app.Application
import androidx.room.Room
import com.epia.gestion_pizzeria_ac03.room.IvaDao
import com.epia.gestion_pizzeria_ac03.room.IvaDb
import com.epia.gestion_pizzeria_ac03.room.PizzaDb

class App: Application() {
    lateinit var db: PizzaDb
    lateinit var dbIva: IvaDb

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            PizzaDb::class.java,
            "room-db"
        ).fallbackToDestructiveMigration() .build()

        dbIva = Room.databaseBuilder(
            this,
            IvaDb::class.java,
            "iva-db"
        ).fallbackToDestructiveMigration() .build()
    }
}

package com.epia.gestion_pizzeria_ac03.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Iva::class], version = 1)
abstract class IvaDb : RoomDatabase() {
    abstract fun ivaDao(): IvaDao

    companion object {
        @Volatile
        private var INSTANCE: IvaDb? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): IvaDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IvaDb::class.java,
                    "iva-db"
                )
                    .fallbackToDestructiveMigration()  // Esto elimina la base de datos al actualizar la versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
//package com.epia.gestion_pizzeria_ac03.room
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [Iva::class], version = 1) // Incrementa la versión si cambias el esquema
//abstract class IvaDb : RoomDatabase() {
//    abstract fun ivaDao(): IvaDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: IvaDb? = null
//
//        fun getDatabase(context: Context): IvaDb {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    IvaDb::class.java,
//                    "iva-db"
//                )
//                    .fallbackToDestructiveMigration() // Usar solo en desarrollo
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
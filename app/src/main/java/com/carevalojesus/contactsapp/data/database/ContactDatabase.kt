
package com.carevalojesus.contactsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carevalojesus.contactsapp.data.dao.RestaurantDao
import com.carevalojesus.contactsapp.data.model.Dish
import com.carevalojesus.contactsapp.data.model.Restaurant

// Aquí le decimos a Room que use nuestras dos tablas (entities)
@Database(entities = [Restaurant::class, Dish::class], version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {

    // Cambiamos el DAO de contactos por nuestro RestaurantDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "restaurant_database" // Nuevo nombre para el archivo de la BD
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
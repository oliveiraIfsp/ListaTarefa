package br.edu.ifsp.listatarefa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ListaEntity::class], version = 1)
abstract class ListaDatabase: RoomDatabase() {
    abstract fun listaDAO(): ListaDAO

    companion object {
        @Volatile
        private var INSTANCE: ListaDatabase? = null

        fun getDatabase(context: Context): ListaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListaDatabase::class.java,
                    "listatarefa.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

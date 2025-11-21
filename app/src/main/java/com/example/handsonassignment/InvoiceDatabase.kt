package com.example.handsonassignment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Invoice::class], version = 1)
abstract  class InvoiceDatabase: RoomDatabase() {

    abstract fun InvoiceDao(): InvoiceDao

    companion object{
        @Volatile
        private var INSTANCE: InvoiceDatabase? =null

        fun getInstance(context: Context): InvoiceDatabase{
            if (INSTANCE == null)
            {
                synchronized(InvoiceDatabase::class)
                {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        InvoiceDatabase::class.java,
                        "invoice_db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
package com.example.handsonassignment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InvoiceDao {

    @Insert
    suspend fun insertInvoice(invoice: Invoice)

    @Query("SELECT * FROM invoices")
    suspend fun getAllInvoices(): List<Invoice>

    @Query("SELECT * FROM invoices WHERE date = :selectedDate")
    suspend fun getInvoiceByDate(selectedDate: String): List<Invoice>

    @Query("SELECT SUM(amount) FROM invoices WHERE date = :selectedDate")
    suspend fun getSumTotalByDate(selectedDate: String?): Double?

}
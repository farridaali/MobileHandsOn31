package com.example.handsonassignment

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var db: InvoiceDatabase
    private lateinit var expense: EditText
    private lateinit var amount: EditText
    private lateinit var add: Button
    private lateinit var filter: Button
    private lateinit var showAll: Button
    private lateinit var calender: CalendarView
    private lateinit var list: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = InvoiceDatabase.getInstance(this)
        expense = findViewById<EditText>(R.id.expense)
        amount = findViewById<EditText>(R.id.amountID)
        add = findViewById<Button>(R.id.addID)
        filter = findViewById<Button>(R.id.FilterID)
        showAll = findViewById<Button>(R.id.ShowID)
        calender = findViewById<CalendarView>(R.id.CalenderID)
        list = findViewById<ListView>(R.id.ListID)


        var selectedDate = ""
        calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }
        add.setOnClickListener {
            val name = expense.text.toString().trim()
            val amt = (amount.text.toString().trim()).toDouble()
            val cal=calender.toString()

            if (name.isNotEmpty())
            {
                lifecycleScope.launch(Dispatchers.IO) {
                    db.InvoiceDao().insertInvoice(Invoice(  name = name, amount = amt,date=selectedDate))
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(this@MainActivity, "Invoice Added!", Toast.LENGTH_SHORT).show()
                        expense.text.clear()
                        amount.text.clear()
                        loadInvoice()
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Expense cannot be empty!",
                    Toast.LENGTH_SHORT).show()
            }
        }

        filter.setOnClickListener {
            if (selectedDate.isNotEmpty())
            {
                lifecycleScope.launch(Dispatchers.IO) {
                    db.InvoiceDao().getInvoiceByDate(selectedDate=selectedDate)
                    withContext(Dispatchers.Main)
                    {
                        loadInvoiceByDate(selectedDate)
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Select Date!",
                    Toast.LENGTH_SHORT).show()
            }
        }
        showAll.setOnClickListener {


            lifecycleScope.launch(Dispatchers.IO) {
                db.InvoiceDao().getAllInvoices()
                withContext(Dispatchers.Main)
                {
                    loadInvoice()
                }
            }
        }


    }

    private fun loadInvoiceByDate( selectedDate: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            var Invoices = db.InvoiceDao().getInvoiceByDate(selectedDate)
            withContext(Dispatchers.Main)
            {
                val adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    Invoices.map { invoice -> //iterator
                        "Name: ${invoice.name}, Amount: ${invoice.amount}, Date: ${invoice.date}"}
                )
                list.adapter = adapter

                list.setOnItemClickListener { _, _, position, _ ->
                    val selectedInvoice = Invoices[position]
                    val  intent = Intent(this@MainActivity, TotalAmount::class.java)
                    intent.putExtra("date", selectedInvoice.date)
                    startActivity(intent)
                }
            }
        }
    }


    private fun loadInvoice(){
        lifecycleScope.launch(Dispatchers.IO) {
            var Invoices = db.InvoiceDao().getAllInvoices().toMutableList()
            withContext(Dispatchers.Main)
            {
                val adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    Invoices.map { invoice -> //iterator
                        "Name: ${invoice.name}, Amount: ${invoice.amount}, Date: ${invoice.date}"}
                )
                list.adapter = adapter


                list.setOnItemClickListener { _, _, position, _ ->
                    val selectedInvoice = Invoices[position]
                    val  intent = Intent(this@MainActivity, TotalAmount::class.java)
                    intent.putExtra("date", selectedInvoice.date)
                    startActivity(intent)
                }

            }
        }
    }
}





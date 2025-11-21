package com.example.handsonassignment

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TotalAmount : AppCompatActivity() {

    private lateinit var textView: TextView


    private lateinit var db: InvoiceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_total_amount)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById<TextView>(R.id.textView)

        db = InvoiceDatabase.getInstance(this)

        val date = intent.getStringExtra("date")


        lifecycleScope.launch(Dispatchers.IO) {
           val amount= db.InvoiceDao().getSumTotalByDate(date)?.toDouble()
            withContext(Dispatchers.Main)
            {
                textView.text = "Total spent on $date is $amount"
            }
        }



    }
}
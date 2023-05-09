package com.example.proyejemplo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import com.google.firebase.database.FirebaseDatabase

class CalendarioActivity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        val layautVertical : LinearLayout = findViewById(R.id.layaut1)
        val ref = db.getReference("Citas")



    }
}
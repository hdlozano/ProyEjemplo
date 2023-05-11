package com.example.proyejemplo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.util.*

class DefCalendarPsico : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_def_calendar_psico)
        val bundle = intent.extras
        val user: TextView = findViewById(R.id.user)
        if (bundle!=null){
            user.setText("Bienvenido ${bundle.getString("user")}")
        }
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        val btnInicio : Button = findViewById(R.id.Inicio)
        val btnLogout : Button = findViewById(R.id.logout)
        val btnNotice : Button = findViewById(R.id.Noticias)
        btnInicio.backgroundTintList=colorStateList
        btnLogout.backgroundTintList=colorStateList
        btnNotice.backgroundTintList=colorStateList

        val btnCita : ImageView = findViewById(R.id.citas)
        btnCita.setOnClickListener(){
            val intentDispoCitas = Intent(this, DispoCitas::class.java)
            if (bundle != null) {
                intentDispoCitas.putExtras(bundle)
            }
            startActivity(intentDispoCitas)
        }




    }
    private fun showMesageSuccessfu() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage("Usuario ingreso")
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
}
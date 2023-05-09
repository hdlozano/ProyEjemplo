package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val user: TextView = findViewById(R.id.user)
        if (bundle != null) {
            user.setText("Bienvenido ${bundle.getString("user")}")
        }
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        val btnPsicology: ImageButton = findViewById(R.id.psycology)
        val btnInicio : Button = findViewById(R.id.Inicio)
        val btnLogout : Button = findViewById(R.id.logout)
        val btnNotice : Button = findViewById(R.id.Noticias)
        btnInicio.backgroundTintList=colorStateList
        btnLogout.backgroundTintList=colorStateList
        btnNotice.backgroundTintList=colorStateList


        btnLogout.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            val intentMain = Intent(this,MainActivity::class.java)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMain)
        }

        btnInicio.setOnClickListener(){

        }
        btnPsicology.setOnClickListener(){
            psycologyProces(bundle)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun psycologyProces(bundle: Bundle?) {
    val intentPsicology = Intent(this, Psicology::class.java)
        if (bundle != null) {
            intentPsicology.putExtras(bundle)
        }
        startActivity(intentPsicology)
    }
}




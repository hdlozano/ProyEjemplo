package com.example.proyejemplo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Registro : AppCompatActivity() {
    val db = FirebaseDatabase.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val nombre : EditText = findViewById(R.id.nombre)
        val email : EditText = findViewById(R.id.email)
        val contrasena : EditText = findViewById(R.id.contrasena)
        val profesion = findViewById<Spinner>(R.id.profesion)
        val profetions = arrayOf("Docente","Estudiante","Psicologo")
        val registro = findViewById<Button>(R.id.Registrar)
        var prof = ""
        profesion.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,profetions)
        profesion.setOnClickListener(){
            prof = profesion.selectedItem.toString()
        }
        registro.setOnClickListener(){
            registrarUser(nombre, email,contrasena,prof?:"estudiante")
        }



    }



    fun registrarUser(nombre: EditText, email: EditText, contrasena: EditText, prof: String) {

        if(email.text.isNotEmpty()&& contrasena.text.isNotEmpty()) {
            if (email.text.contains("@jdc.edu.co")){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email.text.toString(),
                    contrasena.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        db.getReference("Usuario")

                    } else {
                        msn = "Se ha producido un error al intentar crear el usuario"
                        showMesageFail(msn)
                    }
                }
            }else{
                msn = "El correo con el que se puede registrar es unicamente de la JDC"
                showMesageFail(msn)
            }

        }else{
            msn="Debe digitar todos los campos"
            showMesageFail(msn)
        }
    }
}



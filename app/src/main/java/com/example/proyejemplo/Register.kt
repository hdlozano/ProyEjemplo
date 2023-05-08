package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.example.proyejemplo.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var prof = ""
    val db = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nombre : EditText = findViewById(R.id.nombre)
        val identi : EditText= findViewById(R.id.cc)
        val email : EditText = findViewById(R.id.email)
        val contrasena : EditText = findViewById(R.id.contrasena)
        val profesion : Spinner = findViewById(R.id.profesion)
        val profetions = resources.getStringArray(R.array.profetions)
        //val profetions = arrayOf("Estudiante","Docente","Psicologo")
        val registro = findViewById<Button>(R.id.Registrar)


        profesion.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,profetions)
        profesion.onItemSelectedListener = this
        registro.setOnClickListener(){
            registrarUser(nombre,identi, email,contrasena,prof)
        }



    }



    fun registrarUser(nombre: EditText, identi: EditText , email: EditText, contrasena: EditText, prof: String) {
        var msn = ""
        if(email.text.isNotEmpty()&& contrasena.text.isNotEmpty()&& nombre.text.isNotEmpty() && identi.text.isNotEmpty()) {
            if (email.text.contains("@jdc.edu.co")){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email.text.toString(),
                    contrasena.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val usuario = Usuario(nombre.text.toString(),identi.text.toString().toInt(),email.text.toString(),contrasena.text.toString(), prof)
                        val ref = db.getReference("Usuario")
                        ref.child(usuario.id.toString()).setValue(usuario)
                        msn="Usuario Creado con exito"
                        showMesageConfirm(msn);
                        val intentMain = Intent(this, MainActivity::class.java)
                        startActivity(intentMain)
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

    private fun showMesageConfirm(msn: String) {
        Toast.makeText(this, msn, Toast.LENGTH_LONG).show()
    }

    private fun showMesageFail(msn: String) {
        Toast.makeText(this, msn, Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(adp: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        prof = adp?.getItemAtPosition(p2).toString()
        //Toast.makeText(this, prof1, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this, prof, Toast.LENGTH_SHORT).show()
    }
}



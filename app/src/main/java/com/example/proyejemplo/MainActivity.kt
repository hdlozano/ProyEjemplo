package com.example.proyejemplo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    var msn =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistrar: Button=findViewById(R.id.Registrar)
        val btnIngresar : Button = findViewById(R.id.ingresar)
        val email : EditText=findViewById(R.id.email)
        val password : EditText = findViewById(R.id.password)
        val bundle = Bundle();
        bundle.putString("email", email.toString())
        bundle.putString("password",password.toString())

        btnRegistrar.setOnClickListener(){
            registro(bundle, email, password);
        }
        btnIngresar.setOnClickListener(){
            ingresar(bundle,email, password)
        }

    }

    private fun ingresar(bundle: Bundle, email: EditText, password: EditText) {

        if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
            if (email.text.contains("@jdc.edu.co")) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intentHome = Intent(this, HomeActivity::class.java)

                        var user = email.text.toString()
                        val indice = user.indexOf('@')
                        user = user.substring(0, indice) // "usuario"
                        bundle.putString("user",user)
                        intentHome.putExtras(bundle)
                        startActivity(intentHome)
                    } else {
                        msn = "Se ha producido un error al intentar loguear el usuario"
                        showMesageFail(msn)
                    }
                }
            } else {
                msn = "El correo con el que se puede Ingresar es unicamente de la JDC"
                showMesageFail(msn)
            }

        }else{
            msn="Debe digitar todos los campos"
            showMesageFail(msn)
        }
    }

    private fun registro(bundle: Bundle, email: EditText, password: EditText) {
        if(email.text.isNotEmpty()&& password.text.isNotEmpty()) {
            if (email.text.contains("@jdc.edu.co")){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    showMesageSuccessfu()
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

    private fun showMesageFail(msn: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msn)
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMesageSuccessfu() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage("Usuario registrado con exito")
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

}
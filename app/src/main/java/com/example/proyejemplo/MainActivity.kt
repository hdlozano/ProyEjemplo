package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.proyejemplo.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var msn =""
    val db = FirebaseDatabase.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        val btnRegistrar: Button=findViewById(R.id.Registrar)
        val btnIngresar : Button = findViewById(R.id.ingresar)
        btnIngresar.backgroundTintList=colorStateList
        btnRegistrar.backgroundTintList = colorStateList
        val email : EditText=findViewById(R.id.email)
        val password : EditText = findViewById(R.id.password)
        val bundle = Bundle()
        bundle.putString("email", email.toString())
        bundle.putString("password",password.toString())

        btnRegistrar.setOnClickListener(){
            registUser();
        }

        btnIngresar.setOnClickListener(){
            ingresar(bundle,email, password)
        }

    }



    private fun ingresar(bundle: Bundle, email: EditText, password: EditText) {
        var user1 : Usuario
        if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
            if (email.text.contains("@jdc.edu.co")) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //showMesageSuccessfu();
                        val referenciaUsuarios = FirebaseDatabase.getInstance().getReference("Usuario")
                        val consultaUsuario = referenciaUsuarios.orderByChild("email").equalTo(email.text.toString())

                        consultaUsuario.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // El usuario con el correo especificado existe en la base de datos
                                    // Recupera los datos del usuario
                                    for (usuarioSnapshot in snapshot.children) {
                                        user1 = usuarioSnapshot.getValue(Usuario::class.java)!!
                                        verifiRol(user1,bundle)
                                    }
                                } else {
                                    // El usuario con el correo especificado no existe en la base de datos
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Ocurrió un error al obtener el usuario
                            }
                        })

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
    private fun verifiRol(user1: Usuario, bundle: Bundle) {

        if (user1.profesion.equals("Psicólogo")) {
            val intentDefCalendarioActivity = Intent(this,DefCalendarPsico::class.java)
            bundle.putString("nombre",user1.nombre)
            bundle.putString("identi",user1.id.toString())
            bundle.putString("email",user1.email)
            intentDefCalendarioActivity.putExtras(bundle)
            startActivity(intentDefCalendarioActivity)


        } else {
            //Toast.makeText(this, user1.nombre, Toast.LENGTH_LONG).show()
            val intentHome = Intent(this, HomeActivity::class.java)
            //intentHome.putExtra("User",user1)
            var user = user1.email.toString()
            val indice = user.indexOf('@')
            user = user.substring(0, indice) // "usuario"
            bundle.putString("user", user)
            intentHome.putExtras(bundle)
            startActivity(intentHome)
        }
    }
    private fun registUser() {
        val intentRegistroU = Intent(this, Register::class.java)
        startActivity(intentRegistroU)
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
        builder.setMessage("Usuario ingreso")
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
}

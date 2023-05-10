package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.proyejemplo.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CalendarioActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val db = FirebaseDatabase.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        val bundle = intent.extras
        val layautVertical : LinearLayout = findViewById(R.id.layaut1)
        val refCitas = db.getReference("Citas")
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        val btnInicio : Button = findViewById(R.id.Inicio)
        val btnLogout : Button = findViewById(R.id.logout)
        val btnNotice : Button = findViewById(R.id.Noticias)
        btnInicio.backgroundTintList=colorStateList
        btnLogout.backgroundTintList=colorStateList
        btnNotice.backgroundTintList=colorStateList

        val spinerPsicologos : Spinner = findViewById(R.id.spinner)

        val database = Firebase.database.reference
        val query = database.child("Usuario").orderByChild("profesion").equalTo("Psicólogo")
        val psicologos = ArrayList<Usuario>()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Aquí obtienes una lista de todos los usuarios que tienen "Psicólogo" como su profesión
                for (data in snapshot.children) {
                    val psicologo = data.getValue(Usuario::class.java)
                    if (psicologo != null) {

                        psicologos.add(psicologo)
                    }
                }
                cargarPsicologos(psicologos,spinerPsicologos)
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja el error aquí
            }
        })


        spinerPsicologos.onItemSelectedListener = this


    }


    private fun showMesageSuccessfu(msn: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage(msn)
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    fun cargarPsicologos(psicologos: ArrayList<Usuario>, spinerPsicologos: Spinner) {
        val nombresPsicologos = psicologos.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresPsicologos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinerPsicologos.adapter= adapter
    }
}





package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.proyejemplo.model.Cita
import com.example.proyejemplo.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CalendarioActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val db = FirebaseDatabase.getInstance()
    var idUser : Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        val bundle = intent.extras
        val user: TextView = findViewById(R.id.user)
        if (bundle != null) {
            user.setText("Bienvenido ${bundle.getString("user")}")
            idUser = bundle.getString("identi").toString().toInt()
        }
        val layautVertical : LinearLayout = findViewById(R.id.listaCitas)
        val scrollView : ScrollView = findViewById(R.id.listadoCitas)
        val refCitas = db.getReference("Citas")
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
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
            if (bundle != null) {
                intentMain.putExtras(bundle)
            }
            startActivity(intentMain)
        }
        btnInicio.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            val intentMain = Intent(this,HomeActivity::class.java)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (bundle != null) {
                intentMain.putExtras(bundle)
            }
            startActivity(intentMain)
        }
        btnNotice.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val url = "https://www.jdc.edu.co/bienestar/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            startActivity(intent)
        }

        val spinerPsicologos : Spinner = findViewById(R.id.spinner)

        val database = Firebase.database.reference
        var query = database.child("Usuario").orderByChild("profesion").equalTo("Psicólogo")
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

        val citas = ArrayList<Cita>()
        val reference = FirebaseDatabase.getInstance()
        query = reference.reference.child("Citas").orderByChild("disponible")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Aquí obtienes una lista de todos los usuarios que tienen "Psicólogo" como su profesión
                for (data in snapshot.children) {
                    val cita = data.getValue(Cita::class.java)
                    if (cita != null) {
                        if (cita.disponible){
                            citas.add(cita)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja el error aquí
            }
        })


        spinerPsicologos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Manejar el evento de cambio de selección aquí
                val selectedItem = parent.getItemAtPosition(position).toString()
                val idpsicologo = psicologos.filter { it.nombre.equals(selectedItem) }
                val citasFiltradas = citas.filter { it.idPsicologo.equals(idpsicologo.get(0).id.toString()) }

                showCitas(citasFiltradas as MutableList<Cita>,scrollView,layautVertical, bundle)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada si no hay elementos seleccionados
            }
        }


    }


    private fun showCitas(
        citas: MutableList<Cita>,
        scrollView: ScrollView,
        layout: LinearLayout,
        bundle: Bundle?
    ) {
            layout.removeAllViews()

            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
            var index =0;
            for (cita: Cita in citas){

                val cardView = CardView(this)
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL

                val textView = TextView(this)
                textView.text= "# ${cita.id} \n Diponible: ${cita.fechaHora}"
                textView.textSize = 20F
                textView.setTextColor(ContextCompat.getColor(this, R.color.white))
                linearLayout.addView(textView)
                val iconX = ImageButton(this)
                iconX.setImageResource(R.drawable.cheque_32)
                iconX.backgroundTintList = colorStateList
                iconX.id = cita.id.toString().toInt()
                iconX.setOnClickListener(){
                    val indexid = it.id
                    registrarCita(indexid, citas, scrollView,layout, bundle);
                }

                linearLayout.addView(iconX)
                val layoutParams1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.layoutParams = layoutParams1
                iconX.layoutParams = layoutParams1

                // Establecer layout_weight del TextView mayor que el del ImageButton
                textView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )

                linearLayout.gravity = Gravity.FILL_HORIZONTAL

                cardView.addView(linearLayout)
                cardView.backgroundTintList = colorStateList
                cardView.radius= 20F
                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(10,10,10,10)
                cardView.layoutParams = layoutParams
                layout.addView(cardView)
                index++
            }
        }

    private fun registrarCita(
        indexid: Int,
        citas: MutableList<Cita>,
        scrollView: ScrollView,
        layout: LinearLayout,
        bundle: Bundle?
    ) {
        var citaUpdate = Cita()
        for (cita : Cita in citas){
            if(cita.id.equals(indexid.toString())){
                citaUpdate= cita
            }
        }
        citaUpdate.setDisponibleFlag(false)
        citaUpdate.setIdEstudianteCita(idUser.toString())
        val refCitas = db.getReference("Citas")
        refCitas.child(citaUpdate.id).setValue(citaUpdate)
        Toast.makeText(this,"Cita registrada con exito",Toast.LENGTH_LONG).show()
        recreate()
        val intentPsicology = Intent(this, Psicology::class.java)
        if (bundle != null) {
            intentPsicology.putExtras(bundle)
        }
        startActivity(intentPsicology)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    private fun showMesageSuccessfu(msn: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage(msn)
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
    fun cargarPsicologos(psicologos: ArrayList<Usuario>, spinerPsicologos: Spinner) {
        val nombresPsicologos = psicologos.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresPsicologos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinerPsicologos.adapter= adapter
    }
}
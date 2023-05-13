package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
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
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CitasProg : AppCompatActivity() {
    var idUser = 0
    var userS = Usuario()
    var usuarios1 = ArrayList<Usuario>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas_prog)

        val bundle = intent.extras
        val user: TextView = findViewById(R.id.user)
        if (bundle != null) {
            user.setText("Bienvenido\n ${bundle.getString("nombre")}")
            userS = Usuario(bundle.getString("nombre")?:"",bundle.getString("identi").toString().toInt(),bundle.getString("email")?:"",bundle.getString("contrseña")?:"",bundle.getString("profesion")?:"")
            idUser = bundle.getString("identi").toString().toInt()
        }
        val layautVertical : LinearLayout = findViewById(R.id.listaCitas)
        val scrollView : ScrollView = findViewById(R.id.listadoCitas)
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

        val usuarios = ArrayList<Usuario>()
        var query = Firebase.database.reference.child("Usuario")
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val usuario = data.getValue(Usuario::class.java)
                    if(usuario!=null){
                        usuarios.add(usuario)
                    }
                }
                carcarCitas(usuarios, layautVertical)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //showCitas(citas as MutableLis,scrollView,layautVertical, citas, usuarios)
    }

    private fun carcarCitas(usuarios: ArrayList<Usuario>, layautVertical: LinearLayout) {
        val citas = ArrayList<Cita>()
        val query = Firebase.database.reference.child("Citas")
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val cita = data.getValue(Cita::class.java)
                    if(cita!=null){
                        citas.add(cita)
                    }
                }
                showCitas(citas,layautVertical,usuarios)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun guardarusuarios(usuarios: ArrayList<Usuario>) {
        //usuarios1 =usuarios
        for (usuario: Usuario in usuarios){
            usuarios1.add(usuario)
        }
        //showMesageSuccessfu("numero de Usuarios ${usuarios1.size}")
    }

    private fun showCitas(
        citas: MutableList<Cita>,
        layout: LinearLayout,
        usuarios: ArrayList<Usuario>
    ) {
        //showMesageSuccessfu("Numero de citas ${citas.size}")
        //showMesageSuccessfu("Numero de usuarios ${usuarios.size}")
        var citasFiltradas =ArrayList<Cita>()
        if (userS.profesion.equals("Psicólogo")){
            citasFiltradas = citas.filter { it.idPsicologo.equals(userS.id.toString()) && !it.disponible} as ArrayList<Cita>
        }else{
            citasFiltradas = citas.filter { it.idEstudiante.equals(userS.id.toString())} as ArrayList<Cita>
        }
        layout.removeAllViews()
        //showMesageSuccessfu("Numero de citas ${citasFiltradas.size}")
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        var index =0;
        for (cita: Cita in citasFiltradas){

            val cardView = CardView(this)
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val textView = TextView(this)

            if(userS.profesion.equals("Psicólogo")){
                val nombreUsuario = usuarios.filter { it.id == cita.idEstudiante.toString().toInt() }

                textView.text= "# ${cita.id} \n Diponible: ${cita.fechaHora} \n Con el estudiante:\n ${nombreUsuario.get(0).nombre}"
            }else{
                val nombreUsuario = usuarios.filter { it.id == cita.idPsicologo.toString().toInt() }
                textView.text= "# ${cita.id} \n Programada: ${cita.fechaHora} \n Con el doctor:\n ${nombreUsuario.get(0).nombre}"
            }

            textView.textSize = 20F
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
            linearLayout.addView(textView)
            val iconX = ImageButton(this)
            iconX.setImageResource(R.drawable.cheque_32)
            iconX.backgroundTintList = colorStateList
            iconX.id = cita.id.toString().toInt()
            iconX.setOnClickListener(){
                val indexid = it.id
               //registrarCita(indexid, citas, scrollView,layout);
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

    private fun showMesageSuccessfu(msn: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage(msn)
        builder.setPositiveButton("Aceptar",null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
}
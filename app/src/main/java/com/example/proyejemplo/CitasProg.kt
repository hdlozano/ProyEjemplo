package com.example.proyejemplo

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.proyejemplo.model.Cita
import com.example.proyejemplo.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CitasProg : AppCompatActivity() {
    var idUser = 0
    var userS = Usuario()

    //var citas = ArrayList<Cita>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas_prog)

        val bundle = intent.extras
        val user: TextView = findViewById(R.id.user)
        if (bundle != null) {
            user.setText("Bienvenido ${bundle.getString("user")}")
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

        val usuarios = ArrayList<Usuario>()
        val database = Firebase.database.reference
        var query = database.child("Usuario").orderByChild("profesion")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val usuario = data.getValue(Usuario::class.java)
                    if (usuario != null) {
                        usuarios.add(usuario)
                    }
                }
                //showMesageSuccessfu("numero de usuario ${usuarios.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja el error aquí
            }
        })

        showMesageSuccessfu("numero de Usuarios ${usuarios.size}")

        val citas = ArrayList<Cita>()
        val reference = FirebaseDatabase.getInstance()
        query = reference.reference.child("Citas").orderByChild("disponible")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Aquí obtienes una lista de todos los usuarios que tienen "Psicólogo" como su profesión
                for (data in snapshot.children) {
                    val cita = data.getValue(Cita::class.java)
                    if (cita != null) {
                            citas.add(cita)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja el error aquí
            }
        })
        showMesageSuccessfu("numero de citas ${citas.size}")
        var citasFiltradas =ArrayList<Cita>()
        if (userS.profesion.equals("Psicólogo")){
            citasFiltradas = citas.filter { it.idPsicologo.equals(userS.id) && it.disponible.equals("false")} as ArrayList<Cita>
        }else{
            citasFiltradas = citas.filter { it.idEstudiante.equals(userS.id) && it.disponible.equals("false")} as ArrayList<Cita>
        }
        showCitas(citas as MutableList<Cita>,scrollView,layautVertical, citas, usuarios)
    }

    private fun showCitas(
        citas: MutableList<Cita>,
        scrollView: ScrollView,
        layout: LinearLayout,
        citas1: ArrayList<Cita>,
        usuarios: ArrayList<Usuario>
    ) {
        layout.removeAllViews()

        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        var index =0;
        for (cita: Cita in citas){

            val cardView = CardView(this)
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val textView = TextView(this)
            var nombreUsuario = usuarios.filter { it.id.equals(cita.idPsicologo) }
            if(userS.profesion.equals("Psicólogo")){
                nombreUsuario = usuarios.filter { it.id.equals(cita.idEstudiante) }

                textView.text= "# ${cita.id} \n Diponible: ${cita.fechaHora} \n con el estudiante ${nombreUsuario.get(0).nombre}"
            }else{
                nombreUsuario = usuarios.filter { it.id.equals(cita.idPsicologo) }
                textView.text= "# ${cita.id} \n Programada: ${cita.fechaHora} \n Con el doctor ${nombreUsuario.get(0).nombre}"
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
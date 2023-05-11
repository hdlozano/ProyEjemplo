package com.example.proyejemplo

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.example.proyejemplo.model.Cita
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DispoCitas : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispo_citas)
        val user : TextView = findViewById(R.id.user)

        var idCita = 1
        val database = Firebase.database.reference
        val citasRef = database.child("Citas")
        citasRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (citaSnapshot in dataSnapshot.children) {
                    idCita = citaSnapshot.key.toString().toInt()+1
                }
                showMesageSuccessfu("bd id: $idCita")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error
            }
        })
        //Toast.makeText(this,"en esta val la bd $idCita",Toast.LENGTH_LONG)
        val bundle = intent.extras
        if (bundle!=null){
            user.text = bundle.getString("nombre")
        }
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        val btnInicio : Button = findViewById(R.id.Inicio)
        val btnLogout : Button = findViewById(R.id.logout)
        val btnNotice : Button = findViewById(R.id.Noticias)
        btnInicio.backgroundTintList=colorStateList
        btnLogout.backgroundTintList=colorStateList
        btnNotice.backgroundTintList=colorStateList
        val citas = mutableListOf<Cita>()
        val btnMas : ImageButton = findViewById(R.id.masDispo)
        val btnSave : ImageButton = findViewById(R.id.salvar)
        val scrollView :ScrollView = findViewById(R.id.listaCitas)
        val layout : LinearLayout = findViewById(R.id.layautCitas)

        btnMas.setOnClickListener(){
            showDateTimePickerDialog(this){date ->

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val dateTimeString = dateFormat.format(date.time)
                val cita = Cita(idCita.toString(),bundle?.getString("identi").toString(),dateTimeString.toString(),true,"")
                citas.add(cita)
                showCitas(citas,scrollView,layout)
                idCita++
                Toast.makeText(this,"Cita disponible",Toast.LENGTH_LONG).show()
            }
        }
        btnSave.setOnClickListener(){
            saveCitas(citas, idCita)

        }



    }

    private fun saveCitas(citas: MutableList<Cita>, idCita: Int) {

        val db = FirebaseDatabase.getInstance()
        val refCitas = db.getReference("Citas")
        for (cita : Cita in citas){
            refCitas.child(cita.id).setValue(cita)
        }
        Toast.makeText(this,"Diponibilidad guardada",Toast.LENGTH_SHORT).show()
    }

    private fun showCitas(citas: MutableList<Cita>, scrollView: ScrollView, layout: LinearLayout) {
        layout.removeAllViews()
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_500))
        var index =0;
        for (cita: Cita in citas){
            val cardView = CardView(this)
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val textView = TextView(this)
            textView.text= "Diponible: ${cita.fechaHora}"
            textView.textSize = 20F
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
            linearLayout.addView(textView)
            val iconX = ImageButton(this)
            iconX.setImageResource(R.drawable.botonx_32)
            iconX.backgroundTintList = colorStateList
            iconX.id = index
            iconX.setOnClickListener(){
                val indexid = it.id
                eliminarDiponibilidad(indexid, citas, scrollView,layout);
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

    private fun eliminarDiponibilidad(
        index: Int,
        citas: MutableList<Cita>,
        scrollView: ScrollView,
        layout: LinearLayout
    ) {
        citas.removeAt(index)
        showCitas(citas,scrollView,layout)

    }


    fun showDateTimePickerDialog(context: Context, onDateTimeSelected: (calendar: Calendar) -> Unit) {
        val currentDateTime = Calendar.getInstance()

        // DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                currentDateTime.set(Calendar.YEAR, year)
                currentDateTime.set(Calendar.MONTH, month)
                currentDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // TimePickerDialog
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        currentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        currentDateTime.set(Calendar.MINUTE, minute)

                        // Call onDateTimeSelected function with selected date and time
                        onDateTimeSelected(currentDateTime)
                    },
                    currentDateTime.get(Calendar.HOUR_OF_DAY),
                    currentDateTime.get(Calendar.MINUTE),
                    true
                )

                timePickerDialog.show()
            },
            currentDateTime.get(Calendar.YEAR),
            currentDateTime.get(Calendar.MONTH),
            currentDateTime.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
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
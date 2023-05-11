package com.example.proyejemplo.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Cita(
    var id: String,
    val idPsicologo: String,
    val fechaHora: String,
    var disponible: Boolean,
    var idEstudiante: String
){
    constructor() : this("", "", "", true, "")
    fun seId(id: String){
        this.id = id
    }
    fun setDisponibleFlag(disponible: Boolean){
        this.disponible = disponible
    }
    fun setIdEstudianteCita(idEstudiante: String){
        this.idEstudiante = idEstudiante
    }

}



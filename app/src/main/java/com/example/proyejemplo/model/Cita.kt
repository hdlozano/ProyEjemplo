package com.example.proyejemplo.model

import java.util.Date

data class Cita(
    var id: String,
    val idPsicologo: String,
    val fechaHora: String,
    val disponible: Boolean,
    val idEstudiante: String
){

    fun seId(id: String){
        this.id = id
    }



}
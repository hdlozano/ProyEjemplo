package com.example.proyejemplo.model

import java.util.Date

data class Cita(
    val id: String,
    val idPsicologo: String,
    val fechaHora: String,
    val disponible: Boolean,
    val idEstudiante: String
)
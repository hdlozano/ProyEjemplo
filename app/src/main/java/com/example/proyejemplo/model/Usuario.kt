package com.example.proyejemplo.model

class Usuario(var nombre: String, var id: Int, var email: String, var contraseña: String,  var profesion: String) {

    /*// Getters
    //fun getNombre(): String {
    //    return nombre
    //}

    fun getId(): Int {
        return id
    }

    fun getEmail(): String {
        return email
    }

    fun getContraseña(): String {
        return contraseña
    }
    fun getProfesion(): String {
        return profesion
    }

    // Setters
    //fun setNombre(nombre: String) {
     //   this.nombre = nombre
    //}

    fun setId(id: Int) {
        this.id = id
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setContraseña(contraseña: String) {
        this.contraseña = contraseña
    }
    fun setProfesion(profesion: String) {
        this.profesion = profesion
    }*/
    // Constructor
    constructor() : this("", 0, "", "","")
}
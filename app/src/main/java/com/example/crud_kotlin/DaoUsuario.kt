package com.example.crud_kotlin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoUsuario {

    //Se realiza la peticion para que traiga todos los usuarios en una lista
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuarios(): MutableList<Usuario>

    //con la anotacíon se implementa la insercion de datos
    @Insert
    suspend fun agregarUsuario(usuario: Usuario)

    /*se hace una query en la cual se pueda cambiar solo el apodo,
    teniendo como identificador el "usuario" */
    @Query("UPDATE usuarios set apodo=:apodo WHERE usuario=:usuario")
    suspend fun actualizarUsuario(usuario: String, apodo: String)

    //Se realiza la eliminacion de usuarios cuando el usuario sea igual al primary key que es buscado
    @Query("DELETE FROM usuarios WHERE usuario=:usuario")
    suspend fun borrarUsuario(usuario: String)
}
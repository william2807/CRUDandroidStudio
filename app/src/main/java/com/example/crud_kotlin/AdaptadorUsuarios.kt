package com.example.crud_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/* Clase AdaptadorUsuarios que extiende RecyclerView.Adapter y toma una lista mutable de
Usuario y un listener AdaptadorListener como parámetros*/
class AdaptadorUsuarios(
    val listaUsuarios: MutableList<Usuario>,
    val listener: AdaptadorListener
): RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder>() {

    // Método onCreateViewHolder para crear un nuevo ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflar el diseño del elemento de RecyclerView.
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_usuario, parent, false)
        return ViewHolder(vista)
    }

    // Método getItemCount para obtener el tamaño de la lista de usuarios.
    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    // Método onBindViewHolder para asociar datos a las vistas en el ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = listaUsuarios[position]

        // Asignar los datos del usuario a las vistas en el ViewHolder.
        holder.tvUsuario.text = usuario.usuario
        holder.tvApodo.text = usuario.apodo

        // Configurar listeners para el CardView y el botón Borrar.
        holder.cvUsuario.setOnClickListener {
            listener.onEditItemClick(usuario)
        }
        holder.btnBorrar.setOnClickListener {
            listener.onDeleteItemClick(usuario)
        }
    }

    // Clase ViewHolder para representar los elementos individuales en el RecyclerView.
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // Declaración de vistas en el elemento de RecyclerView.
        val cvUsuario = itemView.findViewById<CardView>(R.id.cvUsuario)
        val tvUsuario = itemView.findViewById<TextView>(R.id.tvUsuario)
        val tvApodo = itemView.findViewById<TextView>(R.id.tvApodo)
        val btnBorrar = itemView.findViewById<Button>(R.id.btnBorrar)
    }
}

package com.example.crud_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.room.Room
import com.example.crud_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

// Declaración de la clase MainActivity que hereda de AppCompatActivity y
// implementa la interfaz AdaptadorListener.
class MainActivity : AppCompatActivity(), AdaptadorListener {

    // Declaración de variables miembro.
    lateinit var binding: ActivityMainBinding
    var listaUsuarios: MutableList<Usuario> = mutableListOf()
    lateinit var adaptador: AdaptadorUsuarios
    lateinit var room: DBPrueba
    lateinit var usuario: Usuario

    // Método onCreate llamado cuando la actividad está siendo creada.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el diseño de la actividad usando el enlace generado ActivityMainBinding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el administrador de diseño para el RecyclerView.
        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)

        // Inicializar la base de datos Room.
        room = Room.databaseBuilder(this, DBPrueba::class.java, "dbPruebas").build()

        // Llamar al método para obtener la lista de usuarios de la base de datos.
        obtenerUsuarios(room)

        // Configurar un click listener para el botón de agregar/actualizar.
        binding.btnAddUpdate.setOnClickListener {
            // Verificar si los campos de usuario y apodo están vacíos.
            if (binding.etUsuario.text.isNullOrEmpty() || binding.etApodo.text.isNullOrEmpty()) {
                Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si el texto del botón es "agregar", crear un nuevo usuario y agregarlo a la base de datos.
            if (binding.btnAddUpdate.text == "agregar") {
                usuario = Usuario(
                    binding.etUsuario.text.toString().trim(),
                    binding.etApodo.text.toString().trim()
                )
                agregarUsuario(room, usuario)
            }
            // Si el texto del botón es "actualizar", actualizar el usuario existente en la base de datos.
            else if (binding.btnAddUpdate.text == "actualizar") {
                usuario.apodo = binding.etApodo.text.toString().trim()
                actualizarUsuario(room, usuario)
            }
        }
    }

    // Método para obtener la lista de usuarios de la base de datos.
    fun obtenerUsuarios(room: DBPrueba) {
        lifecycleScope.launch {
            listaUsuarios = room.daoUsuario().obtenerUsuarios()
            adaptador = AdaptadorUsuarios(listaUsuarios, this@MainActivity)
            binding.rvUsuarios.adapter = adaptador
        }
    }

    // Método para actualizar un usuario en la base de datos.
    fun actualizarUsuario(room: DBPrueba, usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().actualizarUsuario(usuario.usuario, usuario.apodo)
            obtenerUsuarios(room)
            limpiarCampos()
        }
    }

    // Método para limpiar los campos de entrada después de agregar o actualizar un usuario.
    fun limpiarCampos() {
        usuario.usuario = ""
        usuario.apodo = ""
        binding.etUsuario.setText("")
        binding.etApodo.setText("")

        // Restaurar el texto del botón a "agregar" y habilitar la entrada de usuario.
        if (binding.btnAddUpdate.text == "actualizar") {
            binding.btnAddUpdate.text = "agregar"
            binding.etUsuario.isEnabled = true
        }
    }

    // Método para agregar un nuevo usuario a la base de datos.
    fun agregarUsuario(room: DBPrueba, usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().agregarUsuario(usuario)
            obtenerUsuarios(room)
            limpiarCampos()
        }
    }

    // Método para manejar el evento de clic en el ícono de edición.
    override fun onEditItemClick(usuario: Usuario) {
        // Configurar la interfaz de usuario para la actualización de usuario.
        binding.btnAddUpdate.text = "actualizar"
        binding.etUsuario.isEnabled = false
        this.usuario = usuario
        binding.etUsuario.setText(this.usuario.usuario)
        binding.etApodo.setText(this.usuario.apodo)
    }

    // Método para manejar el evento de clic en el ícono de eliminación.
    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteItemClick(usuario: Usuario) {
        lifecycleScope.launch {
            // Eliminar el usuario de la base de datos.
            room.daoUsuario().borrarUsuario(usuario.usuario)
            // Notificar al adaptador sobre el cambio en los datos.
            adaptador.notifyDataSetChanged()
            // Actualizar la lista de usuarios.
            obtenerUsuarios(room)
        }
    }
}

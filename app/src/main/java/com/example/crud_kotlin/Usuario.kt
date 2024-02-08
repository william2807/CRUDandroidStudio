package com.example.crud_kotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario (
    @PrimaryKey var usuario: String,
    @ColumnInfo(name = "Apodo") var apodo: String
)
package com.simon.aplicaciontareas.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tipos_tareas")
data class TiposTareas(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var titulo: String
)

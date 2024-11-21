package com.simon.aplicaciontareas.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tipos_tareas")
data class TipoTarea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, 
    val titulo: String
)

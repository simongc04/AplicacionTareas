package com.simon.aplicaciontareas.datos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



@Entity(
    tableName = "Tareas",
    foreignKeys = [
        ForeignKey(
            entity = TiposTareas::class,
            parentColumns = ["idTipo"],
            childColumns = ["idTipo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Tareas(
    @PrimaryKey(autoGenerate = true)
    val idTarea: Int = 0,
    val descripcion: String,
    val fecha: String,
    val idTipo: Int
)

package com.simon.aplicaciontareas.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simon.aplicaciontareas.TaskViewModel

@Composable
fun TaskApp(taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tareas.collectAsState()

    var nuevoNombreTarea by remember { mutableStateOf("") }
    var nuevaDescripcionTarea by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nuevoNombreTarea,
            onValueChange = { nuevoNombreTarea = it },
            label = { Text("Nombre de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nuevaDescripcionTarea,
            onValueChange = { nuevaDescripcionTarea = it },
            label = { Text("Descripción de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (nuevoNombreTarea.isNotBlank() && nuevaDescripcionTarea.isNotBlank()) {
                taskViewModel.agregarTarea(nuevoNombreTarea, nuevaDescripcionTarea)
                nuevoNombreTarea = ""
                nuevaDescripcionTarea = ""
            }
        }) {
            Text("Añadir tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Lista de Tareas", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))

        // Usamos LazyColumn para renderizar las tareas de manera eficiente
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(tasks) { tarea -> // Se usa 'items' para crear la lista lazily
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Titulo Tarea", style = MaterialTheme.typography.h6)
                        Text(tarea.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Descripción", style = MaterialTheme.typography.h6)
                        Text(tarea.description)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { taskViewModel.eliminarTarea(tarea) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
                            }
                        }
                    }
                }
            }
        }
    }
}

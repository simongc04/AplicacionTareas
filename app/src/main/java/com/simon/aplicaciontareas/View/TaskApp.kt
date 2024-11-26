package com.simon.aplicaciontareas.View

import androidx.compose.foundation.layout.*
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

    var newTaskName by remember { mutableStateOf("") }

    // Column para la UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = newTaskName,
            onValueChange = { newTaskName = it },
            label = { Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (newTaskName.isNotBlank()) {
                taskViewModel.agregarTarea(newTaskName)
                newTaskName = ""
            }
        }) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        tasks.forEach { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(task.name)

                IconButton(onClick = { taskViewModel.eliminarTarea(task) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                }
            }
        }
    }
}

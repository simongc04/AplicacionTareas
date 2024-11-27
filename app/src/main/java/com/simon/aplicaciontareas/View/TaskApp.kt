import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simon.aplicaciontareas.TaskViewModel
import com.simon.aplicaciontareas.datos.Task

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
        Text("Añadir Tarea", color = Color(0xFFFF0000))
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nuevoNombreTarea,
            onValueChange = { nuevoNombreTarea = it },
            label = { Text("Nombre de la tarea") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF0000))
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nuevaDescripcionTarea,
            onValueChange = { nuevaDescripcionTarea = it },
            label = { Text("Descripción de la tarea") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF0000))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para añadir la tarea
        Button(
            onClick = {
                if (nuevoNombreTarea.isNotBlank() && nuevaDescripcionTarea.isNotBlank()) {
                    taskViewModel.agregarTarea(nuevoNombreTarea, nuevaDescripcionTarea)
                    nuevoNombreTarea = ""
                    nuevaDescripcionTarea = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000))
        ) {
            Text("Añadir tarea", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de tareas
        Text("Lista de Tareas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(tasks) { tarea -> TareaCard(taskViewModel, tarea) } // Mostrar cada tarea
        }
    }
}

@Composable
fun TareaCard(taskViewModel: TaskViewModel, tarea: Task) {
    var isEditing by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf(tarea.name) }
    var nuevaDescripcion by remember { mutableStateOf(tarea.description) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                // Mostrar campos para editar tarea
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text("Nuevo Título") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF0000))
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nuevaDescripcion,
                    onValueChange = { nuevaDescripcion = it },
                    label = { Text("Nueva Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF0000))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para guardar cambios
                Button(
                    onClick = {
                        if (nuevoNombre.isNotBlank() && nuevaDescripcion.isNotBlank()) {
                            taskViewModel.modificarTarea(tarea.copy(name = nuevoNombre, description = nuevaDescripcion))
                            isEditing = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000))
                ) {
                    Text("Guardar cambios", color = Color.White)
                }
            } else {
                // Mostrar información de la tarea
                Text("Tarea: ${tarea.name}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Descripción: ${tarea.description}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(8.dp))

                // Botones para eliminar o editar tarea
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { taskViewModel.eliminarTarea(tarea) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea", tint = Color(0xFF0000FF))
                    }

                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar tarea", tint = Color(0xFFFF0000))
                    }
                }
            }
        }
    }
}

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
import com.simon.aplicaciontareas.datos.Task
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val tiposTareasDao = database.tiposTareasDao()
    val scope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var tipos_tareas by remember { mutableStateOf(listOf<TiposTareas>()) }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }
    var selectedTaskType by remember { mutableStateOf<TiposTareas?>(null) }
    var newTypeTaskName by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var editingTaskName by remember { mutableStateOf("") }
    var editingTaskDescription by remember { mutableStateOf("") }
    var editingTipoTarea by remember { mutableStateOf<TiposTareas?>(null) }
    var editingTipoTareaName by remember { mutableStateOf("") }

    // Cargar tareas y tipos de tareas al iniciar
    LaunchedEffect(Unit) {
        try {
            tasks = taskDao.getAllTasks()
            tipos_tareas = tiposTareasDao.getAllTiposTareas()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8B0000))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tareas
        Text(
            text = "Tareas",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(vertical = 2.dp)
                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(6.dp)
                .padding(horizontal = 14.dp)
        )

        OutlinedTextField(
            value = if (editingTask != null) editingTaskName else newTaskName,
            onValueChange = {
                if (editingTask != null) editingTaskName = it else newTaskName = it
            },
            label = { Text(if (editingTask != null) "Editar tarea" else "Nombre de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = if (editingTask != null) editingTaskDescription else newTaskDescription,
            onValueChange = {
                if (editingTask != null) editingTaskDescription = it else newTaskDescription = it
            },
            label = { Text(if (editingTask != null) "Editar descripción" else "Descripción de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { dropdownExpanded = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black) // Fondo negro
            ) {
                Text(text = selectedTaskType?.titulo ?: "Selecciona un tipo de tarea", color = Color.White)
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                tipos_tareas.forEach { tipo ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTaskType = tipo
                            dropdownExpanded = false
                        }
                    ) {
                        Text(text = tipo.titulo)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    try {
                        if (editingTask != null) {
                            editingTask?.let {
                                it.titulo = editingTaskName
                                it.descripcion = editingTaskDescription
                                it.id_tipostareas = selectedTaskType?.id ?: it.id_tipostareas
                                taskDao.update(it)
                            }
                            editingTask = null
                            editingTaskName = ""
                            editingTaskDescription = ""
                            selectedTaskType = null
                        } else if (newTaskName.isNotEmpty() && selectedTaskType != null) {
                            val newTask = Task(
                                id = 0,
                                titulo = newTaskName,
                                descripcion = newTaskDescription,
                                id_tipostareas = selectedTaskType!!.id
                            )
                            taskDao.insert(newTask)
                            newTaskName = ""
                            newTaskDescription = ""
                            selectedTaskType = null
                        }
                        tasks = taskDao.getAllTasks()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },

            modifier = Modifier

                .fillMaxWidth()
                .background(Color.Black), // Fondo negro

            enabled = (editingTask != null || (selectedTaskType != null && newTaskName.isNotEmpty() && newTaskDescription.isNotEmpty()))
        ) {
            Text(if (editingTask != null) "Guardar cambios" else "Agregar tarea", color = Color.White)
        }

        tasks.forEach { task ->
            val tipoTareaTitulo = tipos_tareas.find { it.id == task.id_tipostareas }?.titulo ?: "Desconocido"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Column(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(text = " ${task.titulo}", style = MaterialTheme.typography.h6)
                        Text(
                            text = " ${task.descripcion}",
                            style = MaterialTheme.typography.body1,
                            color = Color.Black
                        )
                        Text(text = " $tipoTareaTitulo", style = MaterialTheme.typography.body2)
                    }
                }
                Row {
                    Button(onClick = {
                        editingTask = task
                        editingTaskName = task.titulo
                        editingTaskDescription = task.descripcion
                        selectedTaskType = tipos_tareas.find { it.id == task.id_tipostareas }
                    }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        scope.launch(Dispatchers.IO) {
                            try {
                                taskDao.delete(task)
                                tasks = taskDao.getAllTasks()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }) { Icon(Icons.Default.Close, contentDescription = "Eliminar") }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tipos de tarea
        Text(
            text = "Tipos de tarea",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 2.dp)
                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(6.dp)
                .padding(horizontal = 14.dp)
        )

        OutlinedTextField(
            value = if (editingTipoTarea != null) editingTipoTareaName else newTypeTaskName,
            onValueChange = {
                if (editingTipoTarea != null) editingTipoTareaName = it else newTypeTaskName = it
            },
            label = { Text(if (editingTipoTarea != null) "Editar tipo de tarea" else "Nombre del tipo") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    try {
                        var sameTitle = false
                        tipos_tareas.forEach {
                            if (it.titulo.lowercase() == newTypeTaskName.lowercase()) {
                                sameTitle = true
                            }
                        }
                        if (editingTipoTarea != null) {
                            editingTipoTarea?.let {
                                it.titulo = editingTipoTareaName
                                tiposTareasDao.update(it)
                            }
                            editingTipoTarea = null
                            editingTipoTareaName = ""
                        } else if (newTypeTaskName.isNotEmpty() && !sameTitle) {
                            val newTypeTask = TiposTareas(titulo = newTypeTaskName)
                            tiposTareasDao.insert(newTypeTask)
                            newTypeTaskName = ""
                        }
                        tipos_tareas = tiposTareasDao.getAllTiposTareas()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .background(Color.Black), // Fondo negro
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black) // Fondo negro

        ) {
            Text(if (editingTipoTarea != null) "Guardar cambios" else "Agregar tipo de tarea", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        tipos_tareas.forEach { tipo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = tipo.titulo, style = MaterialTheme.typography.h6)
                Row {
                    Button(onClick = {
                        editingTipoTarea = tipo
                        editingTipoTareaName = tipo.titulo
                    }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        scope.launch(Dispatchers.IO) {
                            try {
                                tiposTareasDao.delete(tipo)
                                tipos_tareas = tiposTareasDao.getAllTiposTareas()
                                tasks = taskDao.getAllTasks()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }) { Icon(Icons.Default.Close, contentDescription = "Eliminar") }
                }
            }
        }
    }
}

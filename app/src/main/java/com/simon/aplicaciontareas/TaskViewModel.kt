package com.simon.aplicaciontareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.aplicaciontareas.datos.Task
import com.simon.aplicaciontareas.datos.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class TaskViewModel(private val daoTareas: TaskDao) : ViewModel() {
    private val _tareas = MutableStateFlow<List<Task>>(emptyList())
    val tareas: StateFlow<List<Task>> get() = _tareas

    // Estado para manejar los mensajes de error.
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage


    // se carga la lista de tareas al crear el ViewModel.
    init {
        cargarTareas()
    }

    private fun cargarTareas() {
        viewModelScope.launch {
            try {
                _tareas.value = daoTareas.getAllTasks() // Cargar todas las tareas
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al cargar tareas", e)
                _errorMessage.value = "Error al cargar tareas: ${e.message}"
            }
        }
    }

    fun agregarTarea(nombreTarea: String, descripcionTarea: String) {
        viewModelScope.launch {
            try {
                // Crear una nueva tarea y agregarla al repositorio
                val nuevaTarea = Task(name = nombreTarea, description = descripcionTarea)
                daoTareas.insert(nuevaTarea)
                cargarTareas()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al agregar tarea", e)
                _errorMessage.value = "Error al agregar tarea: ${e.message}"
            }
        }
    }

    fun eliminarTarea(tarea: Task) {
        viewModelScope.launch {
            try {
                daoTareas.delete(tarea)
                cargarTareas()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al eliminar tarea", e)
                _errorMessage.value = "Error al eliminar tarea: ${e.message}"
            }
        }
    }

    fun modificarTarea(tarea: Task) {
        viewModelScope.launch {
            try {
                daoTareas.update(tarea)
                cargarTareas()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al modificar tarea", e)
                _errorMessage.value = "Error al modificar tarea: ${e.message}"
            }
        }
    }
}

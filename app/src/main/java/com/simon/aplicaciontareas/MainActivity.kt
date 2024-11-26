package com.simon.aplicaciontareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.simon.aplicaciontareas.View.TaskApp
import com.simon.aplicaciontareas.datos.AppDatabase



class MainActivity : ComponentActivity() {

    lateinit var database: AppDatabase

    var taskViewModel: TaskViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(applicationContext)

        taskViewModel = TaskViewModel(database.taskDao())
        setContent {
            taskViewModel?.let {
                TaskApp(it)
            }
        }
    }
}
package com.example.todo_list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list.data.model.Task
import com.example.todo_list.data.db.MyRepository
import com.example.todo_list.data.model.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: MyRepository) : ViewModel() {

    val tasks = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(task: Task, onInserted: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.addTask(task)
            onInserted(id)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            tasks.value
                .filter { it.categoryId == category.id }
                .forEach { taskWithCategory ->
                    val updatedTask = taskWithCategory.copy(categoryId = -1)
                    repository.updateTask(updatedTask)
                }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    suspend fun getCategoryById(id: Int?): Category? {
        return if (id == null) null else repository.getCategoryById(id).firstOrNull()
    }

    suspend fun getCategoryByName(name: String): Category? {
        return  repository.getCategoryByName(name).firstOrNull()
    }


}

package com.example.todo_list.data.db

import com.example.todo_list.data.model.Category
import com.example.todo_list.data.model.Task
import kotlinx.coroutines.flow.Flow

class MyRepository(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(task: Task) = taskDao.insert(task)

    suspend fun deleteTask(task: Task) = taskDao.delete(task)

    suspend fun updateTask(task: Task) = taskDao.update(task)

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun addCategory(category: Category) = categoryDao.insert(category)

    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    suspend fun updateCategory(category: Category) = categoryDao.update(category)

    fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)

    fun getCategoryByName(categoryName: String) = categoryDao.getCategoryByName(categoryName)
}

package com.example.todo_list.navigation

sealed class Routes(val route: String) {
    data object TaskList : Routes("task_list")
}

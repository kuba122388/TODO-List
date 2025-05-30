package com.example.todo_list.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todo_list.ui.screens.TaskListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.TaskList.route
    ) {
        composable(route = Routes.TaskList.route) {
            TaskListScreen(navController = navController)
        }
    }
}

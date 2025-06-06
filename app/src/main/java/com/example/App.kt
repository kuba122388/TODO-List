package com.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.todo_list.data.db.AppDatabase
import com.example.todo_list.data.db.TaskRepository
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.ui.theme.Theme
import com.example.todo_list.navigation.NavGraph
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.viewModel.TaskViewModel
import com.example.todo_list.viewModel.TaskViewModelFactory

@Composable
fun App() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context = context)
    val repo = TaskRepository(taskDao = db.taskDao(), categoryDao = db.categoryDao())

    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repo)
    )

    Theme {
        val navController = rememberNavController()

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = TODOListTheme.colors.background
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(
                        top = Dimens.mediumPadding,
                        start = Dimens.largePadding,
                        end = Dimens.mediumPadding,
                        bottom = Dimens.mediumPadding
                    )
            ) {
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}

package com.example.todo_list.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.navigation.Routes
import com.example.todo_list.sharedPreferences.SharedPreferencesHelper
import com.example.todo_list.ui.components.SearchModule
import com.example.todo_list.ui.components.TaskCard
import com.example.todo_list.ui.components.TopBar
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.viewModel.TaskViewModel

@Composable
fun TaskListScreen(
    navController: NavController,
    sharedPreferencesHelper: SharedPreferencesHelper,
    viewModel: TaskViewModel
) {
    val userName = sharedPreferencesHelper.getUserName()

    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val listState = rememberLazyListState()

    var selectedCategory by remember { mutableIntStateOf(-1) }
    var addButtonExpanded by remember { mutableStateOf(false) }

    var showCategoryDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }

    val showDoneTasks = sharedPreferencesHelper.getHideDoneTask()
    val engHour = sharedPreferencesHelper.getFullHourFormat()

    val sampleTasks by viewModel.tasks.collectAsState(initial = emptyList())
    val categoryList by viewModel.categories.collectAsState(initial = emptyList())

    val filteredTasks =
        sampleTasks.filter { task ->
            (selectedCategory == -1 || task.categoryId == selectedCategory) &&
                    (searchText.isEmpty() || task.title.lowercase()
                        .contains(searchText.lowercase()))
        }.sortedBy { it.destinationDate }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            text = "Welcome, $userName!",
            id = R.drawable.settings,
            navController = navController,
            destination = Routes.Settings.route
        )

        SearchModule(searchText = searchText, onSearchChange = { searchText = it })

        Spacer(Modifier.size(Dimens.mediumPadding))

        if (filteredTasks.isEmpty()) Text(
            text = "No tasks found",
            fontFamily = OswaldFontFamily,
            fontSize = Dimens.fontTiny,
            fontWeight = FontWeight.Light
        )


        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.82f),
            state = listState
        ) {
            itemsIndexed(
                filteredTasks,
                key = { index, task -> task.title + index }) { index, task ->
                var selected by remember { mutableStateOf(task.isDone) }
                var expanded by remember { mutableStateOf(false) }

                LaunchedEffect(expanded) {
                    if (expanded) {
                        listState.animateScrollToItem(index)
                    }
                }

                if (!showDoneTasks || !task.isDone) TaskCard(
                    task = task,
                    selected = selected,
                    expanded = expanded,
                    engHour = engHour,
                    categories = categoryList,
                    viewModel = viewModel,
                    onSelectedToggle = {
                        selected = !selected;
                        val newTask = task.copy(isDone = selected)
                        viewModel.updateTask(newTask)
                    },
                    onExpandedToggle = { expanded = !expanded }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterButtons(
                selectedCategory,
                clearFilter = { selectedCategory = -1 },
                showFilterDialog = { showFilterDialog = true })

            Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))

            AddButtons(
                addButtonExpanded,
                onTogglePlus = { addButtonExpanded = !addButtonExpanded },
                onToggleTask = { showTaskDialog = true; addButtonExpanded = false },
                onToggleCategory = { showCategoryDialog = true; addButtonExpanded = false })

            if (showFilterDialog) {
                FilterDialog(
                    categoryList,
                    dismiss = { showFilterDialog = false },
                    changeCategory = { selectedCategory = it }
                )
            }

            if (showCategoryDialog) {
                CategoryDialog(dismiss = { showCategoryDialog = false }, viewModel = viewModel)
            }

            if (showTaskDialog) {
                TaskDialog(
                    dismiss = { showTaskDialog = false },
                    categoryList = categoryList,
                    viewModel = viewModel
                )
            }

        }
    }
}

@Composable
fun FilterButtons(filter: Int, clearFilter: () -> Unit, showFilterDialog: () -> Unit) {
    ActionButton(
        modifier = Modifier.clickable {
            showFilterDialog()
        },
        id = R.drawable.filter,
        buttonSize = Dimens.actionBtnSize,
        imgSize = 24.dp
    )
    if (filter != -1) ActionButton(
        modifier = Modifier.clickable {
            clearFilter()
        },
        id = R.drawable.close,
        buttonSize = Dimens.actionOptBtnSize,
        imgSize = 20.dp
    ) else Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))
}

@Composable
fun AddButtons(
    addButtonExpanded: Boolean,
    onTogglePlus: () -> Unit,
    onToggleTask: () -> Unit,
    onToggleCategory: () -> Unit
) {
    if (addButtonExpanded) SingleButton(text = "Task", R.drawable.task) { onToggleTask() }
    else Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))

    if (addButtonExpanded) SingleButton(
        text = "Category",
        R.drawable.category,
    ) { onToggleCategory() }
    else Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))

    ActionButton(
        modifier = Modifier.clickable { onTogglePlus() },
        id = R.drawable.plus,
        buttonSize = Dimens.actionBtnSize,
        imgSize = 30.dp
    )
}

@Composable
fun SingleButton(text: String, id: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.alpha(0f),
            text = text,
            fontSize = Dimens.fontUnderBtn,
            fontFamily = OswaldFontFamily,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.size(3.dp))
        ActionButton(
            modifier = Modifier.clickable { onClick() },
            id = id,
            buttonSize = Dimens.actionOptBtnSize,
            imgSize = 20.dp
        )
        Spacer(Modifier.size(3.dp))
        Text(
            text = text,
            fontSize = Dimens.fontUnderBtn,
            fontFamily = OswaldFontFamily,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun ActionButton(
    id: Int,
    buttonSize: Dp,
    imgSize: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = CircleShape, clip = false)
            .clip(CircleShape)
            .background(color = TODOListTheme.colors.onButtons)
            .height(buttonSize)
            .width(buttonSize),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(imgSize),
            painter = painterResource(id = id),
            contentDescription = null
        )
    }
}



package com.example.todo_list.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.data.model.Task
import com.example.todo_list.ui.components.SearchModule
import com.example.todo_list.ui.components.TaskCard
import com.example.todo_list.ui.components.TopBar
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OpenSansCondensed
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme
import java.time.LocalDateTime

@Composable
fun TaskListScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val listState = rememberLazyListState()

    var filterByCategory by remember { mutableStateOf("as") }
    var addButtonExpanded by remember { mutableStateOf(true) }

    var showTaskDialog by remember { mutableStateOf(false) }

    val sampleTasks = listOf(
        Task(
            title = "Zrobić zakupy",
            description = "Mleko, chleb, jajka",
            creationDate = LocalDateTime.now(),
            destinationDate = LocalDateTime.now().plusDays(1),
            category = Category("Dom", Color(0xFF4CAF50)),
            notification = true,
            attachments = listOf()
        ),
        Task(
            title = "Spotkanie projektowe",
            description = "Omawiamy sprint 5asdasdsadsdasdaasdsdasdadsdsadasasdsdasdasda",
            creationDate = LocalDateTime.now(),
            destinationDate = LocalDateTime.now().plusDays(2).plusHours(5),
            category = Category("Praca", Color(0xFF2196F3)),
            notification = false,
            attachments = listOf("sciezka/do/plik1.pdf")
        ),
        Task(
            title = "Trening",
            description = "Siłownia o 19:00",
            creationDate = LocalDateTime.now(),
            destinationDate = LocalDateTime.now().plusDays(1),
            category = Category("Zdrowie", Color(0xFFFF5722)),
            notification = true,
            attachments = listOf("zdjecie1.jpg", "zdjecie2.jpg")
        ),
        Task(
            title = "Wyjść na dwór",
            description = "Nie siedź przy kompie",
            creationDate = LocalDateTime.now(),
            destinationDate = LocalDateTime.now().plusDays(1),
            category = Category("Dom", Color(0xFF4CAF50)),
            notification = false,
            attachments = listOf()
        ),
    )


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
        TopBar()

        Spacer(Modifier.size(Dimens.smallPadding))

        SearchModule(searchText = searchText, onSearchChange = { searchText = it })

        Spacer(Modifier.size(Dimens.mediumPadding))

        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.82f),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingTasks)
        ) {
            itemsIndexed(sampleTasks, key = { index, task -> task.title + index }) { index, task ->
                var selected by remember { mutableStateOf(false) }
                var expanded by remember { mutableStateOf(false) }

                LaunchedEffect(expanded) {
                    if (expanded) {
                        listState.animateScrollToItem(index)
                    }
                }

                TaskCard(
                    task = task,
                    selected = selected,
                    expanded = expanded,
                    onSelectedToggle = { selected = !selected },
                    onExpandedToggle = { expanded = !expanded }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterButtons(filterByCategory) { filterByCategory = "" }

            Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))

            AddButtons(addButtonExpanded) { addButtonExpanded = !addButtonExpanded }
        }
    }
}

@Composable
fun FilterButtons(filter: String, clearFilter: () -> Unit) {
    ActionButton(
        id = R.drawable.filter,
        buttonSize = Dimens.actionBtnSize,
        imgSize = 24.dp
    )
    if (filter.isNotEmpty()) ActionButton(
        modifier = Modifier.clickable {
            clearFilter()
        },
        id = R.drawable.close,
        buttonSize = Dimens.actionOptBtnSize,
        imgSize = 20.dp
    ) else Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))
}

@Composable
fun AddButtons(addButtonExpanded: Boolean, onToggle: () -> Unit) {
    if (addButtonExpanded) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.alpha(0f),
                text = "Task",
                fontSize = Dimens.fontUnderBtn,
                fontFamily = OswaldFontFamily,
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.size(3.dp))
            ActionButton(
                id = R.drawable.task,
                buttonSize = Dimens.actionOptBtnSize,
                imgSize = 20.dp
            )
            Spacer(Modifier.size(3.dp))
            Text(
                text = "Task",
                fontSize = Dimens.fontUnderBtn,
                fontFamily = OswaldFontFamily,
                fontWeight = FontWeight.Light
            )
        }

    } else Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))
    if (addButtonExpanded) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.alpha(0f),
                text = "Category",
                fontSize = Dimens.fontUnderBtn,
                fontFamily = OswaldFontFamily,
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.size(3.dp))
            ActionButton(
                id = R.drawable.category,
                buttonSize = Dimens.actionOptBtnSize,
                imgSize = 20.dp
            )
            Spacer(Modifier.size(3.dp))
            Text(
                text = "Category",
                fontSize = Dimens.fontUnderBtn,
                fontFamily = OswaldFontFamily,
                fontWeight = FontWeight.Light
            )
        }
    } else Spacer(modifier = Modifier.size(Dimens.actionOptBtnSize))

    ActionButton(
        modifier = Modifier.clickable { onToggle() },
        id = R.drawable.plus,
        buttonSize = Dimens.actionBtnSize,
        imgSize = 30.dp
    )
}

@Composable
fun ActionButton(id: Int, buttonSize: Dp, imgSize: Dp, modifier: Modifier = Modifier) {
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

@Composable
fun InsideTaskButtons() {
    Box(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(Dimens.roundedSize))
            .background(TODOListTheme.colors.taskButtons)
            .padding(
                horizontal = Dimens.smallPadding,
                vertical = Dimens.tinyPadding
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(Dimens.imgSize),
                painter = painterResource(id = R.drawable.edit),
                contentDescription = null
            )
            Spacer(Modifier.size(Dimens.tinyPadding))
            Text(
                "Edit",
                color = TODOListTheme.colors.onTaskText,
                fontFamily = OpenSansCondensed,
                fontWeight = FontWeight.Light,
                fontSize = Dimens.fontTiny
            )
            Spacer(Modifier.size(Dimens.smallPadding))
        }
    }
    Spacer(modifier = Modifier.size(10.dp))
    Box(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(Dimens.roundedSize))
            .background(TODOListTheme.colors.taskButtons)
            .padding(
                horizontal = Dimens.smallPadding,
                vertical = Dimens.tinyPadding
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier.size(Dimens.imgSize),
                painter = painterResource(id = R.drawable.edit),
                contentDescription = null
            )
            Spacer(Modifier.size(Dimens.tinyPadding))

            Text(
                "Delete",
                color = TODOListTheme.colors.onTaskText,
                fontFamily = OpenSansCondensed,
                fontWeight = FontWeight.Light,
                fontSize = Dimens.fontTiny
            )
            Spacer(Modifier.size(Dimens.smallPadding))
        }
    }
}


@Composable
fun TaskContent(task: Task, expanded: Boolean) {
    if (expanded) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .padding(start = 2.dp)
        ) {
            Text(
                task.description,
                color = TODOListTheme.colors.onTaskText,
                fontFamily = OpenSansCondensed,
                fontWeight = FontWeight.Light,
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            task.attachments.forEach { attachment ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        modifier = Modifier.size(Dimens.imgSize),
                        painter = painterResource(id = R.drawable.attachment),
                        contentDescription = null,
                    )
                    Text(
                        attachment,
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OpenSansCondensed,
                        fontWeight = FontWeight.Light,
                        textDecoration = TextDecoration.Underline
                    )

                }
                Spacer(modifier = Modifier.size(Dimens.tinyPadding))
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row { InsideTaskButtons() }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.roundedSize))
                    .background(
                        if (task.category != null)
                            task.category.Color
                                .copy(alpha = 0.9f)
                                .compositeOver(Color.Black.copy(alpha = 1f))
                        else TODOListTheme.colors.taskButtons
                    )
                    .padding(
                        horizontal = Dimens.mediumPadding,
                        vertical = Dimens.tinyPadding
                    )
            ) {
                task.category?.let {
                    Text(
                        it.Title,
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OpenSansCondensed
                    )
                }
            }
        }
    }
}

@Composable
fun TaskHeaderRow(
    task: Task,
    selected: Boolean,
    expanded: Boolean,
    onSelectToggle: () -> Unit,
    onExpandToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clickable { onExpandToggle() }
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Spacer(modifier = Modifier.size(2.dp))
            CustomRadioButton(
                selected = selected,
                onClick = onSelectToggle
            )
        }

        Spacer(Modifier.size(Dimens.smallPadding))

        Text(
            task.title,
            color = TODOListTheme.colors.onTaskText,
            fontFamily = OpenSansCondensed,
            fontWeight = FontWeight.Light,
            fontSize = Dimens.fontSmall,
            modifier = Modifier.weight(1f)
        )

        ExpandButton(expanded = expanded)
    }
}


@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon =
        if (selected) painterResource(id = R.drawable.radio_button_filled)
        else painterResource(id = R.drawable.radio_button_unfilled)


    Icon(
        painter = icon,
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .size(Dimens.radioBtnSize)
            .clickable(onClick = onClick)
    )
}

@Composable
fun ExpandButton(
    expanded: Boolean
) {
    val icon = painterResource(id = R.drawable.expand_white)

    Icon(
        painter = icon,
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .size(Dimens.radioBtnSize)
            .rotate(if (expanded) 180f else 0f)
    )
}

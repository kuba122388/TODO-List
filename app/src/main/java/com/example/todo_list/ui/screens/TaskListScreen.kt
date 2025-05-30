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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.data.model.Task
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.InputFieldInactive
import com.example.todo_list.ui.theme.OnSearchBar
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme
import java.time.LocalDate

@Composable
fun TaskListScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val sampleTasks = listOf(
        Task(
            title = "Zrobić zakupy",
            description = "Mleko, chleb, jajka",
            creationDate = LocalDate.now(),
            destinationDate = LocalDate.now().plusDays(1),
            category = Category("Dom", Color(0xFF4CAF50)),
            notification = true,
            attachments = listOf()
        ),
        Task(
            title = "Spotkanie projektowe",
            description = "Omawiamy sprint 5",
            creationDate = LocalDate.now(),
            destinationDate = LocalDate.now().plusDays(2),
            category = Category("Praca", Color(0xFF2196F3)),
            notification = false,
            attachments = listOf("sciezka/do/plik1.pdf")
        ),
        Task(
            title = "Trening",
            description = "Siłownia o 19:00",
            creationDate = LocalDate.now(),
            destinationDate = LocalDate.now().plusDays(1),
            category = Category("Zdrowie", Color(0xFFFF5722)),
            notification = true,
            attachments = listOf("zdjecie1.jpg", "zdjecie2.jpg")
        )
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

        Spacer(Modifier.size(Dimens.smallPadding))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingTasks)
        ) {
            items(sampleTasks) { task ->
                var selected by remember { mutableStateOf(false) }
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .clip(RoundedCornerShape(Dimens.roundedSize))
                        .background(TODOListTheme.colors.taskWOCategory)
                        .padding(horizontal = Dimens.smallPadding, vertical = Dimens.smallPadding)
                ) {
                    Column {
                        TopTaskInfo()
                        Spacer(modifier = Modifier.size(Dimens.tinyPadding))
                        TaskHeaderRow(
                            task = task,
                            selected = selected,
                            expanded = expanded,
                            onSelectToggle = { selected = !selected },
                            onExpandToggle = { expanded = !expanded })
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    val myColors = TODOListTheme.colors
    Row {
        Column(
            modifier = Modifier.wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier)
                    Text(
                        "Welcome, Jacob!",
                        fontSize = Dimens.fontBig,
                        fontFamily = OswaldFontFamily.Oswald,
                        maxLines = 1
                    )
                    HorizontalDivider(color = Color.Black, thickness = 2.dp)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.roundedSize))
                        .background(myColors.controlBackground)
                        .padding(all = Dimens.insidePadding)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun SearchModule(searchText: String, onSearchChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Find your task",
            fontSize = Dimens.fontMedium,
            fontFamily = OswaldFontFamily.Oswald,
            fontWeight = FontWeight.Light,
            maxLines = 1,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.size(Dimens.tinyPadding))
        CustomSearchBar(query = searchText, onQueryChange = onSearchChange)
    }
}

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.roundedSize))
            .background(InputFieldInactive)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = OnSearchBar
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = OswaldFontFamily.Oswald,
                    fontWeight = FontWeight.Light,
                    color = OnSearchBar
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            "What are we looking for? :)",
                            fontSize = Dimens.fontTiny,
                            fontFamily = OswaldFontFamily.Oswald,
                            fontWeight = FontWeight.Light,
                            color = OnSearchBar,
                            letterSpacing = 0.5.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun TopTaskInfo() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(Dimens.imgSize),
                        painter = painterResource(id = R.drawable.create),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(Dimens.tinyPadding))
                    Text(
                        "25.05.2025 2:35PM",
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OswaldFontFamily.Oswald,
                        fontWeight = FontWeight.Light
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(Dimens.imgSize),
                        painter = painterResource(id = R.drawable.target),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(Dimens.tinyPadding))
                    Text(
                        "25.05.2025 2:35PM",
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OswaldFontFamily.Oswald,
                        fontWeight = FontWeight.Light
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(Dimens.imgSize),
                        painter = painterResource(id = R.drawable.attachment),
                        contentDescription = null,
                    )
                    Text(
                        "3",
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OswaldFontFamily.Oswald,
                        fontWeight = FontWeight.Light
                    )
                }

                Image(
                    modifier = Modifier.size(Dimens.imgSize),
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = null
                )

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
            .clickable { onExpandToggle() },
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
            fontFamily = OswaldFontFamily.Oswald,
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
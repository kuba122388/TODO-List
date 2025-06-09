package com.example.todo_list.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.data.model.Task
import com.example.todo_list.ui.screens.TaskDialog
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OpenSansCondensed
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.viewModel.TaskViewModel
import java.io.File
import java.time.format.DateTimeFormatter

@Composable
fun TaskCard(
    task: Task,
    categories: List<Category>,
    selected: Boolean,
    expanded: Boolean,
    engHour: Boolean,
    viewModel: TaskViewModel,
    onSelectedToggle: () -> Unit,
    onExpandedToggle: () -> Unit
) {
    val category = categories.find { it.id == task.categoryId }

    Column {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.roundedSize))
                .background(
                    if (task.categoryId != -1)
                        Color(category!!.colorLong).copy(alpha = 0.8f)
                    else TODOListTheme.colors.taskWOCategory
                )
                .padding(horizontal = Dimens.smallPadding, vertical = Dimens.smallPadding)
        ) {
            Column {
                TopTaskInfo(task, engHour)
                Spacer(modifier = Modifier.size(Dimens.tinyPadding))
                TaskHeaderRow(
                    task = task,
                    selected = selected,
                    expanded = expanded,
                    onSelectToggle = { onSelectedToggle() },
                    onExpandToggle = { onExpandedToggle() })
                TaskContent(task, expanded, category, viewModel, categories)
            }
        }
    }
    Spacer(Modifier.size(Dimens.spacingTasks))
}

@Composable
fun TopTaskInfo(task: Task, fullHour: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(0.995f),
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
                        if (fullHour) task.creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:MM"))
                        else task.creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy h:mma")),
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OpenSansCondensed,
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
                        if (fullHour) task.destinationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:MM"))
                        else task.destinationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy h:mma")),
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OpenSansCondensed,
                        fontWeight = FontWeight.Light
                    )
                }
                Row(
                    modifier = Modifier.alpha(
                        if (task.attachments.isNotEmpty()) 1f
                        else 0f
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(Dimens.imgSize),
                        painter = painterResource(id = R.drawable.attachment),
                        contentDescription = null,
                    )
                    Text(
                        task.attachments.size.toString(),
                        color = TODOListTheme.colors.onTaskText,
                        fontFamily = OpenSansCondensed,
                        fontWeight = FontWeight.Light
                    )
                }

                Image(
                    modifier = Modifier
                        .size(Dimens.imgSize)
                        .alpha(
                            if (task.notification) 1f
                            else 0f
                        ),
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
fun TaskContent(
    task: Task,
    expanded: Boolean,
    category: Category?,
    viewModel: TaskViewModel,
    categories: List<Category>
) {
    val context = LocalContext.current

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
                        attachment.lastPathSegment?.substringAfterLast("/")!!,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(
                                    attachment,
                                    context.contentResolver.getType(attachment)
                                )
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }

                            try {
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Toast
                                    .makeText(
                                        context,
                                        "Brak aplikacji do otwarcia tego pliku",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },

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
            Row { InsideTaskButtons(task, viewModel, categories) }
            if (task.categoryId != -1) Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.roundedSize))
                    .background(
                        if (task.categoryId != -1)
                            Color(category!!.colorLong).copy(alpha = 0.8f)
                        else TODOListTheme.colors.taskWOCategory
                    )
                    .padding(
                        horizontal = Dimens.mediumPadding,
                        vertical = Dimens.tinyPadding
                    )
            ) {
                Text(
                    category!!.title,
                    color = TODOListTheme.colors.onTaskText,
                    fontFamily = OpenSansCondensed
                )
            }
        }
    }
}

@Composable
fun InsideTaskButtons(task: Task, viewModel: TaskViewModel, categoryList: List<Category>) {
    var showTask by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clickable {
                showTask = !showTask
            }
            .width(80.dp)
            .clip(RoundedCornerShape(Dimens.roundedSize))
            .background(TODOListTheme.colors.taskButtons)
            .padding(
                horizontal = Dimens.smallPadding,
                vertical = Dimens.tinyPadding
            )

    ) {
        if (showTask) {
            TaskDialog(
                dismiss = { showTask = !showTask },
                categoryList = categoryList,
                viewModel = viewModel,
                task = task
            )
        }
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
            .clickable {
                task.attachments.forEach { uri ->
                    val file = File(context.filesDir, "${uri.lastPathSegment?.substringAfterLast("/")}")
                    if (file.exists()) {
                        file.delete()
                    }
                }
                viewModel.deleteTask(task)
                Toast
                    .makeText(context, "Task deleted successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
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
                painter = painterResource(id = R.drawable.delete_white),
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

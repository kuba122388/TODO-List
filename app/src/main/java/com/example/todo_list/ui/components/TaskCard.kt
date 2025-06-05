package com.example.todo_list.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.todo_list.R
import com.example.todo_list.data.model.Task
import com.example.todo_list.ui.screens.TaskContent
import com.example.todo_list.ui.screens.TaskHeaderRow
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OpenSansCondensed
import com.example.todo_list.ui.theme.TODOListTheme
import java.time.format.DateTimeFormatter

@Composable
fun TaskCard(
    task: Task,
    selected: Boolean,
    expanded: Boolean,
    onSelectedToggle: () -> Unit,
    onExpandedToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.roundedSize))
            .background(
                if (task.category != null)
                    task.category.Color.copy(alpha = 0.8f)
                else TODOListTheme.colors.taskWOCategory
            )
            .padding(horizontal = Dimens.smallPadding, vertical = Dimens.smallPadding)
    ) {
        Column {
            TopTaskInfo(task)
            Spacer(modifier = Modifier.size(Dimens.tinyPadding))
            TaskHeaderRow(
                task = task,
                selected = selected,
                expanded = expanded,
                onSelectToggle = { onSelectedToggle() },
                onExpandToggle = { onExpandedToggle() })
            TaskContent(task, expanded)
        }
    }
}

@Composable
fun TopTaskInfo(task: Task) {
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
                        task.creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy h:mma")),
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
                        task.destinationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy h:mma")),
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
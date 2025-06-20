package com.example.todo_list.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todo_list.R
import com.example.todo_list.data.model.Task
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
            if (task.category != null) Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.roundedSize))
                    .background(
                        task.category.Color
                            .copy(alpha = 0.9f)
                            .compositeOver(Color.Black.copy(alpha = 1f))
                    )
                    .padding(
                        horizontal = Dimens.mediumPadding,
                        vertical = Dimens.tinyPadding
                    )
            ) {
                Text(
                    task.category.Title,
                    color = TODOListTheme.colors.onTaskText,
                    fontFamily = OpenSansCondensed
                )
            }
        }
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

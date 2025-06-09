package com.example.todo_list.ui.screens

import TopBarPopUp
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.data.model.Task
import com.example.todo_list.notifications.NotificationScheduler
import com.example.todo_list.sharedPreferences.SharedPreferencesHelper
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.viewModel.TaskViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun TaskDialog(
    dismiss: () -> Unit,
    categoryList: List<Category>,
    viewModel: TaskViewModel,
    task: Task? = null,
    sharedPreferencesHelper: SharedPreferencesHelper
) {

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val context = LocalContext.current
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    var expanded by remember { mutableStateOf(false) }

    var notification by remember { mutableStateOf(task?.notification ?: false) }

    val attachments = remember { mutableStateListOf<Uri>() }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val dateTimePlaceholder =
        task?.destinationDate?.format(formatter) ?: "Click here to set"
    var dateTimeText by remember { mutableStateOf(dateTimePlaceholder) }

    LaunchedEffect(task?.categoryId) {
        task?.categoryId?.let { categoryId ->
            val category = viewModel.getCategoryById(categoryId)
            selectedCategory = category
        }
        task?.attachments?.forEach { uri -> attachments.add(uri) }
    }

    var selectedCategoryText = selectedCategory?.title ?: ""

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val mimeType = context.contentResolver.getType(uri)
                val extension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(mimeType) ?: "bin"

                val fileName =
                    "${uri.lastPathSegment?.substringAfterLast("/")}_${System.currentTimeMillis()}.$extension"
                val outputFile = File(context.filesDir, fileName)

                inputStream?.use { input ->
                    FileOutputStream(outputFile).use { output ->
                        input.copyTo(output)
                    }
                }

                val localUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    outputFile
                )

                attachments.add(localUri)

            } catch (e: Exception) {
                Toast.makeText(context, "Błąd podczas kopiowania załącznika", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



    Dialog(onDismissRequest = { dismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                TopBarPopUp(
                    text = if (task != null) "Edit Task" else "Add Task",
                    id = R.drawable.close,
                    action = {
                        attachments.forEach { uri ->
                            val file = File(context.filesDir, "${uri.lastPathSegment?.substringAfterLast("/")}")
                            if (file.exists()) {
                                file.delete()
                            }
                        }
                        dismiss()
                    }
                )

                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Title: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                BasicTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .fillMaxWidth()
                        .padding(vertical = Dimens.smallPadding, horizontal = Dimens.tinyPadding),
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = OswaldFontFamily,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Description",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )

                BasicTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = Dimens.smallPadding, horizontal = Dimens.tinyPadding),
                    value = description,
                    onValueChange = { description = it },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = OswaldFontFamily,
                        fontWeight = FontWeight.Light
                    )
                )
                Spacer(Modifier.size(Dimens.smallPadding))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Execution time:",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(Modifier.size(Dimens.smallPadding))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .fillMaxWidth()
                            .padding(
                                vertical = Dimens.smallPadding,
                                horizontal = Dimens.smallPadding
                            )
                    ) {
                        DateTimePickerSample({ dateTimeText = it }, text = dateTimeText)
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))



                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Category:",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(modifier = Modifier.size(Dimens.smallPadding))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .fillMaxWidth()
                            .padding(
                                vertical = Dimens.smallPadding,
                                horizontal = Dimens.smallPadding
                            )
                            .clickable { expanded = !expanded }
                    ) {
                        Column {
                            Text(
                                text = if (selectedCategoryText == "") "<None>" else selectedCategoryText
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("<None>") },
                                    onClick = {
                                        selectedCategoryText = ""
                                        expanded = false
                                        selectedCategory = null
                                    }
                                )

                                categoryList.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(text = category.title) },
                                        onClick = {
                                            selectedCategoryText = category.title
                                            selectedCategory = category
                                            expanded = false
                                        },
                                        modifier = Modifier.background(color = Color(category.colorLong))
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))



                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Notification:",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(Modifier.size(Dimens.smallPadding))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .size(30.dp)
                            .clickable { notification = !notification },
                        contentAlignment = Alignment.Center
                    ) {
                        if (notification) Image(
                            painter = painterResource(id = R.drawable.ok),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))


                Row {
                    Text(
                        "Attachments: ",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(modifier = Modifier.size(Dimens.tinyPadding))
                    Text(
                        "(${attachments.size}/3)",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily,
                        color = TODOListTheme.colors.attachment
                    )
                }
                Spacer(modifier = Modifier.size(Dimens.tinyPadding))

                attachments.forEach { uri ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    val file = File(context.filesDir, "${uri.lastPathSegment?.substringAfterLast("/")}")
                                    if (file.exists()) {
                                        file.delete()
                                    }
                                    attachments.remove(uri)
                                },
                            painter = painterResource(id = R.drawable.delete_red),
                            contentDescription = null
                        )

                        Text(
                            text = uri.lastPathSegment?.substringAfterLast("/")!!,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    val fileName =
                                        "${uri.lastPathSegment?.substringAfterLast("/")}"
                                    val outputFile = File(context.filesDir, fileName)

                                    val localUri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.provider",
                                        outputFile
                                    )

                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(
                                            localUri,
                                            context.contentResolver.getType(localUri)
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
                            painter = painterResource(id = R.drawable.open),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.size(Dimens.smallPadding))

                }
                if (attachments.size < 3) Row(
                    modifier = Modifier.clickable {
                        launcher.launch("*/*")
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.plus_attachment),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(Dimens.smallPadding))
                    Text(
                        "Add attachment ",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                }

                Spacer(modifier = Modifier.size(Dimens.smallPadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                if (title.isEmpty()) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Title of the task cannot be empty!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@clickable
                                }
                                if (dateTimeText == dateTimePlaceholder && task == null) {
                                    Toast
                                        .makeText(
                                            context,
                                            "You must set execution time!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    return@clickable
                                }
                                if (task == null) {
                                    val newTask = Task(
                                        isDone = false,
                                        title = title,
                                        description = description,
                                        creationDate = LocalDateTime.now(),
                                        destinationDate =
                                        LocalDateTime.parse(
                                            dateTimeText,
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        ),
                                        categoryId = if (selectedCategoryText == "") -1 else selectedCategory?.id,
                                        notification = notification,
                                        attachments = attachments
                                    )
                                    viewModel.addTask(newTask) { generatedId ->
                                        val taskWithId = newTask.copy(id = generatedId.toInt())
                                        if (notification) {
                                            NotificationScheduler(context, sharedPreferencesHelper).scheduleNotification(taskWithId)
                                        } else{
                                            NotificationScheduler(context, sharedPreferencesHelper).cancelNotification(taskWithId)
                                        }
                                        Log.d("My debug", "${taskWithId.id}")
                                    }
                                    Toast
                                        .makeText(
                                            context,
                                            "Task added successfully!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else {
                                    val updatedTask = task.copy(
                                        id = task.id,
                                        title = title,
                                        description = description,
                                        destinationDate = LocalDateTime.parse(
                                            dateTimeText,
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        ),
                                        categoryId = if (selectedCategoryText == "") -1 else selectedCategory?.id,
                                        notification = notification,
                                        attachments = attachments
                                    )
                                    viewModel.updateTask(updatedTask)
                                    Log.d("My debug", "${updatedTask.id}")
                                    if (notification) {
                                        NotificationScheduler(context, sharedPreferencesHelper).cancelNotification(updatedTask)
                                        NotificationScheduler(context, sharedPreferencesHelper).scheduleNotification(updatedTask)
                                    } else{
                                        NotificationScheduler(context, sharedPreferencesHelper).cancelNotification(updatedTask)
                                    }
                                    Toast
                                        .makeText(
                                            context,
                                            "Task edited successfully!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()

                                }
                                dismiss()
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = TODOListTheme.colors.saveButton)
                            .padding(
                                horizontal = Dimens.largePadding,
                                vertical = Dimens.tinyPadding
                            )

                    ) {
                        Text(
                            "SAVE",
                            fontSize = Dimens.fontSmall,
                            fontFamily = OswaldFontFamily,
                            color = TODOListTheme.colors.onButtons
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun DateTimePickerSample(onClick: (String) -> Unit, text: String) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var dateTimeText by remember { mutableStateOf(text) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    val formatted = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                    ).format(calendar.time)
                    dateTimeText = formatted
                    onClick(dateTimeText)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Text(text = dateTimeText, modifier = Modifier
        .clickable {
            datePickerDialog.show()
        })

}

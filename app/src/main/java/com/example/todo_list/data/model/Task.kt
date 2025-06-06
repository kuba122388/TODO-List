package com.example.todo_list.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val creationDate: LocalDateTime,
    val destinationDate: LocalDateTime,
    val categoryId: Int?,
    val notification: Boolean,
    val attachments: List<Uri>
)

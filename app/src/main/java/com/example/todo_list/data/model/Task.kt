package com.example.todo_list.data.model

import java.time.LocalDateTime

data class Task(
    val title: String,
    val description: String,
    val creationDate: LocalDateTime,
    val destinationDate: LocalDateTime,
    val category: Category?,
    val notification: Boolean,
    val attachments: List<String>
)

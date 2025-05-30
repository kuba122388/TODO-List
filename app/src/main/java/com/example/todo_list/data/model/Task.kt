package com.example.todo_list.data.model

import java.time.LocalDate

data class Task(
    val title: String,
    val description: String,
    val creationDate: LocalDate,
    val destinationDate: LocalDate,
    val category: Category?,
    val notification: Boolean,
    val attachments: List<String>
)

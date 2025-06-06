package com.example.todo_list.data.converters

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.example.todo_list.data.model.Category
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromUriList(uris: List<Uri>): String {
        return uris.joinToString(separator = ",") { it.toString() }
    }

    @TypeConverter
    fun toUriList(data: String): List<Uri> {
        return if (data.isEmpty()) emptyList()
        else data.split(",").map { Uri.parse(it) }
    }

}

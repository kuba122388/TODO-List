package com.example.todo_list.sharedPreferences

import android.content.Context

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun setUserName(newName: String){
        sharedPreferences.edit().putString("user_name", newName).apply()
    }

    fun getUserName(): String {
        val name = sharedPreferences.getString("user_name", "Mysterious") ?: "Mysterious"
        return name
    }

    fun setHideDoneTask(hidden: Boolean){
        sharedPreferences.edit().putBoolean("hidden_tasks", hidden).apply()
    }

    fun getHideDoneTask() : Boolean{
        val hidden = sharedPreferences.getBoolean("hidden_tasks", false)
        return hidden
    }

    fun getFullHourFormat(fullHour: Boolean){
        sharedPreferences.edit().putBoolean("full_hour", fullHour).apply()
    }

    fun getFullHourFormat() : Boolean {
        val engFormat = sharedPreferences.getBoolean("full_hour", false)
        return engFormat
    }

    fun setNotificationTime(minutesBefore: String) {
        sharedPreferences.edit().putInt("notification_minutes", minutesBefore.toInt()).apply()
    }

    fun getNotificationTime() : String {
        val minutesBefore = sharedPreferences.getInt("notification_minutes", 30)
        return minutesBefore.toString()
    }
}
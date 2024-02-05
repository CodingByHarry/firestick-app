package com.example.bobjimmy.ui.theme

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        with(sharedPreferences.edit()) {
            putString("username", user.username)
            apply()
        }
    }

    fun getUser(): User {
        val name = sharedPreferences.getString("username", "") ?: ""
        return User(name)
    }
}

// SessionManager.kt
package com.example.employeesmanagementsys

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        const val KEY_USER_NAME = "userName"
        const val KEY_USER_ID = "userId"
        const val KEY_USER_PASSWORD = "userPassword"
        const val KEY_USER_EMAIL = "userEmail"
        const val KEY_USER_ROLE = "userRole" // Add the new key for user role
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "") ?: ""
    }

    fun setUserName(userName: String) {
        editor.putString(KEY_USER_NAME, userName)
        editor.apply()
    }

    fun setUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    }

    fun setUserPassword(userPassword: String) {
        editor.putString(KEY_USER_PASSWORD, userPassword)
        editor.apply()
    }

    fun getUserPassword(): String {
        return sharedPreferences.getString(KEY_USER_PASSWORD, "") ?: ""
    }

    fun setUserEmail(userEmail: String) {
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.apply()
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""
    }

    // New function to set the user role
    fun setUserRole(userRole: String) {
        editor.putString(KEY_USER_ROLE, userRole)
        editor.apply()
    }

    // New function to get the user role
    fun getUserRole(): String {
        return sharedPreferences.getString(KEY_USER_ROLE, "") ?: ""
    }
}
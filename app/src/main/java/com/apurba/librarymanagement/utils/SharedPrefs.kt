package com.apurba.librarymanagement.utils

import android.content.Context
import android.content.SharedPreferences
import com.apurba.librarymanagement.models.User
import androidx.core.content.edit
import com.apurba.librarymanagement.models.Member

object SharedPrefs {
    
    private const val PREF_NAME = "LibraryAppPrefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_MEMEBER_ID = "member_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_ROLE = "role"
    private const val KEY_LAST_LOGIN = "last_login"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveUser(context: Context, user: User) {
        val prefs = getPrefs(context)
        prefs.edit() {
            apply {
                putInt(KEY_USER_ID, user.id)
                putString(KEY_USERNAME, user.username)
                putString(KEY_EMAIL, user.email)
                putInt(KEY_ROLE, user.role)
                putString(KEY_LAST_LOGIN, user.lastLoginDate)
                putBoolean(KEY_IS_LOGGED_IN, true)
            }
        }
    }

    fun saveMember(context: Context, memberId: Int) {

        val prefs = getPrefs(context)
        prefs.edit() {
            apply {
                putInt(KEY_MEMEBER_ID, memberId)
            }
        }
    }
    
    fun getUser(context: Context): User? {
        val prefs = getPrefs(context)
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        
        if (!isLoggedIn) return null
        
        return User(
            id = prefs.getInt(KEY_USER_ID, 0),
            username = prefs.getString(KEY_USERNAME, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            role = prefs.getInt(KEY_ROLE, 0),
            lastLoginDate = prefs.getString(KEY_LAST_LOGIN, null)
        )
    }
    
    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun clearUser(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().clear().apply()
    }
    
    fun getUsername(context: Context): String {
        return getPrefs(context).getString(KEY_USERNAME, "") ?: ""
    }

    fun getMemberId(context: Context): Int {
        return getPrefs(context).getInt(KEY_MEMEBER_ID, 0)
    }
    
    fun getUserRole(context: Context): Int {
        return getPrefs(context).getInt(KEY_ROLE, 0)
    }
} 
package com.example.finflow.data

import android.content.Context
import android.content.SharedPreferences
import com.example.finflow.model.Budget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREFERENCES_NAME = "finance_tracker_prefs"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_BUDGET = "budget"
        private const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
        private const val KEY_REMINDER_ENABLED = "reminder_enabled"
    }

    fun setCurrency(currency: String) {
        sharedPreferences.edit() { putString(KEY_CURRENCY, currency) }
    }

    fun getCurrency(): String {
        return sharedPreferences.getString(KEY_CURRENCY, "USD") ?: "USD"
    }

    fun setBudget(budget: Budget) {
        val budgetJson = gson.toJson(budget.toMap())
        sharedPreferences.edit() { putString(KEY_BUDGET, budgetJson) }
    }

    fun getBudget(): Budget {
        val budgetJson = sharedPreferences.getString(KEY_BUDGET, null)
        return if (budgetJson != null) {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val budgetMap: Map<String, Any> = gson.fromJson(budgetJson, type)
            Budget.fromMap(budgetMap)
        } else {
            Budget(0.0)
        }
    }

    fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit() { putBoolean(KEY_NOTIFICATION_ENABLED, enabled) }
    }

    fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true)
    }

    fun setReminderEnabled(enabled: Boolean) {
        sharedPreferences.edit() { putBoolean(KEY_REMINDER_ENABLED, enabled) }
    }

    fun isReminderEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_REMINDER_ENABLED, false)
    }
}

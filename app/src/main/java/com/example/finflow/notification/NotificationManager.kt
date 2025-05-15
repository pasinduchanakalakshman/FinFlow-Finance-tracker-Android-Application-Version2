package com.example.finflow.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.finflow.R
import com.example.finflow.data.PreferencesManager
import com.example.finflow.data.TransactionRepository
import com.example.finflow.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class NotificationManager(private val context: Context) {
    private val notificationManagerCompat = NotificationManagerCompat.from(context)
    private val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val preferencesManager = PreferencesManager(context)
    private val transactionRepository = TransactionRepository(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val CHANNEL_ID_BUDGET = "budget_channel"
        private const val CHANNEL_ID_REMINDER = "reminder_channel"
        private const val NOTIFICATION_ID_BUDGET = 1001
        private const val NOTIFICATION_ID_REMINDER = 1002
        private const val REMINDER_REQUEST_CODE = 2001
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val budgetChannel = NotificationChannel(
                CHANNEL_ID_BUDGET,
                "Budget Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for budget alerts"
                enableLights(true)
                enableVibration(true)
            }

            val reminderChannel = NotificationChannel(
                CHANNEL_ID_REMINDER,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily reminders to record expenses"
                enableLights(true)
                enableVibration(true)
            }

            systemNotificationManager.createNotificationChannel(budgetChannel)
            systemNotificationManager.createNotificationChannel(reminderChannel)
        }
    }

    fun checkBudgetAndNotify() {
        if (!preferencesManager.isNotificationEnabled()) return

        coroutineScope.launch {
            val budget = preferencesManager.getBudget()
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            if (budget.month == currentMonth && budget.year == currentYear && budget.amount > 0) {
                val totalExpenses = transactionRepository.getTotalExpensesForMonth(currentMonth, currentYear)
                val budgetPercentage = (totalExpenses / budget.amount) * 100

                // Log for debugging
                android.util.Log.d("NotificationManager", "Budget check: $totalExpenses / ${budget.amount} = $budgetPercentage%")

                if (budgetPercentage >= 90) {
                    showBudgetNotification(budgetPercentage, budget.amount, totalExpenses)
                }
            }
        }
    }

    private fun showBudgetNotification(percentage: Double, budgetAmount: Double, spentAmount: Double) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val message = if (percentage >= 100) {
            "You've exceeded your monthly budget of ${preferencesManager.getCurrency()} $budgetAmount"
        } else {
            "You're at ${percentage.toInt()}% of your monthly budget (${preferencesManager.getCurrency()} $spentAmount / $budgetAmount)"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_BUDGET)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Budget Alert")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Log for debugging
        android.util.Log.d("NotificationManager", "Showing notification: $message")

        try {
            notificationManagerCompat.notify(NOTIFICATION_ID_BUDGET, notification)
        } catch (e: SecurityException) {
            android.util.Log.e("NotificationManager", "Failed to show notification: ${e.message}")
        }
    }

    fun scheduleDailyReminder() {
        if (!preferencesManager.isReminderEnabled()) {
            cancelDailyReminder()
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, REMINDER_REQUEST_CODE, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        // Set reminder for 8 PM every day
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // If it's already past 8 PM, schedule for tomorrow
            if (Calendar.getInstance().after(this)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                } else {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            android.util.Log.d("NotificationManager", "Daily reminder scheduled for ${calendar.time}")
        } catch (e: SecurityException) {
            android.util.Log.e("NotificationManager", "Failed to schedule reminder: ${e.message}")
        }
    }

    fun cancelDailyReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, REMINDER_REQUEST_CODE, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        try {
            alarmManager.cancel(pendingIntent)
            android.util.Log.d("NotificationManager", "Daily reminder cancelled")
        } catch (e: SecurityException) {
            android.util.Log.e("NotificationManager", "Failed to cancel reminder: ${e.message}")
        }
    }

    fun showReminderNotification() {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Daily Reminder")
            .setContentText("Don't forget to record your expenses today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManagerCompat.notify(NOTIFICATION_ID_REMINDER, notification)
            android.util.Log.d("NotificationManager", "Reminder notification shown")
        } catch (e: SecurityException) {
            android.util.Log.e("NotificationManager", "Failed to show reminder: ${e.message}")
        }
    }
}
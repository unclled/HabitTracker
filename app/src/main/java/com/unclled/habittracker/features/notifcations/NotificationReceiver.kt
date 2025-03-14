package com.unclled.habittracker.features.notifcations

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.unclled.habittracker.features.database.HabitRepository
import com.unclled.habittracker.features.database.HabitsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        val db = HabitsDatabase.getInstance(context!!)
        val repository = HabitRepository(db.getHabitDao())
        CoroutineScope(Dispatchers.IO).launch {
            val hasEntries = repository.getAnyHabit().isNotEmpty()

            val notificationMessage = if (hasEntries) {
                "Пора выполнить вашу цель!"
            } else {
                "Пора поставить перед собой новую цель!"
            }

            val notification = BasicNotification(
                channelId = "general_channel",
                title = "Напоминание",
                content = notificationMessage,
                priority = NotificationCompat.PRIORITY_DEFAULT
            )

            NotificationFactory(context).showNotification(1, notification)
        }
    }
}
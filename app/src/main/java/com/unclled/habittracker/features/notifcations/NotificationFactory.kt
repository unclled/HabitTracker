package com.unclled.habittracker.features.notifcations

import android.Manifest
import android.app.Notification
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.unclled.habittracker.R
import javax.inject.Inject

class NotificationFactory @Inject constructor(val context: Context) {

    private fun createNotification(model: SkeletalNotification): Notification {
        return NotificationCompat.Builder(context, model.channelId)
            .setSmallIcon(R.drawable.kitten)
            .setContentTitle(model.title)
            .setContentText(model.content)
            .setPriority(model.priority)
            .setAutoCancel(true)
            .apply {
                model.actions.forEach { (iconId, title, actionIntent) ->
                    addAction(iconId, title, actionIntent)
                }
            }
            .build()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(id: Int, notification: SkeletalNotification) {
        NotificationManagerCompat
            .from(context)
            .notify(id, createNotification(notification))
    }
}
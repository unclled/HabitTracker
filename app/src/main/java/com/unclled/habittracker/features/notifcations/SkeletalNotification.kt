package com.unclled.habittracker.features.notifcations

import android.app.PendingIntent

sealed class SkeletalNotification(
    val channelId: String,
    val title: String,
    val content: String,
    val priority: Int,
    val actions: MutableList<NotificationAction> = mutableListOf()
)

class BasicNotification(
    channelId: String,
    title: String,
    content: String,
    priority: Int
) : SkeletalNotification(channelId, title, content, priority)

data class NotificationAction(
    val iconId: Int,
    val title: String,
    val actionIntent: PendingIntent
)
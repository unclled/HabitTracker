package com.unclled.habittracker.features.notifcations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.unclled.habittracker.R

class NotificationChannelFactory {
    private val notificationChannel: MutableList<NotificationChannel> = arrayListOf()

    fun createChannel(channelInfo: NotificationChannelInfo): NotificationChannel =
        NotificationChannel(channelInfo.id, channelInfo.name, channelInfo.priority).apply {
            this.description = channelInfo.description
        }

    fun init(context: Context) {
        val generalChannel = createChannel(
            NotificationChannelInfo(
                context.getString(R.string.notifications_general_channel_id),
                context.getString(R.string.notifications_general_channel),
                context.getString(R.string.notifications_general_channel_description),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(generalChannel)
        notificationChannel.add(generalChannel)
    }

    data class NotificationChannelInfo(
        val id: String,
        val name: String,
        val description: String,
        val priority: Int
    )
}

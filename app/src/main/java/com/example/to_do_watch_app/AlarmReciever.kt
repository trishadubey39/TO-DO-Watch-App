package com.example.to_do_watch_app
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.to_do_watch_app.viewmodel.ALARM_ID
import com.example.to_do_watch_app.viewmodel.CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context, "Alarm", "Time to wake up!")
    }

    private fun showNotification(context: Context, title: String, message: String) {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(ALARM_ID, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Alarm Channel"
        val description = "Channel for alarm notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}

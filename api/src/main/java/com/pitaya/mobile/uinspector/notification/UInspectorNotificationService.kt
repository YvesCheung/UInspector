package com.pitaya.mobile.uinspector.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat
import com.pitaya.mobile.uinspector.R
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.state.UInspectorState
import com.pitaya.mobile.uinspector.util.LibName

/**
 * @author YvesCheung
 * 2020/12/29
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class UInspectorNotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val currentState = UInspector.currentState.isRunning
        val pendingState = intent?.getBooleanExtra(PENDING_RUNNING, currentState) ?: currentState

        startForeground(notificationId, createNotification(pendingState))

        UInspector.changeStateInner(pendingState)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    private fun createNotification(isRunning: Boolean): Notification {
        val notificationBuilder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    LibName,
                    getString(R.string.uinspector_notification_channel_name),
                    IMPORTANCE_HIGH
                )
                NotificationManagerCompat.from(this).createNotificationChannel(channel)

                NotificationCompat.Builder(this, channel.id)
            } else {
                NotificationCompat.Builder(this)
            }

        val app = applicationInfo
        val appName = packageManager.getApplicationLabel(applicationInfo)
        val title = getString(
            if (isRunning) R.string.uinspector_notification_channel_stop_title
            else R.string.uinspector_notification_channel_start_title,
            appName
        )
        val info = getString(
            if (isRunning) R.string.uinspector_notification_channel_stop_message
            else R.string.uinspector_notification_channel_start_message
        )
        val view = createView(
            title, info,
            if (isRunning) R.drawable.notification_stop
            else R.drawable.notification_start
        )
        return notificationBuilder
            .setSmallIcon(app.icon)
            .setContentTitle(title)
            .setContentInfo(info)
            .setCustomContentView(view)
            .setContent(view)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(
                PendingIntent.getService(
                    this, 0,
                    Intent(this, UInspectorNotificationService::class.java)
                        .putExtra(PENDING_RUNNING, !isRunning),
                    FLAG_ONE_SHOT or FLAG_UPDATE_CURRENT
                )
            )
            .setPriority(PRIORITY_HIGH)
            .setNotificationSilent()
            .build()
    }

    private fun createView(
        title: String,
        info: String,
        @DrawableRes icon: Int
    ): RemoteViews {
        val root = RemoteViews(packageName, R.layout.uinspector_notification_channel_view)
        root.setTextViewText(R.id.notification_title, title)
        root.setTextViewText(R.id.notification_info, info)
        root.setImageViewResource(R.id.notification_btn, icon)
        return root
    }

    companion object {

        /**
         * To toggle the value of [UInspectorState.isRunning]
         */
        const val PENDING_RUNNING = "pending_start"

        private const val notificationId = 23333
    }
}
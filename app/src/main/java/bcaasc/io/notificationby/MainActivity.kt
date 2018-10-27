package bcaasc.io.notificationby

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.NotificationManager
import android.os.Build
import android.app.NotificationChannel
import android.annotation.TargetApi
import android.app.Notification
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.Toast
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.provider.Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
import android.content.Intent
import android.provider.Settings


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channelId = "chat"
            var channelName = "聊天消息"
            var importance = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)
            channelId = "subscribe"
            channelName = "订阅消息"
            importance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance)
        }
        btn_chat.setOnClickListener { sendChatMsg() }
        btn_subscribe.setOnClickListener { sendSubscribeMsg() }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    fun sendChatMsg() {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel("chat")
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                startActivity(intent)
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show()
            }
        }
//        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "chat")
            .setContentTitle("收到一条聊天消息")
            .setContentText("今天中午吃什么？")
            .setWhen(System.currentTimeMillis())
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setTicker("BCAASCWALLET")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
            .setAutoCancel(true)
            .build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        manager.notify(1, notification)
    }

    fun sendSubscribeMsg() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "subscribe")
            .setContentTitle("收到一条订阅消息")
            .setContentText("地铁沿线30万商铺抢购中！")
            .setWhen(System.currentTimeMillis())
            .setTicker("BCAASCWALLET")
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
            .setAutoCancel(true)
            .build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        manager.notify(2, notification)
    }
}

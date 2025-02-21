package uz.polat.broadcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ReceiverRegisterService : Service() {
    private val channelId = "my_service"
    private val channelName = "My Background Service"
    private val notificationID = 101
    override fun onBind(intent: Intent?): IBinder? = null
    private val broadcast = AppBroadcast()
    override fun onCreate() {
        super.onCreate()
//        val actionPowerConnected = IntentFilter(Intent.ACTION_POWER_CONNECTED)
//        val actionPowerDisconnected = IntentFilter(Intent.ACTION_POWER_DISCONNECTED)
//        val actionScreenOn = IntentFilter(Intent.ACTION_SCREEN_ON)
//        val actionScreenOff = IntentFilter(Intent.ACTION_SCREEN_OFF)
//        val actionAirPlane = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
//        val headSet = IntentFilter(Intent.ACTION_HEADSET_PLUG)
//        val batteryLow = IntentFilter(Intent.ACTION_BATTERY_LOW)
//        val timeTick = IntentFilter(Intent.ACTION_TIME_TICK)
////        val actionCustomBroadcast = IntentFilter("uz.gita.broadcast.CUSTOM_RECEIVER")
//        //receiverFlag - bu agar broadcast boshqa applar orqali kelish yoki kelmasligini bildiradi
//        val receiverFlag = ContextCompat.RECEIVER_EXPORTED

        val all = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_HEADSET_PLUG)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_TIME_TICK)
            addAction( BluetoothAdapter.ACTION_STATE_CHANGED)
        }

        registerReceiver(broadcast,all)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dismissIntent = Intent(this, NotificationDismissedReceiver::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Maxmadona")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setDeleteIntent(dismissPendingIntent) // Handle notification removal
            .build()

        startForeground(notificationID, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_HIGH
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}
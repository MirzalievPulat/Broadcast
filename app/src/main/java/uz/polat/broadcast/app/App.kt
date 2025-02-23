package uz.polat.broadcast.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.speech.tts.TextToSpeech
import uz.polat.broadcast.NetworkStatusValidator
import uz.polat.broadcast.repository.LocalStorage
import uz.polat.broadcast.speakText
import java.util.Locale

class App : Application() {
    companion object {
        var tts: TextToSpeech? = null
        val channelId = "my_service"
        val channelName = "Bildirishnomalar"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val localeStorage = LocalStorage.getInstance(this)
        tts = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.UK
            }
        }

        NetworkStatusValidator.listenNetworkStatus(this, {
            if (localeStorage.networkMode)
                speakText("Internet is on")
        }, {
            if (localeStorage.networkMode)
                speakText("Internet is off")
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                channelId,
                channelName, NotificationManager.IMPORTANCE_LOW//here1
            )
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
        }
    }
}

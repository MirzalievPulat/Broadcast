package uz.polat.broadcast.app

import android.app.Application
import android.speech.tts.TextToSpeech
import uz.polat.broadcast.NetworkStatusValidator
import uz.polat.broadcast.repository.LocalStorage
import uz.polat.broadcast.speakText
import java.util.Locale

class App : Application() {
    companion object {
        var tts: TextToSpeech? = null
    }

    override fun onCreate() {
        super.onCreate()
        val localeStorage = LocalStorage.getInstance(this)
        tts = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.UK
            }
        }

        NetworkStatusValidator.listenNetworkStatus(this,{
            if (localeStorage.networkMode)
                speakText("Internet is on")
        },{
            if (localeStorage.networkMode)
                speakText("Internet is off")
        })
    }
}

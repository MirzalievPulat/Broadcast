package uz.polat.broadcast

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import uz.polat.broadcast.app.App
import uz.polat.broadcast.repository.LocalStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AppBroadcast : BroadcastReceiver() {
    private lateinit var localStorage: LocalStorage


    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("TAG", "onReceive: ${intent?.action}")
        localStorage = LocalStorage.getInstance(context!!)


        when (intent?.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                if (localStorage.airPlaneMode) {
                    val isTurnedOn = intent.getBooleanExtra("state", false)
                    if (isTurnedOn) speakText("Airplane mode turned on")
                    else speakText("Airplane mode turned off")
                }
            }

            Intent.ACTION_POWER_CONNECTED -> {
                if (localStorage.powerMode)
                    speakText("Power connected")
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                if (localStorage.powerMode)
                    speakText("Power disconnected")
            }

            Intent.ACTION_SCREEN_ON -> {
                if (localStorage.screenMode)
                    speakText("Screen on")
            }

            Intent.ACTION_SCREEN_OFF -> {
                if (localStorage.screenMode)
                    speakText("Screen off")
            }

            Intent.ACTION_BATTERY_LOW ->{
                if (localStorage.lowBatteryMode)
                    speakText("Battery low")
            }

            Intent.ACTION_HEADSET_PLUG ->{
                if (localStorage.headSetMode){
                    val state = intent.getIntExtra("state", -1)
                    when (state) {
                        1 -> speakText("Headset connected")   // Plugged in
                        0 -> speakText("Headset disconnected") // Unplugged
                        else -> Log.d("TAG", "Invalid headset state: $state") // Ignore unknown states
                    }
                }
            }

            Intent.ACTION_TIME_TICK ->{
                if (localStorage.timeMode){
                    println("kirdi1")
                    speakText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
                    println("kirdi last")
                }
            }

            ACTION_STATE_CHANGED->{
                Log.d("TAG", "onReceive: blueTooth")
                if (localStorage.blueToothMode){
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> {
                            speakText("Bluetooth is offf")
                        }
                        BluetoothAdapter.STATE_ON -> {
                            speakText("Bluetooth is on")
                        }
                    }
                }
            }


        }
    }



}
private var last = ""

fun speakText(text: String) {
    if (last != text){
        val tts = App.tts
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "")
        last = text
    }
}


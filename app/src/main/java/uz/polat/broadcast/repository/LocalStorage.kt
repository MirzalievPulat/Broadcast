package uz.polat.broadcast.repository

import android.content.Context


class LocalStorage(context: Context) : SharedPreference(context) {

    companion object {
        private var instance: LocalStorage? = null

        fun getInstance(context: Context): LocalStorage {
            if (instance == null) {
                instance = LocalStorage(context)
            }
            return instance!!
        }
    }

    var airPlaneMode: Boolean by booleans()
    var screenMode: Boolean by booleans()
    var powerMode: Boolean by booleans()
    var lowBatteryMode: Boolean by booleans()
    var headSetMode: Boolean by booleans()
    var timeMode: Boolean by booleans()
    var blueToothMode: Boolean by booleans()
    var networkMode: Boolean by booleans()

}


package playground.develop.fdelivery.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object Extensions {

    fun short(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun long(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun debug(TAG: String, message: String) {
        Log.d(TAG, message)
    }
}
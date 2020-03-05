package com.juliushekkala.juliusmobilecomp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import org.jetbrains.anko.doAsync

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val geofencingTransition = geofencingEvent.geofenceTransition

        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            var uid = intent!!.getIntExtra("uid", 0)
            var text = intent.getStringExtra("message")

            MainActivity.showNotification(context!!, text)

            //Removal
            doAsync {
                val db = Room.databaseBuilder(context, AppDatabase::class.java, "reminders").build()
                db.reminderDao().delete(uid)
                db.close()
            }

        }
    }
}
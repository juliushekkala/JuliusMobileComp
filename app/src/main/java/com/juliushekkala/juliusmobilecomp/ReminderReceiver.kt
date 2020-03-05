package com.juliushekkala.juliusmobilecomp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import org.jetbrains.anko.doAsync

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val text = intent.getStringExtra("message")
        val uid = intent.getIntExtra("uid", 0)

        //context.toast(text!!)

        MainActivity.showNotification(context,text!!)

        //Removal
       doAsync {
           val db = Room.databaseBuilder(context, AppDatabase::class.java, "reminders").build()
           db.reminderDao().delete(uid)
           db.close()
        }
    }
}
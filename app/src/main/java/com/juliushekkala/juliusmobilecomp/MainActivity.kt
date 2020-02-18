package com.juliushekkala.juliusmobilecomp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab_time.setOnClickListener {
            val intent = Intent(applicationContext, TimeActivity::class.java)
            startActivity(intent)
        }

        fab_map.setOnClickListener {
            val intent = Intent(applicationContext, MapActivity::class.java)
            startActivity(intent)
        }

        var fabOpened = false
        fab.setOnClickListener {
            if (!fabOpened) {
                fabOpened = true
                fab_time.animate().translationY(-resources.getDimension(R.dimen.standard_66))
                fab_map.animate().translationY(-resources.getDimension(R.dimen.standard_116))
            } else {
                fabOpened = false
                fab_time.animate().translationY(0f)
                fab_map.animate().translationY(0f)
            }

        }





    }

    override fun onResume() {
        super.onResume()

        refreshList()

    }


    private fun refreshList() {
        doAsync {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders").build()
            val reminders = db.reminderDao().getReminders()
            db.close()

            uiThread {

                if (!reminders.isEmpty()) {
                    val adapter = ReminderAdapter(applicationContext, reminders)
                    list.adapter = adapter
                } else {
                    toast("Impossible to show reminders")
                }


            }
        }
    }
    companion object {
        val CHANNEL_ID="REMINDER_CHANNEL_ID"
        var NotificationID = 1567
        fun showNotification(context: Context, message:String) {
            var notificationBuilder=NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm).setContentTitle(context.getString(R.string.app_name))
                .setContentText(message).setStyle(NotificationCompat.BigTextStyle().bigText(message)).setPriority(NotificationCompat.PRIORITY_DEFAULT)
            var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description=context.getString(R.string.app_name)
                }
                notificationManager.createNotificationChannel(channel)
            }
            val notification=NotificationID+ Random(NotificationID).nextInt(1, 30)
            notificationManager.notify(notification, notificationBuilder.build())
        }
    }
}

package com.juliushekkala.juliusmobilecomp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_time.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*

class TimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        time_create.setOnClickListener {


            val calendar = GregorianCalendar (
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth,
                timePicker.currentHour,
                timePicker.currentMinute
            )

            if (et_message.text.toString() != "" && calendar.timeInMillis > System.currentTimeMillis()) {
                val reminder = Reminder (
                    uid = null,
                    time = calendar.timeInMillis,
                    location = null,
                    message = et_message.text.toString()
                )

                doAsync {
                    val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders").build()
                    val id = db.reminderDao().insert(reminder)
                    db.close()

                    setAlarm(reminder.time!!, reminder.message, id.toInt())

                    finish()
                }
            } else {
                toast("Impossible")
            }






        }
    }


    private fun setAlarm(time: Long, message: String, id: Int) {

        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("message", message)

        intent.putExtra("uid", id)

        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExact(AlarmManager.RTC, time, pendingIntent)

        runOnUiThread{(toast("Reminder is created"))}
   }
}

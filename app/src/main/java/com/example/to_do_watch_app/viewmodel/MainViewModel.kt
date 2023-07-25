package com.example.to_do_watch_app.viewmodel
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do_watch_app.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

const val ALARM_ID = 1
const val CHANNEL_ID = "AlarmChannel"

class MainViewModel : ViewModel() {
    private val _alarmTime = MutableStateFlow<Pair<Int, Int>?>(null)
    val alarmTime: StateFlow<Pair<Int, Int>?> get() = _alarmTime

    fun setAlarmTime(hour: Int, minute: Int) {
        _alarmTime.value = Pair(hour, minute)
    }

    fun setAlarm(context: Context) {
        val alarmTimeValue = alarmTime.value
        if (alarmTimeValue != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val (hourOfDay, minute) = alarmTimeValue
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    ALARM_ID,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Check if the time has already passed, if so, set the alarm for the next day
                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }

                // Set the alarm
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }
}

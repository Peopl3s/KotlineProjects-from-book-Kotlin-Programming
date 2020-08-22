package android.bignerdranch.com.samodelkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


private const val CHARACTER_DATA_KEY = "EVENT_DATA_KEY"

private var Bundle.eventData
    get() = getSerializable(CHARACTER_DATA_KEY) as EventData
    set(value) = putSerializable(CHARACTER_DATA_KEY, value)


class MainActivity : AppCompatActivity() {
    private var eventData = EventData()
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.eventData = eventData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventData = savedInstanceState?.eventData?: EventData()

        startButton.setOnClickListener {
            eventData.descriptionEvent = nevent.text.toString()
            val spinner: Spinner = findViewById(R.id.spinner)
            eventData.periodEvent = spinner.selectedItem.toString()
            displayEventData()

            alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, 0)
            }
            alarmMgr?.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60 * 1000,
                alarmIntent
            )


        }
    }

    private fun displayEventData(){
        eventData.run{
            task.text = descriptionEvent
            val spinner: Spinner = findViewById(R.id.spinner)
            spinner.setSelection(when(periodEvent){
                "1 minutes" -> 0
                "5 minutes" -> 1
                "30 minutes" -> 2
                "60 minutes" -> 3
                "120 minutes" -> 4
                else -> 0
            })

        }
    }
}

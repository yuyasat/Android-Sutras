package yuyasat.listview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val timeZones = TimeZone.getAvailableIDs()

    val listView = findViewById<ListView>(R.id.timeZoneList)

    val adapter = ArrayAdapter<String>(this,
        R.layout.list_time_zone_row,
        R.id.timeZone,
        timeZones)
    listView.adapter = adapter

    listView.setOnItemClickListener { parent, view, position, id ->
      val  timeZone = adapter.getItem(position)
      Toast.makeText(this, timeZone, Toast.LENGTH_SHORT).show()
    }
  }
}

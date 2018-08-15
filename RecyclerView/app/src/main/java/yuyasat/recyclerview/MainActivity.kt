package yuyasat.recyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Layout
import android.view.LayoutInflater
import android.widget.Toast
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val recyclerView = findViewById<RecyclerView>(R.id.timeZoneList)

    /*
    val f = fun (timeZone : TimeZone) {
      Toast.makeText(this, timeZone.displayName, Toast.LENGTH_SHORT).show()
    }
    val adapter = SampleAdapter(this, f)
    */
    val adapter = SampleAdapter(this) { timeZone ->
      Toast.makeText(this, timeZone.displayName, Toast.LENGTH_SHORT).show()
    }

    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(
        this, LinearLayoutManager.VERTICAL, false
    )
    // recyclerView.layoutManager = GridLayoutManager(this, 3)
  }
}

class SampleAdapter(context: Context, private val onItemClicked: (TimeZone) -> Unit)
  : RecyclerView.Adapter<SampleAdapter.SampleViewHolder>() {
  private val inflater = LayoutInflater.from(context)

  private val timeZones = TimeZone.getAvailableIDs().map {
    id -> TimeZone.getTimeZone(id)
  }

  override fun getItemCount() = timeZones.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
    val view = inflater.inflate(R.layout.list_time_zone_row, parent, false)

    val viewHolder = SampleViewHolder(view)

    view.setOnClickListener {
      val position = viewHolder.adapterPosition
      val timeZone = timeZones[position]

      onItemClicked(timeZone)
    }

    return viewHolder
  }


  override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
    val timeZone = timeZones[position]
    holder.name.text = timeZone.id
  }

  class SampleViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.timeZone)
  }
}

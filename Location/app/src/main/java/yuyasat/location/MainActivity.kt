package yuyasat.location

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
  private var currentDate = Calendar.getInstance()
  private lateinit var googleMap : GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState != null &&
        savedInstanceState.containsKey("currentDate")) {
      currentDate = savedInstanceState.getSerializable("currentDate") as Calendar
    }

    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync {
      googleMap = it
      renderMap()
    }

    showCurrentDate()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 1) {
      val switchFragment =
          supportFragmentManager.findFragmentById(R.id.switch_fragment)
      switchFragment.onActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    currentDate.set(year, month, dayOfMonth)
    renderMap()
    showCurrentDate()
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    super.onSaveInstanceState(outState, outPersistentState)
    outState?.putSerializable("currentDate", currentDate)
  }

  private fun renderMap() {
    val locations = selectInDay(this,
        currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE])

    zoomTo(googleMap, locations)
    putMarkers(googleMap, locations)
  }

  private fun zoomTo(map: GoogleMap, locations: List<LocationRecord>) {
    if (locations.size == 1) {
      val latLng = LatLng(locations[0].latitude, locations[0].longitude)

      val move = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
      map.moveCamera(move)
    } else if (locations.size > 1) {
      val bounds = LatLngBounds.Builder()
      locations.forEach { location ->
        bounds.include(LatLng(location.latitude, location.longitude))
      }
      val padding = (50 * resources.displayMetrics.density).toInt()
      val move = CameraUpdateFactory.newLatLngBounds(bounds.build(),
          resources.displayMetrics.widthPixels,
          resources.displayMetrics.heightPixels,
          padding)
      map.moveCamera(move)
    }
  }

  private fun putMarkers(map: GoogleMap, locations: List<LocationRecord>) {
    map.clear()

    // 複数の直線を描画させるためのオブジェクト
    val lineOptions = PolylineOptions()

    locations.forEach { location ->
      val latLng = LatLng(location.latitude, location.longitude)
      lineOptions.add(latLng)

      val marker = MarkerOptions()
          .position(latLng)
          .draggable(false)
      val descriptor = BitmapDescriptorFactory.defaultMarker(
          BitmapDescriptorFactory.HUE_BLUE
      )
      marker.icon(descriptor)
    }

    map.addPolyline(lineOptions)
  }

  private fun showCurrentDate() {
    val dateView = findViewById<TextView>(R.id.date)

    dateView.setOnClickListener {
      val dialog = DatePickerFragment()
      dialog.arguments = Bundle().apply {
        putSerializable("current", currentDate)
      }
      dialog.show(supportFragmentManager, "calendar")
    }

    dateView.setText(DateFormat.format("MM月 dd日", currentDate.time))
  }
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
  private lateinit var calendar : Calendar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    calendar = arguments?.getSerializable("current") as? Calendar
        ?: Calendar.getInstance()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return DatePickerDialog(context, this,
        calendar[Calendar.YEAR], calendar[Calendar.MONTH],
        calendar[Calendar.DATE])
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    if (context is DatePickerDialog.OnDateSetListener) {
      (context as DatePickerDialog.OnDateSetListener).onDateSet(
          view, year, month, dayOfMonth
      )
    }
  }

}

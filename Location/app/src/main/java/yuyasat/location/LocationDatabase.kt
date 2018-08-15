package yuyasat.location

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import java.util.*

private const val DB_NAME = "LocationDatabase"
private const val DB_VERSION = 1

class LocationDatabase(context: Context)
  : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
  override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL("""
      |CREATE TABLE Locations (
      |  _id INTEGER PRIMARY KEY AUTOINCREMENT,
      |  latitude REAL NOT NULL,
      |  longitude REAL NOT NULL,
      |  time INTEGER NOT NULL
      |)
    """.trimMargin())
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
  }
}

class LocationRecord(val id: Long, val latitude: Double,
                     val longitude: Double, val time: Long)

fun selectInDay(context: Context, year: Int, month: Int, day: Int)
    : List<LocationRecord> {
  val calendar = Calendar.getInstance()
  calendar.set(year, month, day, 0, 0,0)
  val from = calendar.time.time.toString()
  calendar.add(Calendar.DATE, 1)
  val to = calendar.time.time.toString()

  val database = LocationDatabase(context).readableDatabase

  val cursor = database.query(
      "Locations",
      null,
      "time >= ? AND time < ?",
      arrayOf(from, to),
      null,
      null,
      "time Desc"
  )
  val locations = mutableListOf<LocationRecord>()
  cursor.use {
    while(cursor.moveToNext()) {
      val place = LocationRecord(
          cursor.getLong(cursor.getColumnIndex("_id")),
          cursor.getDouble(cursor.getColumnIndex("latitude")),
          cursor.getDouble(cursor.getColumnIndex("longitude")),
          cursor.getLong(cursor.getColumnIndex("time"))
      )
      locations.add(place)
    }
  }
  database.close()
  return locations
}

fun insertLocations(context: Context, locations: List<Location>) {
  val database = LocationDatabase(context).writableDatabase

  database.use { db ->
    locations.filter { !it.isFromMockProvider }
        .forEach { location ->
          val record = ContentValues().apply {
            put("latitude", location.latitude)
            put("longitude", location.longitude)
            put("time", location.time)
          }
          db.insert("Locations", null, record)
        }
  }
}
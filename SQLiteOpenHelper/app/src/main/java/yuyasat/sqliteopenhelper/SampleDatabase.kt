package yuyasat.sqliteopenhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "SampleDatabase"
private const val DB_VERSION = 1

class SampleDBOpenHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
  override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL("""
      |CREATE TABLE texts (
      |  _id INTEGER PRIMARY KEY AUTOINCREMENT,
      |  text TEXT NOT NULL,
      |  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      |)
    """.trimMargin())

  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

  }
}

fun queryTexts(context: Context) : List<String> {
  val database = SampleDBOpenHelper(context).readableDatabase
  val cursor = database.query(
      "texts", null, null, null, null, null, "created_at DESC"
  )
  val texts = mutableListOf<String>()
  cursor.use {
    while(cursor.moveToNext()) {
      val text = cursor.getString(cursor.getColumnIndex("text"))
      texts.add(text)
    }
  }

  database.close()
  return texts
}

fun insertText(context: Context, text: String) {
  val database = SampleDBOpenHelper(context).writableDatabase

  database.use { db ->
    val record = ContentValues().apply {
      put("text", text)
    }
    db.insert("texts", null, record)
  }
}
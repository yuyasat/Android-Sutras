package yuyasat.sharedpreferences

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val pref = getSharedPreferences("file_name", Context.MODE_PRIVATE)

    val storedText = pref.getString("key", "未登録")

    val editText = findViewById<EditText>(R.id.editText)
    editText.setText(storedText)

    val button = findViewById<Button>(R.id.store)
    button.setOnClickListener {
      val inputText = editText.text.toString()
      pref.edit().putString("key", inputText).apply()
    }
  }
}

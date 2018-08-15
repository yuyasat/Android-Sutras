package yuyasat.file

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
  private var currentDir : File = Environment.getExternalStorageDirectory()
  private lateinit var recyclerView : RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    recyclerView = findViewById(R.id.filesList)
    recyclerView.layoutManager = LinearLayoutManager(this,
        LinearLayoutManager.VERTICAL, false)
    if (hasPermission()) showFiles()
  }

  private fun showFiles() {
    val adapter = FilesAdapter(this,
        currentDir.listFiles().toList()) { file ->
      if (file.isDirectory) {
        currentDir = file
        showFiles()
      } else {
        Toast.makeText(this,
            file.absolutePath, Toast.LENGTH_SHORT).show()
      }
    }
    recyclerView.adapter = adapter
    title = currentDir.path
  }

  private fun hasPermission() : Boolean {
    if (
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(this,
          arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
      return false
    }

    return true
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      showFiles()
    } else {
      finish()
    }
  }

  override fun onBackPressed() {
    if (currentDir != Environment.getExternalStorageDirectory()) {
      currentDir = currentDir.parentFile
      showFiles()
    } else {
      super.onBackPressed()
    }
  }
}

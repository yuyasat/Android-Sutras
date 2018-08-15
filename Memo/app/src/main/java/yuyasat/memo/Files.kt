package yuyasat.memo

import android.os.Environment
import android.text.format.DateFormat
import java.io.*
import java.util.*

private fun getFilesDir() : File {
  val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

  if (publicDir != null) {
    if (!publicDir.exists()) publicDir.mkdirs()
    return  publicDir
  } else {
    val dir = File(Environment.getExternalStorageDirectory(), "MemoFiles")
    if (!dir.exists()) dir.mkdirs()
    return dir
  }
}

fun getFiles() = getFilesDir().listFiles().toList()

fun outputFile(original: File?, content: String) : File {
  val timeStamp = DateFormat.format("yyyy-MM-dd-hh-mm-ss", Date())
  val file = original ?: File(getFilesDir(), "memo-$timeStamp")

  val writer = BufferedWriter(FileWriter(file))

  writer.use {
    it.write(content)
    it.flush()
  }
  return file
}

fun inputFile(file : File) : String {
  val reader = BufferedReader(FileReader(file))
  return reader.readLines().joinToString("¥n")
}
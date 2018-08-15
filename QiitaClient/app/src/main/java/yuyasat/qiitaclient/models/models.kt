package yuyasat.qiitaclient.models

import android.os.Parcel
import android.os.Parcelable

data class User(val id: String, val name: String, val profileImageUrl: String) : Parcelable {
  constructor(source: Parcel) : this(
      source.readString(),
      source.readString(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(id)
    writeString(name)
    writeString(profileImageUrl)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
      override fun createFromParcel(source: Parcel): User = User(source)
      override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
    }
  }
}

data class Article(val id: String, val title: String, val url: String, val user: User) : Parcelable {
  constructor(source: Parcel) : this(
      source.readString(),
      source.readString(),
      source.readString(),
      source.readParcelable<User>(User::class.java.classLoader)
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(id)
    writeString(title)
    writeString(url)
    writeParcelable(user, 0)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
      override fun createFromParcel(source: Parcel): Article = Article(source)
      override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
    }
  }
}
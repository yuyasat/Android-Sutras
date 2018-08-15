package yuyasat.qiitaclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import yuyasat.qiitaclient.R
import yuyasat.qiitaclient.bindView
import yuyasat.qiitaclient.models.Article

class ArticleView : FrameLayout {
  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
      : super(context, attrs, defStyleAttr)

  val profileImageView: ImageView by bindView(R.id.profileImageView)
  val titleTextView: TextView by bindView(R.id.titleTextView)
  val userNameTextView: TextView by bindView(R.id.userNameTextView)

  init {
    LayoutInflater.from(context).inflate(R.layout.list_article_row, this)
  }

  fun setArticle(article: Article) {
    titleTextView.text = article.title
    userNameTextView.text = article.user.name
    Glide.with(context).load(article.user.profileImageUrl).into(profileImageView)
  }
}
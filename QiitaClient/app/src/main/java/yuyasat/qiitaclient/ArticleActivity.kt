package yuyasat.qiitaclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import yuyasat.qiitaclient.models.Article
import yuyasat.qiitaclient.views.ArticleView

class ArticleActivity : AppCompatActivity() {
  companion object {
    private const val ARTICLE_EXTRA: String = "article"

    fun intent(context: Context, article: Article): Intent {
      return Intent(context, ArticleActivity::class.java)
          .putExtra(ARTICLE_EXTRA, article)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_article)

    val articleView = findViewById<ArticleView>(R.id.article_view)
    val articleWebView = findViewById<WebView>(R.id.articleWebView)

    val article = intent.getParcelableExtra<Article>(ARTICLE_EXTRA)
    articleView.setArticle(article)
    articleWebView.loadUrl(article.url)
  }
}
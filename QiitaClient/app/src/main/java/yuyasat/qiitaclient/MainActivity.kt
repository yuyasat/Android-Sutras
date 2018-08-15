package yuyasat.qiitaclient

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import yuyasat.qiitaclient.client.ArticleClient
import yuyasat.qiitaclient.models.Article

class MainActivity : RxAppCompatActivity() {
  private lateinit var recyclerView : RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    recyclerView = findViewById(R.id.articlesList)
    recyclerView.layoutManager = LinearLayoutManager(this,
        LinearLayoutManager.VERTICAL, false)
    val articles = mutableListOf<Article>()
    val adapter = ArticlesAdapter(this, articles) { article ->
      startActivity(
          ArticleActivity.intent(this, article)
      )
    }
    recyclerView.adapter = adapter

    val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://qiita.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    val articleClient = retrofit.create(ArticleClient::class.java)

    val searchButton = findViewById<Button>(R.id.searchButton)
    val queryEditText = findViewById<EditText>(R.id.queryEditText)
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
    searchButton.setOnClickListener { view ->
      progressBar.visibility = View.VISIBLE

      articleClient.search(queryEditText.text.toString())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doAfterTerminate {
            progressBar.visibility = View.GONE
          }
          .bindToLifecycle(this)
          .subscribe({
            queryEditText.text.clear()
            adapter.articles = it
            adapter.notifyDataSetChanged()
          }, {
            toast("エラー: $it")
          })
    }
  }
}

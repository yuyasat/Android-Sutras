package yuyasat.qiitaclient

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import yuyasat.qiitaclient.models.Article

class ArticlesAdapter(context: Context, var articles : List<Article>,
                      private val onClick : (Article) -> Unit)
  : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
    val view = inflater.inflate(R.layout.list_article_row, parent, false)
    val viewHolder = ArticleViewHolder(view)

    view.setOnClickListener {
      onClick(articles[viewHolder.adapterPosition])
    }
    return viewHolder
  }

  override fun getItemCount() = articles.size

  override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
    val article = articles[position]
    holder.title.text = article.title
    holder.userName.text = article.user.name
    Glide.with(holder.profileImage.context)
        .load(article.user.profileImageUrl)
        .into(holder.profileImage)

  }

  private val inflater = LayoutInflater.from(context)

  class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.titleTextView)
    val userName = view.findViewById<TextView>(R.id.userNameTextView)
    val profileImage = view.findViewById<ImageView>(R.id.profileImageView)
  }
}

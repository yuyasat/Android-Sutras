package yuyasat.qiitaclient.client

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import yuyasat.qiitaclient.models.Article


interface ArticleClient {
  @GET("/api/v2/items")
  fun search(@Query("query") query: String): Observable<List<Article>>
}
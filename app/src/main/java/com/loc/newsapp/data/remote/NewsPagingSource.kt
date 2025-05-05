package com.loc.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.util.Constants.API_KEY

class NewsPagingSource(
    private val newsAPI: NewsAPI,
    private val sources: String,

) : PagingSource<Int , Article>() {

    private var totalNewsCount = 0



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val newsResponse = newsAPI.getNews(source = sources , page = page )
            totalNewsCount += newsResponse.articles.size
            val articles = newsResponse.articles.distinctBy { it.title }
            LoadResult.Page(
                data = articles,
                nextKey = if(totalNewsCount == newsResponse.totalResults) null else page + 1 ,
                prevKey = null
            )

        }catch (e: Exception){
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
       return state.anchorPosition?.let{ anchorPosition ->
           val anchorPage = state.closestPageToPosition(anchorPosition)
              anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

       }
    }
}
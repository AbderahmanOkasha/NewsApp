package com.loc.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.loc.newsapp.data.remote.NewsAPI
import com.loc.newsapp.data.remote.NewsPagingSource
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryimpl (
    private val newsApi : NewsAPI
): NewsRepository  {
    override fun getNews(sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10 ) ,
            pagingSourceFactory = {
                NewsPagingSource(
                    newsAPI = newsApi,
                    sources = sources.joinToString(",")
                )
            }
        ).flow
    }
}
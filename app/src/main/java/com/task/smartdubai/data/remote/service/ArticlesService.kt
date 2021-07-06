package com.task.smartdubai.data.remote.service

import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesService {
    @GET("all-sections/7.json")
    suspend fun fetchArticles(@Query("api-key") apiKey:String): Response<PopularArticlesResponse>
}

package com.task.smartdubai.data.remote

import com.task.smartdubai.data.Resource
import com.task.smartdubai.data.dto.news.PopularArticlesResponse

internal interface RemoteDataSource {
    suspend fun requestArticles(apiKey:String): Resource<PopularArticlesResponse>
}

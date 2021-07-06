package com.task.smartdubai.data

import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {
    suspend fun requestArticles(apiKey:String): Flow<Resource<PopularArticlesResponse>>
}

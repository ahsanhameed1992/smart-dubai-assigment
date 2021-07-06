package com.task.smartdubai.data

import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class DataRepository @Inject constructor(private val remoteRepository: RemoteData,  private val ioDispatcher: CoroutineContext) : DataRepositorySource {

    override suspend fun requestArticles(apiKey: String): Flow<Resource<PopularArticlesResponse>> {
        return flow {
            emit(remoteRepository.requestArticles(apiKey))
        }.flowOn(ioDispatcher)
    }
}

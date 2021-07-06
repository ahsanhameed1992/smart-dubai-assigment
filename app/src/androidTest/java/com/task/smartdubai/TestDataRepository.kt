package com.task.smartdubai

import com.task.smartdubai.TestUtil.dataStatus
import com.task.smartdubai.TestUtil.initData
import com.task.smartdubai.data.DataRepositorySource
import com.task.smartdubai.data.Resource
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.error.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class TestDataRepository @Inject constructor() : DataRepositorySource {

    override suspend fun requestArticles(apiKey: String): Flow<Resource<PopularArticlesResponse>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Resource.Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(Resource.DataError<PopularArticlesResponse>(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Resource.Success(PopularArticlesResponse(results = arrayListOf()))) }
            }
        }
    }



}

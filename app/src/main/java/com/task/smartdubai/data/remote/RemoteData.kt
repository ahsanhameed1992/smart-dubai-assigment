package com.task.smartdubai.data.remote

import com.task.smartdubai.data.Resource
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.error.NETWORK_ERROR
import com.task.smartdubai.data.error.NO_INTERNET_CONNECTION
import com.task.smartdubai.data.remote.service.ArticlesService
import com.task.smartdubai.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator, private val networkConnectivity: NetworkConnectivity) : RemoteDataSource {
    override suspend fun requestArticles(apiKey:String): Resource<PopularArticlesResponse> {
        val articlesService = serviceGenerator.createService(ArticlesService::class.java)
        return when (val response = processCall { articlesService.fetchArticles(apiKey) }) {
            is PopularArticlesResponse -> {
                Resource.Success(data = response as PopularArticlesResponse)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}

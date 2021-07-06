package com.task.smartdubai

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.dto.news.Results
import java.io.InputStream

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var articles: PopularArticlesResponse = PopularArticlesResponse(results = arrayListOf())
    fun initData(): PopularArticlesResponse {

        val jsonString = getJson("ArticlesApiResponse.json")
        val listType = object : TypeToken<List<Results?>?>() {}.type
        val articlesList: List<Results> =  Gson().fromJson(jsonString, listType)

        articlesList.let {
            articles = PopularArticlesResponse(results = ArrayList(it))
            return articles
        }
        return PopularArticlesResponse(results = arrayListOf())
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}

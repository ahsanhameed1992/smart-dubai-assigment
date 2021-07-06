package com.task.smartdubai.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.dto.news.Results
import java.io.File
import java.io.InputStream


class TestModelsGenerator {
    private var articles: PopularArticlesResponse = PopularArticlesResponse(results = arrayListOf())

    init {

        val jsonString = getJson("ArticlesApiResponse.json")
        val listType = object : TypeToken<List<Results?>?>() {}.type
        val articlesList: List<Results> =  Gson().fromJson(jsonString, listType)
        articlesList.let {
            articles = PopularArticlesResponse(results = ArrayList(it))
        }
        print("this is $articles")
    }

    fun generateArticles(): PopularArticlesResponse {
        return articles
    }

    fun generateArticlesModelWithEmptyList(): PopularArticlesResponse {
        return PopularArticlesResponse(results = arrayListOf())
    }

    fun generateArticlesItemModel(): Results {
        return articles.results[0]
    }

    fun getStubSearchTitle(): String {
        return articles.results[0].title
    }


    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */

    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val inputStream: InputStream? = this.javaClass.classLoader?.getResourceAsStream(path)
       // val file = File(uri?.path)
        return inputStream?.bufferedReader().use { it!!.readText() }
    }
}

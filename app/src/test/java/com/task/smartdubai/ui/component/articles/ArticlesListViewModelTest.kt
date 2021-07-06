package com.task.smartdubai.ui.component.articles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.task.smartdubai.data.DataRepository
import com.task.smartdubai.data.Resource
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.error.NETWORK_ERROR
import com.task.smartdubai.util.InstantExecutorExtension
import com.task.smartdubai.util.MainCoroutineRule
import com.task.smartdubai.util.TestModelsGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class ArticlesListViewModelTest {
    // Subject under test
    private lateinit var articlesListViewModel: ArticlesListViewModel
    private  val apiKey:String = "RZq6bhQAXJ9SpkiH4FasHiWzBxhGwjQG"

    // Use a fake UseCase to be injected into the viewModel
    private val dataRepository: DataRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var articleTitle: String
    private val testModelsGenerator: TestModelsGenerator = TestModelsGenerator()

    @Before
    fun setUp() {
        // Create class under test
        // We initialise the repository with no tasks
        articleTitle = testModelsGenerator.getStubSearchTitle()
    }

    @Test
    fun `get Articles List`() {
        // Let's do an answer for the liveData
        val articlesModel = testModelsGenerator.generateArticles()

        //1- Mock calls
        coEvery { dataRepository.requestArticles(apiKey) } returns flow {
            emit(Resource.Success(articlesModel))
        }

        //2-Call
        articlesListViewModel = ArticlesListViewModel(dataRepository)
        articlesListViewModel.getArticles(apiKey)
        //active observer for livedata
        articlesListViewModel.articlesLiveData.observeForever { }

        //3-verify
        val isEmptyList = articlesListViewModel.articlesLiveData.value?.data?.results.isNullOrEmpty()
        assertEquals(articlesModel, articlesListViewModel.articlesLiveData.value?.data)
        assertEquals(false,isEmptyList)
    }

    @Test
    fun `get Articles Empty List`() {
        // Let's do an answer for the liveData
        val articlesModel = testModelsGenerator.generateArticlesModelWithEmptyList()

        //1- Mock calls
        coEvery { dataRepository.requestArticles(apiKey) } returns flow {
            emit(Resource.Success(articlesModel))
        }

        //2-Call
        articlesListViewModel = ArticlesListViewModel(dataRepository)
        articlesListViewModel.getArticles(apiKey)
        //active observer for livedata
        articlesListViewModel.articlesLiveData.observeForever { }

        //3-verify
        val isEmptyList = articlesListViewModel.articlesLiveData.value?.data?.results.isNullOrEmpty()
        assertEquals(articlesModel, articlesListViewModel.articlesLiveData.value?.data)
        assertEquals(true, isEmptyList)
    }

    @Test
    fun `get Articles Error`() {
        // Let's do an answer for the liveData
        val error: Resource<PopularArticlesResponse> = Resource.DataError(NETWORK_ERROR)

        //1- Mock calls
        coEvery { dataRepository.requestArticles(apiKey) } returns flow {
            emit(error)
        }

        //2-Call
        articlesListViewModel = ArticlesListViewModel(dataRepository)
        articlesListViewModel.getArticles(apiKey)
        //active observer for livedata
        articlesListViewModel.articlesLiveData.observeForever { }

        //3-verify
        assertEquals(NETWORK_ERROR, articlesListViewModel.articlesLiveData.value?.errorCode)
    }

    @Test
    fun `search Success`() {
        // Let's do an answer for the liveData
        val article = testModelsGenerator.generateArticlesItemModel()
        val title = article.title
        //1- Mock calls
        articlesListViewModel = ArticlesListViewModel(dataRepository)
        articlesListViewModel.articlesLiveDataPrivate.value = Resource.Success(testModelsGenerator.generateArticles())

        //2-Call
        articlesListViewModel.onSearchClick(title)
        //active observer for livedata
        articlesListViewModel.articleSearchFound.observeForever { }

        //3-verify
        assertEquals(article, articlesListViewModel.articleSearchFound.value)
    }

    @Test
    fun `search Fail`() {
        // Let's do an answer for the liveData
        val title = "*&*^%"

        //1- Mock calls
        articlesListViewModel = ArticlesListViewModel(dataRepository)
        articlesListViewModel.articlesLiveDataPrivate.value = Resource.Success(testModelsGenerator.generateArticles())

        //2-Call
        articlesListViewModel.onSearchClick(title)
        //active observer for livedata
        articlesListViewModel.noSearchFound.observeForever { }

        //3-verify
        assertEquals(Unit, articlesListViewModel.noSearchFound.value)
    }
}

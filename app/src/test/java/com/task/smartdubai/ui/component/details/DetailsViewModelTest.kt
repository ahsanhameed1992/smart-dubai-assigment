package com.task.smartdubai.ui.component.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.task.smartdubai.data.DataRepository
import com.task.smartdubai.util.InstantExecutorExtension
import com.task.smartdubai.util.MainCoroutineRule
import com.task.smartdubai.util.TestModelsGenerator
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class DetailsViewModelTest {
    // Subject under test
    private lateinit var detailsViewModel: DetailsViewModel

    // Use a fake UseCase to be injected into the viewModel
    private val dataRepository: DataRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testModelsGenerator: TestModelsGenerator = TestModelsGenerator()

    @Test
    fun `init Intent Data`() {
        //1- Mock Data
        val articleItem = testModelsGenerator.generateArticlesItemModel()
        //2-Call
        detailsViewModel = DetailsViewModel(dataRepository)
        detailsViewModel.initIntentData(articleItem)
        //active observer for livedata
        detailsViewModel.articleData.observeForever { }

        //3-verify
        val articlesData = detailsViewModel.articleData.value
        assertEquals(articleItem, articlesData)
    }


}

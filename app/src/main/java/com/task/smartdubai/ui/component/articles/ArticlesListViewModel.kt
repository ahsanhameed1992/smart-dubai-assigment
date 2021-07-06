package com.task.smartdubai.ui.component.articles

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.smartdubai.data.DataRepositorySource
import com.task.smartdubai.data.Resource
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.dto.news.Results
import com.task.smartdubai.ui.base.BaseViewModel
import com.task.smartdubai.utils.SingleEvent
import com.task.smartdubai.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale.ROOT
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val articlesLiveDataPrivate = MutableLiveData<Resource<PopularArticlesResponse>>()
    val articlesLiveData: LiveData<Resource<PopularArticlesResponse>> get() = articlesLiveDataPrivate


    //TODO check to make them as one Resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val articleSearchFoundPrivate: MutableLiveData<Results> = MutableLiveData()
    val articleSearchFound: LiveData<Results> get() = articleSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openArticleDetailsPrivate = MutableLiveData<SingleEvent<Results>>()
    val openArticleDetails: LiveData<SingleEvent<Results>> get() = openArticleDetailsPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun getArticles(apiKey:String) {
        viewModelScope.launch {
            articlesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestArticles(apiKey).collect {
                    articlesLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openArticleDetails(result: Results) {
        openArticleDetailsPrivate.value = SingleEvent(result)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun onSearchClick(artiicleName: String) {
        articlesLiveDataPrivate.value?.data?.results?.let {
            if (it.isNotEmpty()) {
                for (article in it) {
                    if (article.title.toLowerCase(ROOT).contains(artiicleName.toLowerCase(ROOT))) {
                        articleSearchFoundPrivate.value = article
                        return
                    }
                }
            }
        }
        return noSearchFoundPrivate.postValue(Unit)
    }
}

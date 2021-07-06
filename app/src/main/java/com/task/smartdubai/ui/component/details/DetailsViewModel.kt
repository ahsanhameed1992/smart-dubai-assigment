package com.task.smartdubai.ui.component.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.smartdubai.data.DataRepositorySource
import com.task.smartdubai.data.dto.news.Results
import com.task.smartdubai.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class DetailsViewModel @Inject constructor(private val dataRepository: DataRepositorySource) : BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val articlePrivate = MutableLiveData<Results>()
    val articleData: LiveData<Results> get() = articlePrivate

    fun initIntentData(results: Results) {
        articlePrivate.value = results
    }

}

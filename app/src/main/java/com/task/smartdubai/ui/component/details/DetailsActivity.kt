package com.task.smartdubai.ui.component.details

import android.os.Bundle
import androidx.activity.viewModels
import com.task.databinding.DetailsLayoutBinding
import com.task.smartdubai.ARTICLE_ITEM_KEY
import com.task.smartdubai.data.dto.news.Results
import com.task.smartdubai.ui.base.BaseActivity
import com.task.smartdubai.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : BaseActivity() {

    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var binding: DetailsLayoutBinding

    override fun initViewBinding() {
        binding = DetailsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.getParcelableExtra<Results>(ARTICLE_ITEM_KEY)?.let { viewModel.initIntentData(it) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun observeViewModel() {
        observe(viewModel.articleData, ::initializeView)
    }

    private fun initializeView(results: Results) {
        binding.tvTitle.text = results.title
        binding.tvHeadline.text = results.byline
        binding.tvDescription.text = results.abstract
        binding.tvDate.text = results.published_date
    }
}

package com.task.smartdubai.ui.component.articles

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.task.R
import com.task.databinding.ArticlesActivityBinding
import com.task.smartdubai.ARTICLE_ITEM_KEY
import com.task.smartdubai.data.Resource
import com.task.smartdubai.data.dto.news.PopularArticlesResponse
import com.task.smartdubai.data.dto.news.Results
import com.task.smartdubai.data.error.SEARCH_ERROR
import com.task.smartdubai.ui.base.BaseActivity
import com.task.smartdubai.ui.component.details.DetailsActivity
import com.task.smartdubai.ui.component.articles.adapter.ArticlesAdapter
import com.task.smartdubai.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesListActivity : BaseActivity() {
    private lateinit var binding: ArticlesActivityBinding

    private val articlesViewModel: ArticlesListViewModel by viewModels()
    private lateinit var articleAdapter: ArticlesAdapter

    override fun initViewBinding() {
        binding = ArticlesActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.articles)
        val layoutManager = LinearLayoutManager(this)
        binding.rvArticlesList.layoutManager = layoutManager
        binding.rvArticlesList.setHasFixedSize(true)
        articlesViewModel.getArticles("RZq6bhQAXJ9SpkiH4FasHiWzBxhGwjQG")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)
        // Associate searchable configuration with the SearchView
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_by_name)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                handleSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> articlesViewModel.getArticles("RZq6bhQAXJ9SpkiH4FasHiWzBxhGwjQG")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = VISIBLE
            articlesViewModel.onSearchClick(query)
        }
    }


    private fun bindListData(articles: PopularArticlesResponse) {
        if (!(articles.results.isNullOrEmpty())) {
            articleAdapter = ArticlesAdapter(articlesViewModel, articles.results)
            binding.rvArticlesList.adapter = articleAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<Results>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(this, DetailsActivity::class.java).apply {
                putExtra(ARTICLE_ITEM_KEY, it)
            }
            startActivity(nextScreenIntent)
        }
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showSearchError() {
        articlesViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) GONE else VISIBLE
        binding.rvArticlesList.visibility = if (show) VISIBLE else GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvArticlesList.toGone()
    }


    private fun showSearchResult(results: Results) {
        articlesViewModel.openArticleDetails(results)
        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleArticlesList(status: Resource<PopularArticlesResponse>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(articles = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { articlesViewModel.showToastMessage(it) }
            }
        }
    }

    override fun observeViewModel() {
        observe(articlesViewModel.articlesLiveData, ::handleArticlesList)
        observe(articlesViewModel.articleSearchFound, ::showSearchResult)
        observe(articlesViewModel.noSearchFound, ::noSearchResult)
        observeEvent(articlesViewModel.openArticleDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(articlesViewModel.showSnackBar)
        observeToast(articlesViewModel.showToast)

    }
}

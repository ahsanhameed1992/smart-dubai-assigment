package com.task.smartdubai.ui.component.articles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.databinding.ArticlesItemBinding
import com.task.smartdubai.data.dto.news.Results
import com.task.smartdubai.ui.base.listeners.RecyclerItemListener
import com.task.smartdubai.ui.component.articles.ArticlesListViewModel


class ArticlesAdapter(private val articlesListViewModel: ArticlesListViewModel, private val articles: List<Results>) : RecyclerView.Adapter<ArticlesViewHolder>() {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(result: Results) {
            articlesListViewModel.openArticleDetails(result)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val itemBinding = ArticlesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticlesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        holder.bind(articles[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}


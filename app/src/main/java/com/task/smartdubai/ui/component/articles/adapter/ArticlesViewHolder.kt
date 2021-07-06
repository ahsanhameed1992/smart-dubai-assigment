package com.task.smartdubai.ui.component.articles.adapter

import androidx.recyclerview.widget.RecyclerView
import com.task.databinding.ArticlesItemBinding
import com.task.smartdubai.data.dto.news.Results
import com.task.smartdubai.ui.base.listeners.RecyclerItemListener


class ArticlesViewHolder(private val itemBinding: ArticlesItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(item: Results, recyclerItemListener: RecyclerItemListener) {
        itemBinding.tvCaption.text = item.byline
        itemBinding.tvName.text = item.title
        itemBinding.tvDate.text = item.published_date
        itemBinding.rlArticleItem.setOnClickListener { recyclerItemListener.onItemSelected(item) }
    }
}


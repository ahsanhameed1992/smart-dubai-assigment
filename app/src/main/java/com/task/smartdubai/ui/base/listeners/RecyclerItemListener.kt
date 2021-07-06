package com.task.smartdubai.ui.base.listeners

import com.task.smartdubai.data.dto.news.Results

interface RecyclerItemListener {
    fun onItemSelected(result : Results)
}

package com.task.smartdubai.data.dto.news

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PopularArticlesResponse (

	@SerializedName("status")
	val status : String?= null,
	@SerializedName("copyright")
	val copyright : String?= null,
	@SerializedName("num_results")
	val num_results : Int? = 0,
	@SerializedName("results")
	val results : List<Results>
): Parcelable
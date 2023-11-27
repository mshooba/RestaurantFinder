package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class ReviewsItem(
    @SerializedName("data")
    val `data`: List<Data>
)
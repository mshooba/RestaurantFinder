package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class X0(
    @SerializedName("localized_name")
    val localizedName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating_image_url")
    val ratingImageUrl: String,
    @SerializedName("value")
    val value: Int
)
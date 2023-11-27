package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class Avatar(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("small")
    val small: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)
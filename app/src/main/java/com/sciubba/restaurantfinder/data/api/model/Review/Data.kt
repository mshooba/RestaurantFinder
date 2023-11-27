package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("helpful_votes")
    val helpfulVotes: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lang")
    val lang: String,
    @SerializedName("location_id")
    val locationId: Int,
    @SerializedName("published_date")
    val publishedDate: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("rating_image_url")
    val ratingImageUrl: String,
    @SerializedName("subratings")
    val subratings: Subratings,
    @SerializedName("text")
    val text: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("travel_date")
    val travelDate: String,
    @SerializedName("trip_type")
    val tripType: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("user")
    val user: User
)
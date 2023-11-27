package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: Avatar,
    @SerializedName("user_location")
    val userLocation: UserLocation,
    @SerializedName("username")
    val username: String
)
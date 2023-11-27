package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class UserLocation(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
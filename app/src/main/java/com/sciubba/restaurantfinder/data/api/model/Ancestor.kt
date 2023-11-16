package com.sciubba.restaurantfinder.data.api.model


import com.google.gson.annotations.SerializedName

data class Ancestor(
    @SerializedName("level")
    val level: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String
)
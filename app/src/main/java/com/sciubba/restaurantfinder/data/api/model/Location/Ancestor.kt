package com.sciubba.restaurantfinder.data.api.model.Location


import com.google.gson.annotations.SerializedName

data class Ancestor(
    @SerializedName("abbrv")
    val abbrv: String,
    @SerializedName("level")
    val level: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String
)
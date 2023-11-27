package com.sciubba.restaurantfinder.data.api.model.Location


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("localized_name")
    val localizedName: String,
    @SerializedName("name")
    val name: String
)
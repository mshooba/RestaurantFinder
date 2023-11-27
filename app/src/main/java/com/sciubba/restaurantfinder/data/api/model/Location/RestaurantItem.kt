package com.sciubba.restaurantfinder.data.api.model.Location


import com.google.gson.annotations.SerializedName

data class RestaurantItem(
    @SerializedName("data")
    val `data`: List<Data>
)
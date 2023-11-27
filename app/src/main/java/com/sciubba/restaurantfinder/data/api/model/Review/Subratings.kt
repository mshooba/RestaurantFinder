package com.sciubba.restaurantfinder.data.api.model.Review


import com.google.gson.annotations.SerializedName

data class Subratings(
    @SerializedName("0")
    val x0: X0,
    @SerializedName("1")
    val x1: X0,
    @SerializedName("2")
    val x2: X0,
    @SerializedName("3")
    val x3: X0
)
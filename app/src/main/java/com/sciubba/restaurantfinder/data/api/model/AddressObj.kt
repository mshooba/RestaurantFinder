package com.sciubba.restaurantfinder.data.api.model


import com.google.gson.annotations.SerializedName

data class AddressObj(
    @SerializedName("address_string")
    val addressString: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String
)
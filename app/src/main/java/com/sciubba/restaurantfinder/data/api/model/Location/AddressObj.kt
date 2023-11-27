package com.sciubba.restaurantfinder.data.api.model.Location


import com.google.gson.annotations.SerializedName

data class AddressObj(
    @SerializedName("address_string")
    val addressString: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("postalcode")
    val postalcode: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("street1")
    val street1: String,
    @SerializedName("street2")
    val street2: String
)
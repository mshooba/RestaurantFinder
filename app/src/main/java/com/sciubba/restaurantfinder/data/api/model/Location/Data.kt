package com.sciubba.restaurantfinder.data.api.model.Location


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("address_obj")
    val addressObj: AddressObj,
    @SerializedName("bearing")
    val bearing: String,
    @SerializedName("distance")
    val distance: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String
)
package com.sciubba.apihomework.data.api.model
import com.google.gson.annotations.SerializedName

data class NearbyLocation(
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("distance")
    val distance: String,
    @SerializedName("bearing")
    val bearing: String,
    @SerializedName("address_obj")
    val addressObj: AddressObj
)

package com.sciubba.restaurantfinder.data.api.model.Location


import com.google.gson.annotations.SerializedName

data class LocationItem(
    @SerializedName("address_obj")
    val addressObj: AddressObj,
    @SerializedName("ancestors")
    val ancestors: List<Ancestor>,
    @SerializedName("awards")
    val awards: List<Any>,
    @SerializedName("category")
    val category: Category,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("neighborhood_info")
    val neighborhoodInfo: List<Any>,
    @SerializedName("see_all_photos")
    val seeAllPhotos: String,
    @SerializedName("subcategory")
    val subcategory: List<Subcategory>,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("web_url")
    val webUrl: String
)
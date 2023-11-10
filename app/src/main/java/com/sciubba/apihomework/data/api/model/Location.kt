package com.sciubba.apihomework.data.api.model
import com.google.gson.annotations.SerializedName

data class Location(
    /**
     * adding lat and long causes error only way to get restaurants for locations?
     *
     * I think the lat and long is nested? but the JSON plugin didn't pick it up
     */
    @SerializedName("image_name")
    val imageName: String,
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
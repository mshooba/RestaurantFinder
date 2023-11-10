package com.sciubba.apihomework.data.api.model


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("localized_name")
    val localizedName: String,
    @SerializedName("name")
    val name: String
)
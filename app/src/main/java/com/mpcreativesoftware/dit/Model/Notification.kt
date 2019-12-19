package com.mpcreativesoftware.dit.Model


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("token")
    var token: ArrayList<String>,
    @SerializedName("title")
    var title: String,
    @SerializedName("message")
    var message: String
)
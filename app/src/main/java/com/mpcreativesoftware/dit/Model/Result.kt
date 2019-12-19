package com.mpcreativesoftware.dit.Model


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("message_id")
    var messageId: String
)
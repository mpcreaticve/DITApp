package com.mpcreativesoftware.dit.Model


import com.google.gson.annotations.SerializedName

data class FCM_Response(
    @SerializedName("canonical_ids")
    var canonicalIds: Int,
    @SerializedName("failure")
    var failure: Int,
    @SerializedName("multicast_id")
    var multicastId: Long,
    @SerializedName("results")
    var results: List<Result>,
    @SerializedName("success")
    var success: Int
)
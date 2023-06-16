package com.pc609.potholesense.api

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @SerializedName("result")
    val result: Int,

    @field:SerializedName("status")
    val status: String? = null,
)
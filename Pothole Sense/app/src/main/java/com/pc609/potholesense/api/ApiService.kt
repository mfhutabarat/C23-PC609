package com.pc609.potholesense.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("upload")
    @Multipart
    fun uploadFile(
        @Part imgFile: MultipartBody.Part,
    ): Call<UploadResponse>
}

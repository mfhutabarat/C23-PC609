package com.pc609.potholesense.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pc609.potholesense.api.ApiService
import com.pc609.potholesense.api.UploadResponse
import com.pc609.potholesense.ui.SharedPreferencesHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


class PredictViewModel(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) : ViewModel() {
    private val apiService: ApiService

    private val _predictionResult = MutableLiveData<Int>()
    val predictionResult: LiveData<Int> get() = _predictionResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _resultText = MutableLiveData<String>()
    val resultText: LiveData<String> get() = _resultText

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.224.238.145/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun processImage(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageByteArray = byteArrayOutputStream.toByteArray()

        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageByteArray)
        val imgPart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        val call = apiService.uploadFile(imgPart)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>,
            ) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    if (uploadResponse != null && uploadResponse.status == "success") {
                        val result = uploadResponse.result
                        val resultDescription = getResultDescription(result)
                        if (resultDescription != null) {
                            showToast(resultDescription)
                            Log.d(TAG, "Hasil prediksi: $resultDescription")
                            _resultText.value = "Hasil Prediksi: $resultDescription"
                        } else {
                            _errorMessage.value = "Hasil tidak valid: $result"
                            Log.e(TAG, "Hasil tidak valid: $result")
                        }
                        // Log respons JSON
                        Log.d(TAG, "Respons JSON: ${response.body()}")
                    } else {
                        _errorMessage.value = "Error: Gagal memparsing respons"
                        Log.e(TAG, "Error: Gagal memparsing respons")
                    }
                } else {
                    _errorMessage.value = "Server Off"
                    Log.e(TAG, "Server Off")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _errorMessage.value = "Request failed: ${t.message}"
                Log.e(TAG, "Request failed", t)
            }
        })
    }

    fun getResultDescription(result: Int): String? {
        return when (result) {
            0 -> "Kerusakan Ringan (Retak)"
            1 -> "Kerusakan Berat (Berlubang)"
            else -> null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "UploadViewModel"
    }
}

package com.example.digimonexplorer.data.remote

import com.example.digimonexplorer.data.model.DigimonDetailResponse
import com.example.digimonexplorer.data.model.DigimonListResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DigimonApiService {
    @GET("digimon")
    suspend fun getDigimonList(//fungsi
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 20
    ): DigimonListResponse

    @GET("digimon/{id}")
    suspend fun getDigimonDetail(//fungsi
        @Path("id") id: Int
    ): DigimonDetailResponse
}

object RetrofitClient {//
    private const val BASE_URL = "https://digi-api.com/api/v1/"

    val apiService: DigimonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DigimonApiService::class.java)
    }
}

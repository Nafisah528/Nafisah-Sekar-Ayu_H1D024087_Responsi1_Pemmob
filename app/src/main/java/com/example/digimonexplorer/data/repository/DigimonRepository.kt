package com.example.digimonexplorer.data.repository

import com.example.digimonexplorer.data.model.DigimonCardItem
import com.example.digimonexplorer.data.model.DigimonDetailResponse
import com.example.digimonexplorer.data.remote.DigimonApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Interface Repository untuk mengabstraksi sumber data aplikasi (MVVM Pattern).
 */
interface DigimonRepository {
    suspend fun getDigimonList(page: Int = 0, pageSize: Int = 20): List<DigimonCardItem>
    suspend fun getDigimonDetail(id: Int): DigimonDetailResponse
}

/**
 * Implementasi Repository yang mengonsumsi Digi-API melalui Retrofit.
 * Menyediakan cache in-memory untuk performa cepat dan navigasi mulus.
 */
class DigimonRepositoryImpl(
    private val apiService: DigimonApiService
) : DigimonRepository {

    // Cache in-memory untuk menyimpan detail Digimon yang telah di-fetch
    private val detailCache = ConcurrentHashMap<Int, DigimonDetailResponse>()

    override suspend fun getDigimonList(page: Int, pageSize: Int): List<DigimonCardItem> = withContext(Dispatchers.IO) {
        val response = apiService.getDigimonList(page = page, pageSize = pageSize)

        // Endpoint list Digi-API hanya menyertakan (id, name, href, image).
        // Untuk memenuhi spesifikasi responsi (Card menampilkan Nama, Level, Attribute, Type),
        // kita menggunakan Kotlin Coroutines async-awaitAll untuk mengambil detail secara paralel dan efisien.
        response.content.map { rawItem ->
            async {
                val detail = try {
                    detailCache[rawItem.id] ?: apiService.getDigimonDetail(rawItem.id).also {
                        detailCache[rawItem.id] = it
                    }
                } catch (e: Exception) {
                    null
                }

                DigimonCardItem(
                    id = rawItem.id,
                    name = rawItem.name,
                    imageUrl = detail?.images?.firstOrNull()?.href ?: rawItem.image,
                    level = detail?.levels?.firstOrNull()?.level ?: "-",
                    attribute = detail?.attributes?.firstOrNull()?.attribute ?: "-",
                    type = detail?.types?.firstOrNull()?.type ?: "-"
                )
            }
        }.awaitAll()
    }

    override suspend fun getDigimonDetail(id: Int): DigimonDetailResponse = withContext(Dispatchers.IO) {
        detailCache[id] ?: apiService.getDigimonDetail(id).also {
            detailCache[id] = it
        }
    }
}

package com.example.digimonexplorer.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model response untuk list Digimon dari endpoint:
 * GET https://digi-api.com/api/v1/digimon
 */
data class DigimonListResponse(
    @SerializedName("content")
    val content: List<DigimonListItem> = emptyList()
)

/**
 * Model item mentah yang diperoleh dari endpoint list Digi-API
 */
data class DigimonListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("href")
    val href: String? = null,
    @SerializedName("image")
    val image: String? = null
)

/**
 * Model response untuk detail Digimon dari endpoint:
 * GET https://digi-api.com/api/v1/digimon/{id}
 */
data class DigimonDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("xAntibody")
    val xAntibody: Boolean = false,
    @SerializedName("images")
    val images: List<DigimonImage> = emptyList(),
    @SerializedName("levels")
    val levels: List<DigimonLevel> = emptyList(),
    @SerializedName("attributes")
    val attributes: List<DigimonAttribute> = emptyList(),
    @SerializedName("types")
    val types: List<DigimonType> = emptyList(),
    @SerializedName("descriptions")
    val descriptions: List<DigimonDescription> = emptyList(),
    @SerializedName("skills")
    val skills: List<DigimonSkill> = emptyList()
)

data class DigimonImage(
    @SerializedName("href")
    val href: String? = null,
    @SerializedName("transparent")
    val transparent: Boolean = false
)

data class DigimonLevel(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("level")
    val level: String? = null
)

data class DigimonAttribute(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("attribute")
    val attribute: String? = null
)

data class DigimonType(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("type")
    val type: String? = null
)

data class DigimonDescription(
    @SerializedName("origin")
    val origin: String? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("description")
    val description: String? = null
)

data class DigimonSkill(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("skill")
    val skill: String? = null,
    @SerializedName("description")
    val description: String? = null
)

/**
 * Model representasi UI untuk item Digimon pada Home Screen.
 * Memuat semua informasi minimal yang diwajibkan oleh spesifikasi responsi:
 * 1. Nama Digimon
 * 2. Level
 * 3. Attribute
 * 4. Type
 * Ditambah nilai keunggulan berupa Gambar Digimon (opsional/nilai tambah).
 */
data class DigimonCardItem(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val level: String,
    val attribute: String,
    val type: String
)

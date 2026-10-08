# Aplikasi Eksplorasi Digimon 🦖📱

Aplikasi mobile berbasis Android modern untuk menjelajahi dan mempelajari berbagai jenis Digimon, atribut, level, dan tipe dari Dunia Digital. Proyek ini dikembangkan menggunakan **Kotlin**, **Jetpack Compose (Material Design 3)**, arsitektur **MVVM murni**, dan mengonsumsi data resmi dari **Digi-API (DAPI)**.

---

## 🛠️ Tech Stack & Library

* **Bahasa:** Kotlin (Memanfaatkan *Data Class*, *Null Safety*, *Lambda*, *Collections*, dan *Coroutines*)
* **UI Toolkit:** Jetpack Compose + Material Design 3 (**100% No XML Layout**)
* **Arsitektur:** MVVM (Model - View - ViewModel - Repository)
* **Asynchronous & Concurrency:** Kotlin Coroutines & StateFlow
* **Networking:** Retrofit 2 + Gson Converter
* **Image Loading:** Coil Compose
* **Navigasi:** Jetpack Compose Navigation (Maksimal 2 screen)
* **Theme & Typography:** Custom Material 3 Theme (Digital Blue & Cyber Orange) dengan Custom Typography

---

## 🏛️ Arsitektur Aplikasi (MVVM)

Aplikasi dibangun dengan pemisahan tanggung jawab yang ketat (*Separation of Concerns*) mengikuti kaidah MVVM resmi Android:

```text
               ┌────────────────────────────────────────────────────────┐
               │                         VIEW                           │
               │  HomeScreen (LazyVerticalGrid) / DetailScreen (Compose)│
               └───────────────────────────▲────────────────────────────┘
                                           │ Observes StateFlow (UiState)
                                           ▼
               ┌────────────────────────────────────────────────────────┐
               │                      VIEWMODEL                         │
               │         HomeViewModel / DetailViewModel                │
               └───────────────────────────▲────────────────────────────┘
                                           │ Coroutine Suspend Functions
                                           ▼
               ┌────────────────────────────────────────────────────────┐
               │                     REPOSITORY                         │
               │            DigimonRepositoryImpl (Cache)               │
               └───────────────────────────▲────────────────────────────┘
                                           │ Network Calls
                                           ▼
               ┌────────────────────────────────────────────────────────┐
               │                     DATA SOURCE                        │
               │         DigimonApiService (Retrofit + DAPI)            │
               └────────────────────────────────────────────────────────┘
```

1. **Model (`data/model`):**
   * Merepresentasikan struktur data dari endpoint Digi-API (`DigimonListResponse`, `DigimonDetailResponse`) serta UI Model `DigimonCardItem` yang memuat data minimal wajib (Nama, Level, Attribute, Type, Gambar).
2. **Repository (`data/repository`):**
   * Mengabstraksi pengambilan data dari network.
   * Menggunakan coroutine paralel `async`/`awaitAll` untuk mengambil atribut detail Digimon dan menyimpannya pada *in-memory cache* (`ConcurrentHashMap`) agar perpindahan ke layar detail instan dan responsif.
3. **ViewModel (`ui/home`, `ui/detail`):**
   * Mengelola *state* UI menggunakan `StateFlow<UiState<T>>`.
   * Menangani pemanggilan fungsi repository secara asynchronous menggunakan `viewModelScope`.
4. **View / UI Screen (`ui/home`, `ui/detail`):**
   * Menampilkan UI deklaratif dengan Jetpack Compose.
   * Menangani ketiga state: `Loading` (indikator loading), `Error` (pesan kesalahan + tombol Coba Lagi), dan `Success` (data Digimon).

---

## 📁 Struktur File & Folder

```
com.example.digimonexplorer/
│
├── data/
│   ├── model/
│   │   └── DigimonModels.kt        # DTO Retrofit dan Domain UI Model DigimonCardItem
│   ├── remote/
│   │   └── DigimonApiService.kt    # Interface Retrofit & Singleton RetrofitClient
│   └── repository/
│       └── DigimonRepository.kt    # Abstraksi dan implementasi repository + cache
│
├── ui/
│   ├── common/
│   │   └── UiState.kt              # Sealed interface untuk state Loading, Success, Error
│   ├── home/
│   │   ├── HomeScreen.kt           # Screen daftar Digimon (LazyVerticalGrid + Cards)
│   │   └── HomeViewModel.kt        # ViewModel pengelola state daftar Digimon
│   ├── detail/
│   │   ├── DetailScreen.kt         # Screen detail Digimon (info lengkap & tombol back)
│   │   └── DetailViewModel.kt      # ViewModel pengelola state detail Digimon
│   ├── navigation/
│   │   └── NavGraph.kt             # Jetpack Compose Navigation (Home -> Detail)
│   ├── theme/
│   │   ├── Color.kt                # Custom Palet Warna Material 3
│   │   ├── Theme.kt                # Custom Material 3 Theme (Light & Dark)
│   │   └── Type.kt                 # Custom Typography Material 3
│   └── ViewModelFactory.kt         # Factory untuk Dependency Injection ViewModel
│
└── MainActivity.kt                 # Entry point aplikasi (Surface & NavGraph)
```

---

## 📱 Spesifikasi Layar & Fitur

### 1. Home Screen
* Menampilkan daftar Digimon dalam format **LazyVerticalGrid** (2 kolom).
* **Minimal Data pada Setiap Card:**
  * ✅ Nama Digimon
  * ✅ Level (e.g., Child, Adult, Ultimate)
  * ✅ Attribute (e.g., Vaccine, Data, Virus)
  * ✅ Type (e.g., Reptile, Beast)
  * 🌟 Nilai tambah: Foto/Gambar Digimon dari API menggunakan library Coil.
  * Badge ID Digimon (`#1`, `#2`, dll).
* **State Handling:**
  * **Loading:** `CircularProgressIndicator` dengan teks informatif saat data diunduh.
  * **Error:** Pesan kesalahan deskriptif dan tombol **Coba Lagi (Retry)**.
  * **Data:** Menampilkan grid kartu Digimon secara interaktif.
* Klik kartu Digimon untuk navigasi langsung ke Detail Screen.

### 2. Digimon Detail Screen
* Menampilkan informasi detail lengkap dari Digimon yang dipilih:
  * Gambar besar Digimon.
  * Badge ID dan nama Digimon.
  * Tag *X-Antibody* (jika ada).
  * Statistik utama: Level, Attribute, dan Type.
  * Deskripsi/profil Digimon dalam bahasa Inggris (jika tersedia dari database API).
  * Jurus / keterampilan (*skills*) utama Digimon beserta deskripsinya.
* **Tombol Navigasi Kembali:** Dilengkapi tombol panah kembali pada `TopAppBar` serta tombol "*Kembali ke Beranda*" di bagian bawah layar.

---

## 🌐 Digi-API (DAPI) Endpoints

* **Base URL:** `https://digi-api.com/api/v1/`
* **Daftar Digimon:** `GET https://digi-api.com/api/v1/digimon?page=0&pageSize=20`
* **Detail Digimon:** `GET https://digi-api.com/api/v1/digimon/{id}`

---

## 🚀 Cara Menjalankan Aplikasi

1. Clone repositori ini:
   ```bash
   git clone <URL_REPOSITORY_ANDA>
   ```
2. Buka folder proyek di **Android Studio** (Koala / Ladybug atau versi terbaru).
3. Pastikan JDK yang digunakan adalah **JDK 17 atau JDK 21**.
4. Tunggu proses *Gradle Sync* selesai.
5. Jalankan aplikasi pada emulator atau perangkat fisik Android (Min SDK 24 / Android 7.0+).

---

## 📸 Tangkapan Layar (Screenshots)

| Home Screen (List) | Detail Screen (Detail) |
|:------------------:|:----------------------:|
| *(Tambahkan screenshot Home di sini)* | *(Tambahkan screenshot Detail di sini)* |

*(Ambil screenshot saat menjalankan aplikasi pada emulator atau HP Anda dan letakkan pada folder `screenshots/`)*

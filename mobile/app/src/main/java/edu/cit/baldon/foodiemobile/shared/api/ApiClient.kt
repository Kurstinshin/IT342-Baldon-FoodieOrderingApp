package edu.cit.baldon.foodiemobile.shared.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // ── Change BASE_URL to your Render URL for production builds ─────────────
    // Emulator → 10.0.2.2 maps to host machine's localhost
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    // Token is injected at runtime via setToken()
    private var token: String? = null

    fun setToken(t: String?) { token = t }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = if (!token.isNullOrBlank()) {
                    original.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else original
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()

    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

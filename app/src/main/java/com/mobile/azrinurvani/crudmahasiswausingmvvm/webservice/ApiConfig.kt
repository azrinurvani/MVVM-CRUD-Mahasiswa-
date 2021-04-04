package com.mobile.azrinurvani.crudmahasiswausingmvvm.webservice

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Logger

object ApiConfig {

    const val BASE_URL ="http://192.168.43.117:8080/server_mahasiswa/index.php/Server/"

//    private val interceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }

    //Untuk LOG API (End point)
//    private val gson = Gson()
//    private val gsonBuilder = GsonBuilder().setLenient().create()
    private val interceptor = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger{
        override fun log(message: String) {
            //Log.d("AZR-API",gson.fromJson(message))
            Log.d("AZR-API",message)
        }

    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    fun config(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun service(): ApiService{
        return config().create(ApiService::class.java)
    }
}
//
//inline  fun<reified T> Gson.fromJson(message: String) =this.fromJson<T>(message, object : TypeToken<T>() {}.type)
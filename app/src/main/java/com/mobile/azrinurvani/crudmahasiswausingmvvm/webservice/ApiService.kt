package com.mobile.azrinurvani.crudmahasiswausingmvvm.webservice

import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.ResponseGetMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.ResponseInsertMahasiswa
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("getMahasiswa")
    fun getAllMahasiswa(): Call<ResponseGetMahasiswa?>?

    @FormUrlEncoded
    @POST("insertMahasiswa")
    fun insertMahasiswa(
        @Field("nim") nim: String?,
        @Field("name") nama: String?,
        @Field("majors") jurusan: String?,
        @Field("jekel") jekel: String?,
        @Field("email") email: String?
    ): Call<ResponseInsertMahasiswa?>?

    @FormUrlEncoded
    @POST("updateMahasiswa")
    fun updateMahasiswa(
        @Field("id") id: String?,
        @Field("nim") nim: String?,
        @Field("name") nama: String?,
        @Field("majors") jurusan: String?,
        @Field("jekel") jekel: String?,
        @Field("email") email: String?
    ): Call<ResponseInsertMahasiswa?>?

    @FormUrlEncoded
    @POST("deleteMahasiswa")
    fun deleteMahasiswa(@Field("id") id: String?): Call<ResponseInsertMahasiswa?>?
}
package com.mobile.azrinurvani.crudmahasiswausingmvvm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mobile.azrinurvani.crudmahasiswausingmvvm.MainActivity
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.DataMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.ResponseGetMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.ResponseInsertMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.view.UpdateHapusActivity
import com.mobile.azrinurvani.crudmahasiswausingmvvm.view.adapter.MahasiswaAdapter
import com.mobile.azrinurvani.crudmahasiswausingmvvm.webservice.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(context: Context): AndroidViewModel(context as Application) {

    val context = context
    var dataMahasiswa :MutableLiveData<ArrayList<DataMahasiswa>> = MutableLiveData()
    var recyclerMahasiswaAdapter : MahasiswaAdapter = MahasiswaAdapter()

    fun getDataMahasiswaObserver() : MutableLiveData<ArrayList<DataMahasiswa>> {
        return dataMahasiswa
    }

    fun getAllMahasiswaFromApi(){
        ApiConfig.service().getAllMahasiswa()?.enqueue(object : Callback<ResponseGetMahasiswa?> {
            override fun onFailure(call: Call<ResponseGetMahasiswa?>, t: Throwable) {
                Log.e("MainViewModel","getAllMahasiswa.onFailure() : ${t.localizedMessage}")
                Toast.makeText(context,"getAllMahasiswa.onFailure : ${t.localizedMessage}",Toast.LENGTH_SHORT).show()
                dataMahasiswa.postValue(null)
            }

            override fun onResponse(call: Call<ResponseGetMahasiswa?>, response: Response<ResponseGetMahasiswa?>) {
                if (response.isSuccessful){
                    if (response.body()?.status == 1){
                        val data = response.body()?.dataMahasiswa as ArrayList<DataMahasiswa>?
                        dataMahasiswa.postValue(data)
                        Log.d("MainViewModel","getAllMahasiswa.onResponse DATA : $data")

                    }
                }else{
                    dataMahasiswa.postValue(null)
                }
            }
        })
    }

    fun insertMahasiswaFromApi(nim:String,name:String,majors:String,jekel:String,email:String){
        ApiConfig.service().insertMahasiswa(nim,name,majors,jekel,email)?.enqueue(object : Callback<ResponseInsertMahasiswa?> {
            override fun onFailure(call: Call<ResponseInsertMahasiswa?>, t: Throwable) {
                Log.e(TAG, "insertMahaiswaFromApi.onFailure: ${t.localizedMessage}")
                Toast.makeText(context,"insertMahaiswaFromApi.onFailure : ${t.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseInsertMahasiswa?>, response: Response<ResponseInsertMahasiswa?>) {
               if (response.isSuccessful){
                   if (response.body()?.status ==1){
                        Log.d(TAG,"insertMahasiswa.onResponse : ${response.body()?.status}")
                        Toast.makeText(getApplication(),"Insert success",Toast.LENGTH_SHORT).show()

                   }else{
                       Log.d(TAG,"insertMahasiswa.onResponse : ${response.body()?.status}")
                       Toast.makeText(getApplication(),"Insert failed : msg : ${response.body()?.pesan}",Toast.LENGTH_SHORT).show()

                   }
               }
            }
        })
    }

    fun updateMahasiswa(id:String,nim:String,name:String,majors:String,jekel:String,email:String){
        ApiConfig.service().updateMahasiswa(id,nim,name,majors,jekel,email)?.enqueue(object: Callback<ResponseInsertMahasiswa?>{
            override fun onFailure(call: Call<ResponseInsertMahasiswa?>, t: Throwable) {
                Log.e(TAG,"updateMahasiswa.onFailure : msg : ${t.localizedMessage} ")
                Toast.makeText(context,"updateMahasiswa.onFailure : ${t.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseInsertMahasiswa?>, response: Response<ResponseInsertMahasiswa?>) {
                if (response.isSuccessful){
                    if (response.body()?.status ==1){
                        Log.d(TAG,"updateMahasiswa.onResponse ${response.body()?.status} ")
                        val intent = Intent(context,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                        //(context as Activity).startActivityForResult(intent,200)
                        context.startActivity(intent)

                    }else{
                        Log.e(TAG, "updateMahasiswa.onResponse.failed : ${response.body()?.pesan}" )
                    }
                }
            }

        })
    }

    fun deleteMahasiswa(id:String){
        ApiConfig.service().deleteMahasiswa(id)?.enqueue(object:Callback<ResponseInsertMahasiswa?>{
            override fun onFailure(call: Call<ResponseInsertMahasiswa?>, t: Throwable) {
                Log.e(TAG, "deleteMahasiswa.onFailure: msg : ${t.localizedMessage}" )
                Toast.makeText(context,"deleteMahasiswa.onFailure : ${t.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseInsertMahasiswa?>, response: Response<ResponseInsertMahasiswa?>) {
                if (response.isSuccessful){
                    val status =response.body()?.status
                    if ( status==1){
                        Log.d(TAG, "deleteMahasiswa.onResponse.success" )
                        val intent = Intent(context,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }else{
                        Log.e(TAG, "deleteMahasiswa.onResponse.failed: msg : ${response.body()?.pesan}" )
                        Toast.makeText(context,"deleteMahasiswa.onFailure : ${response.body()?.pesan}",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } )
    }


    //Recycler Adapter
    fun getRecyclerAdapter(): MahasiswaAdapter{
        return recyclerMahasiswaAdapter
    }

    fun setRecyclerAdapter(data: ArrayList<DataMahasiswa>){
        recyclerMahasiswaAdapter.setAdapter(data)
        recyclerMahasiswaAdapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}

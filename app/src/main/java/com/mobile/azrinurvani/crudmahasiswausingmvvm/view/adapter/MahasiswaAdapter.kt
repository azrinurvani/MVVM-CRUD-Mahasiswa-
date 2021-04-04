package com.mobile.azrinurvani.crudmahasiswausingmvvm.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azrinurvani.crudmahasiswausingmvvm.databinding.ListMahasiswaBinding
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.DataMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.view.UpdateHapusActivity


class MahasiswaAdapter : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    var listMahasiswa = ArrayList<DataMahasiswa>()
    private lateinit var bindingLayout : ListMahasiswaBinding

    fun setAdapter(data : ArrayList<DataMahasiswa>){
        this.listMahasiswa = data
    }

    fun getDataFromAdapter(position: Int): DataMahasiswa{
        return listMahasiswa[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        bindingLayout = ListMahasiswaBinding.inflate(layoutInflater,parent,false)
        return MahasiswaViewHolder(bindingLayout)
    }

    override fun getItemCount(): Int {
        if (listMahasiswa!=null){
            return listMahasiswa?.size!!
        }else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        holder.nim.text = listMahasiswa?.get(position)?.mahasiswaNim.toString()
        holder.nama.text = listMahasiswa?.get(position)?.mahasiswaNama.toString()
        holder.email.text = listMahasiswa?.get(position)?.mahasiswaEmail.toString()
        holder.jekel.text = listMahasiswa?.get(position)?.mahasiswaJekel.toString()
        holder.jurusan.text = listMahasiswa?.get(position)?.mahasiswaJurusan.toString()

        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
            val intent = Intent(context,UpdateHapusActivity::class.java)
            val data = listMahasiswa?.get(position)
            intent.putExtra("DATA_MHS",data)
            //(context as Activity).startActivityForResult(intent,200)
            context.startActivity(intent)
        }

    }


    inner class MahasiswaViewHolder(binding : ListMahasiswaBinding) :RecyclerView.ViewHolder(binding.root)  {

        val nim = binding.txtNim
        val nama = binding.txtNama
        val email= binding.txtEmail
        val jekel = binding.txtJekel
        val jurusan = binding.txtJurusan

    }

}
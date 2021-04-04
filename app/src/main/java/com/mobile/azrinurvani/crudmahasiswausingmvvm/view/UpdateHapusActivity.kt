package com.mobile.azrinurvani.crudmahasiswausingmvvm.view


import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.mobile.azrinurvani.crudmahasiswausingmvvm.R
import com.mobile.azrinurvani.crudmahasiswausingmvvm.databinding.ActivityUpdateHapusBinding
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.DataMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.viewmodel.MainViewModel
import com.mobile.azrinurvani.crudmahasiswausingmvvm.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_update_hapus.*

class UpdateHapusActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateHapusBinding
    private lateinit var vmFactory : ViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var data: DataMahasiswa

    val arrayJurusan = arrayOf(
            "Teknologi Informasi",
            "Teknik Mesin",
            "Teknik Sipil",
            "Teknik Elektro",
            "Akuntansi",
            "Adminitrasi Niaga",
            "Bahasa Inggris"
    )

    var majorsSelected : String = ""
    var jekelSelected : String = ""
    var id:String = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateHapusBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(R.color.black)


        setContentView(binding.root)

        data = intent?.getParcelableExtra<DataMahasiswa>("DATA_MHS")!!
        id = data?.mahasiswaId.toString()

        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        vmFactory = ViewModelFactory(this.application)
        viewModel = ViewModelProviders.of(this,vmFactory).get(MainViewModel::class.java)

        setFormUpdate()
        genderSelected()
        setMajorsSelected()
        binding.btnUpdate.setOnClickListener {
            updateMahasiswa()
        }

    }

    private fun setFormUpdate(){

        if (data!=null){
            binding.edtNim.setText(data.mahasiswaNim)
            binding.edtNama.setText(data.mahasiswaNama)
            binding.edtEmail.setText(data.mahasiswaEmail)

            //Radio button auto checked berdasarkan data intent
            if (data?.mahasiswaJekel.equals("Laki-laki")){
                //cara 1
                binding.rbLaki.isChecked = true
                //binding.rgJekel.check(binding.rbLaki.id)

            }else if (data?.mahasiswaJekel.equals("Perempuan")){
                //cara 2
                binding.rgJekel.check(binding.rbPerempuan.id)
            }

        }

    }
    private fun genderSelected(){
        if (rbLaki.isChecked){
            jekelSelected = "Laki-laki"
        }else if (rbPerempuan.isChecked){
            jekelSelected = "Perempuan"
        }
    }

    private fun updateMahasiswa(){
        val nim = binding.edtNim.text.toString()
        val name = binding.edtNama.text.toString()
        val email = binding.edtEmail.text.toString()
        val majors = majorsSelected
        val gender = jekelSelected

        viewModel.updateMahasiswa(id,nim,name,majors,gender,email)

    }


    private fun deleteMahasiswa() {

        val alertDelete = AlertDialog.Builder(this)
        alertDelete.setTitle("Please confirm !")
        alertDelete.setMessage("Are you sure ?")
        alertDelete.setCancelable(false)
        alertDelete.setPositiveButton("Yes",object : DialogInterface.OnClickListener{
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                viewModel.deleteMahasiswa(id)
            }

        })
        alertDelete.setNegativeButton("No",object: DialogInterface.OnClickListener{
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
               dialogInterface?.dismiss()
            }

        })

        alertDelete.show()
    }

    private fun setMajorsSelected(){
        val adapterMajors = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayJurusan)
        adapterMajors.setDropDownViewResource(com.mobile.azrinurvani.crudmahasiswausingmvvm.R.layout.support_simple_spinner_dropdown_item)

        binding.spinnerJurusan.apply {
            adapter = adapterMajors
            onItemSelectedListener = MajorsItemSelectedListener()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_hapus,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.btnHapus -> deleteMahasiswa()
        }

        return super.onOptionsItemSelected(item)
    }


    inner class MajorsItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
            majorsSelected = adapterView?.getItemAtPosition(i).toString()
        }

    }

}
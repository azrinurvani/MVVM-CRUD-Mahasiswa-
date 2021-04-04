package com.mobile.azrinurvani.crudmahasiswausingmvvm

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azrinurvani.crudmahasiswausingmvvm.databinding.ActivityMainBinding
import com.mobile.azrinurvani.crudmahasiswausingmvvm.databinding.DialogInputBinding
import com.mobile.azrinurvani.crudmahasiswausingmvvm.model.DataMahasiswa
import com.mobile.azrinurvani.crudmahasiswausingmvvm.viewmodel.MainViewModel
import com.mobile.azrinurvani.crudmahasiswausingmvvm.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var vmFactory : ViewModelFactory

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

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(R.color.black)

        vmFactory = ViewModelFactory(this.application)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java) --> tidak menggunakan VMFactory
        viewModel = ViewModelProviders.of(this,vmFactory).get(MainViewModel::class.java)

        retrievingData()
        setupRecylcer()
        binding.fabAdd.setOnClickListener {
            dialogInput()
        }
        itemTouchHelperListener()
    }

    private fun retrievingData(){
        viewModel.getDataMahasiswaObserver().observe(this, Observer<ArrayList<DataMahasiswa>> {
            if (it!=null){
                //update adapter
                viewModel.setRecyclerAdapter(it)
            }else{
                Toast.makeText(this@MainActivity,"Error in fetching data...", Toast.LENGTH_LONG).show()
            }

        })
        viewModel.getAllMahasiswaFromApi()
    }


    private fun dialogInput(){
        val bindingDialog : DialogInputBinding = DialogInputBinding.inflate(layoutInflater)

        val adapterMajors = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayJurusan)
        adapterMajors.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        bindingDialog.spinnerJurusan.apply {
            adapter = adapterMajors
            onItemSelectedListener = MajorsItemSelectedListener()
        }


        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(bindingDialog.root)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle("Input Data")
        dialogBuilder.setPositiveButton("Save",object : DialogInterface.OnClickListener{
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                if (bindingDialog.rbLaki.isChecked){
                    jekelSelected = "Laki-laki"
                }else if (bindingDialog.rbPerempuan.isChecked){
                    jekelSelected = "Perempuan"
                }

                val nim = bindingDialog.edtNim.text.toString()
                val name = bindingDialog.edtNama.text.toString()
                val jekel = jekelSelected
                val majors = majorsSelected
                val email = bindingDialog.edtEmail.text.toString()

                insertData(nim,name,majors,jekel,email)
                dialogInterface?.dismiss()
                recreate()
            }
        })
        dialogBuilder.setNegativeButton("Cancel") { dialogInterface, p1 -> dialogInterface?.dismiss() }

        dialogBuilder.show()
    }


    private fun insertData(nim:String,name:String,majors:String,jekel:String,email:String){
        viewModel.insertMahasiswaFromApi(nim,name,majors,jekel,email)
    }

    private fun setupRecylcer(){
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = viewModel.getRecyclerAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity)
            val decoration = DividerItemDecoration(this@MainActivity,VERTICAL)
            addItemDecoration(decoration)
        }
    }

    private fun itemTouchHelperListener(){
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val dataMahasiswa = viewModel.getRecyclerAdapter()
                Toast.makeText(this@MainActivity, "Deleting ${dataMahasiswa.listMahasiswa[position].mahasiswaNama}", Toast.LENGTH_LONG).show()

                viewModel.deleteMahasiswa(dataMahasiswa.listMahasiswa[position].mahasiswaId.toString())
            }

        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==100){
            //
        }
    }

    inner class MajorsItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
            majorsSelected = adapterView?.getItemAtPosition(i).toString()
        }

    }
}


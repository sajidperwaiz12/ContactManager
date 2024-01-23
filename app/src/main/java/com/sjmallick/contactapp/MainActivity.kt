package com.sjmallick.contactapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sjmallick.contactapp.databinding.ActivityMainBinding
import com.sjmallick.contactapp.mvvmarch.MainActivityViewModel
import com.sjmallick.contactapp.roomdb.entity.Contact

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: MainActivityViewModel
    private var contactList = ArrayList<Contact>()
    private lateinit var contactAdapter: ContactAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            createUI()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            val alertDialog = AlertDialog.Builder(this)
                .setMessage("This app requires call permission to run")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), DIAL_CALL_PERMISSION)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), DIAL_CALL_PERMISSION)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createUI() {
        contactAdapter = ContactAdapter(this, contactList)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.btnAddContact.setOnClickListener {
            startActivity(Intent(this, SaveEditActivity::class.java))
        }

        viewModel.data.observeForever {
            contactList.clear()
            it.map { contact ->
                contactList.add(contact)
            }
            contactList.sortedBy { contact ->
                contact.name
            }
            contactAdapter.notifyDataSetChanged()
        }

        var itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            1,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (ItemTouchHelper.LEFT == direction) {
                    viewModel.deleteContact(contactList[viewHolder.adapterPosition])
                } else {
                    contactList[viewHolder.adapterPosition].phoneNo?.let { initiatePhoneCall(it) }
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = contactAdapter

        contactAdapter.setOnItemClickListener(object : ContactAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val contact = contactList[position]

                Intent(this@MainActivity, SaveEditActivity::class.java).also {
                    it.putExtra("FLAG", 1)
                    it.putExtra("DATA", contact)
                    startActivity(it)
                }
            }
        })
    }

    private fun initiatePhoneCall(phoneNumber: String?) {
        val dialUri = Uri.parse("tel:$phoneNumber")
        val dialIntent = Intent(Intent.ACTION_CALL, dialUri)
        startActivity(dialIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == DIAL_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createUI()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), DIAL_CALL_PERMISSION)
            }
        }
    }
}



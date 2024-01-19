package com.sjmallick.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sjmallick.contactapp.databinding.ActivityMainBinding
import com.sjmallick.contactapp.mvvmarch.MainActivityViewModel
import com.sjmallick.contactapp.roomdb.DbBuilder
import com.sjmallick.contactapp.roomdb.entity.Contact

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var viewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        DbBuilder.getDb(this)?.contactDao()
//            ?.createContact(
//                Contact(name = "Sajid Perwaiz",
//                    phoneNo = "7782826654"))

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.btnAddContact.setOnClickListener {
            startActivity(Intent(this, SaveEditActivity::class.java))
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val contactAdapter = ContactAdapter(this, viewModel!!.contactList)
        binding.recyclerView.adapter = contactAdapter

        contactAdapter.setOnItemClickListener(object : ContactAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val contact = viewModel!!.contactList[position]

                val name = contact.name
                val phoneNo = contact.phoneNo
                val image = contact.profile
                val email = contact.email

                Intent(this@MainActivity, SaveEditActivity::class.java).also {
                    it.putExtra(EXTRA_MODE, MODE_EDIT)
                    it.putExtra("name", name)
                    it.putExtra("phoneNo", phoneNo)
                    it.putExtra("image", image)
                    it.putExtra("email", email)
                    it.putExtra("contact", contact)
                    startActivity(it)
                }
            }

            override fun onCallButtonClick(position: Int) {
                val phoneNumber : String = viewModel!!.contactList[position].phoneNo!!
                initiatePhoneCall(phoneNumber)
            }
        })
    }

    private fun initiatePhoneCall(phoneNumber: String) {
        val dialUri = Uri.parse("tel:$phoneNumber")
        val dialIntent = Intent(Intent.ACTION_CALL, dialUri)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(dialIntent)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        }
    }

    companion object {
        private const val REQUEST_CALL_PERMISSION = 1
        const val EXTRA_MODE = "extra_mode"
        const val MODE_ADD = "add"
        const val MODE_EDIT = "edit"
    }
}



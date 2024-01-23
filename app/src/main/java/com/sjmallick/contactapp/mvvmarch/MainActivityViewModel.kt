package com.sjmallick.contactapp.mvvmarch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sjmallick.contactapp.roomdb.Database
import com.sjmallick.contactapp.roomdb.DbBuilder
import com.sjmallick.contactapp.roomdb.entity.Contact

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var repo: Repo   
    lateinit var data: LiveData<List<Contact>>

    init {
        repo = Repo(application)
        data = repo.getData()!!
    }

    fun deleteContact(contact: Contact) {
        repo.deleteData(contact)
    }

}
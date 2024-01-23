package com.sjmallick.contactapp.mvvmarch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sjmallick.contactapp.roomdb.Database
import com.sjmallick.contactapp.roomdb.DbBuilder
import com.sjmallick.contactapp.roomdb.entity.Contact

class SaveEditActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repo: Repo

    init {
        repo = Repo(application)
    }

    fun storeData(contact: Contact, function : (i: Long?) -> Unit) {
        val i = repo.insertData(contact)
        function(i)
    }

    fun updateData(contact: Contact, function: (i: Int?) -> Unit) {
        function(repo.updateData(contact))
    }

}
package com.sjmallick.contactapp.mvvmarch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sjmallick.contactapp.roomdb.Database
import com.sjmallick.contactapp.roomdb.DbBuilder
import com.sjmallick.contactapp.roomdb.entity.Contact

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var repo: Repo
    var contactList = ArrayList<Contact>()

    init {
        repo = Repo(application)
        repo.getData()?.map {
            contactList.add(it)
        }
    }

}
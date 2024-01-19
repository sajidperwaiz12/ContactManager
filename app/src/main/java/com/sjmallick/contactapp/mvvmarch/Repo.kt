package com.sjmallick.contactapp.mvvmarch

import android.content.Context
import com.sjmallick.contactapp.roomdb.Database
import com.sjmallick.contactapp.roomdb.DbBuilder
import com.sjmallick.contactapp.roomdb.entity.Contact

class Repo(val context: Context) {

    var database: Database? = null

    init {
        database = DbBuilder.getDb(context)
    }

    fun getData() : List<Contact>? {
        return database?.contactDao()?.readContact()
    }

    fun insertData(contact: Contact) : Long? {
        return  database?.contactDao()?.createContact(contact)
    }

    fun deleteData(contact: Contact) {
        database?.contactDao()?.deleteContact(contact)
    }

    fun updateData(contact: Contact) {
        database?.contactDao()?.updateContact(contact)
    }

}
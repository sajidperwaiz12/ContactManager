package com.sjmallick.contactapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sjmallick.contactapp.roomdb.dao.ContactDao
import com.sjmallick.contactapp.roomdb.entity.Contact

@Database(entities = [Contact::class], version = 6, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
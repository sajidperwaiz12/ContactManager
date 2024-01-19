package com.sjmallick.contactapp.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sjmallick.contactapp.roomdb.entity.Contact
import java.io.Serializable

@Dao
interface ContactDao {
    // create
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createContact(contact: Contact) : Long

    // update
    @Update
    fun updateContact(contact: Contact)

    // read
    @Query("SELECT * FROM CONTACT")
    fun readContact(): List<Contact>

    @Query("SELECT * FROM CONTACT WHERE id = :id1")
    fun readContact(id1: Int): Contact

    // delete
    @Delete
    fun deleteContact(contact: Contact)
}
package com.sjmallick.contactapp.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity
class Contact (
    @PrimaryKey
    var id: Int? = null,
    var profile: ByteArray? = null,
    var name: String? = null,
    var phoneNo: String? = null,
    var email: String? = null,
//    var date: Date
) : Serializable
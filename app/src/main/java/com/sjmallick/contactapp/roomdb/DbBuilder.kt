package com.sjmallick.contactapp.roomdb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sjmallick.contactapp.dbName

object DbBuilder {
    private var database: Database? = null

    fun getDb(context: Context): Database? {
        if (database == null) {
            database = Room.databaseBuilder(context,
                Database::class.java,
                dbName).setJournalMode(RoomDatabase.JournalMode.TRUNCATE).fallbackToDestructiveMigration().allowMainThreadQueries().build()
        }

        return database
    }
}
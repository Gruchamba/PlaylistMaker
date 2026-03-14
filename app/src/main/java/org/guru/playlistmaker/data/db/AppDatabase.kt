package org.guru.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.guru.playlistmaker.data.db.dao.TrackDao
import org.guru.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract  class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}
package com.example.gli007.moviedatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [MovieItem::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class MovieRoomDatabase: RoomDatabase(){
    abstract fun movieDao(): MovieItemDao

    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null
        val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movie_table ADD COLUMN liked INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
        fun getDatabase(
            context: Context
        ): MovieRoomDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "Movie_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
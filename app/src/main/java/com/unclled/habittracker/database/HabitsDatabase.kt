package com.unclled.habittracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.unclled.habittracker.database.dao.HabitDao
import com.unclled.habittracker.database.model.HabitEntity
import com.unclled.habittracker.database.model.ReminderTimeEntity

@Database(version = 1, entities = [HabitEntity::class, ReminderTimeEntity::class])
abstract class HabitsDatabase : RoomDatabase() {

    abstract fun getHabitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitsDatabase? = null

        fun getInstance(context: Context): HabitsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    HabitsDatabase::class.java,
                    "habits"
                )
                    //.fallbackToDestructiveMigration()
                    .createFromAsset("reminderTime.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
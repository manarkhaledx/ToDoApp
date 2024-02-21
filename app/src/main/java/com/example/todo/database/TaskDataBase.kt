package com.example.todo.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.database.dao.TasksDao
import com.example.todo.database.model.Task

@Database(entities = [Task::class], version = 2, exportSchema = true)
abstract class TaskDataBase : RoomDatabase() {
    abstract fun getTaskDao(): TasksDao

    companion object { //static
        private const val DATABASE_NAME = "tasks_dataBase"
        private var database: TaskDataBase? = null
        fun init(app: Application) {
            //create
            database = Room.databaseBuilder(
                app.applicationContext,
                TaskDataBase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build() //build obj
        }

        fun getInstance(): TaskDataBase {
            return database!!
        }
    }
}
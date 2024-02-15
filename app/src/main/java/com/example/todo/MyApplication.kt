package com.example.todo

import android.app.Application
import com.example.todo.database.TaskDataBase

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        //initialize
        TaskDataBase.init(this)
    }
}
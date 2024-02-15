package com.example.todo.ui.home.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo
    val title:String,

    @ColumnInfo
    val content:String?=null,
    @ColumnInfo
    val isDone:Boolean=false,
    @ColumnInfo
    val date:Long?=null,
    @ColumnInfo
    val time:Long?=null
){

}

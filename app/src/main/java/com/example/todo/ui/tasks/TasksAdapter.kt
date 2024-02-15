package com.example.todo.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.ui.home.tasks.Task

class TasksAdapter(private var taskList: MutableList<Task> = mutableListOf()) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(task: Task){
           binding.title.text=task.title
            binding.description.text=task.content
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding:ItemTaskBinding=ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int =taskList?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task=taskList!![position]
        holder.bind(task)
    }

    fun changeData(allTasks: List<Task>) {
        if(taskList==null){
            taskList= mutableListOf()
        }
        taskList?.clear()
        taskList?.addAll(allTasks)
        notifyDataSetChanged() // affect performance so don't call it many times (full data changed)
    }
}
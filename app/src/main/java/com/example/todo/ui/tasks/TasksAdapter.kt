package com.example.todo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.TaskDataBase
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.ui.home.tasks.Task
import com.zerobranch.layout.SwipeLayout

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
        val task = taskList!![position]
        holder.bind(task)
        updateUIBasedOnIsDoneState(holder, task.isDone)

        holder.binding.btnTaskIsDone.setOnClickListener {
            task.isDone = true
            TaskDataBase.getInstance().getTaskDao().updateTask(task)
            notifyDataSetChanged()
            updateUIBasedOnIsDoneState(holder, true)
        }
        holder.binding.swipeLayout.setOnActionsListener(object :SwipeLayout.SwipeActionsListener{
            override fun onOpen(direction: Int, isContinuous: Boolean) {
                if(direction == SwipeLayout.RIGHT){

                }else if(direction==SwipeLayout.LEFT){

                }
            }
            override fun onClose() {
            }
        })
    }


    private fun updateUIBasedOnIsDoneState(holder: ViewHolder, isDone: Boolean) {
        if (isDone) {
            holder.binding.btnTaskIsDone.isVisible = false
            holder.binding.tvTaskDone.isVisible = true
        } else {
            holder.binding.btnTaskIsDone.isVisible = true
            holder.binding.tvTaskDone.isVisible = false
        }


    }
    fun interface OnTaskDoneListener{
        fun onTaskDone()

    }

    fun changeData(allTasks: List<Task>) {
        if(taskList==null){
            taskList= mutableListOf()
        }
        taskList?.clear()
        taskList?.addAll(allTasks)
        notifyDataSetChanged() // affect performance so don't call it many times (full data changed)
    }
    fun setSwipeActionsListener(listener: SwipeLayout.SwipeActionsListener) {
        val swipeActionsListener=listener
    }

}
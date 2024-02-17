package com.example.todo.ui.tasks

 import android.content.Context
 import android.view.LayoutInflater
 import android.view.ViewGroup
 import androidx.appcompat.app.AlertDialog
 import androidx.core.view.isVisible
 import androidx.fragment.app.FragmentManager
 import androidx.recyclerview.widget.RecyclerView
 import com.example.todo.database.TaskDataBase
 import com.example.todo.databinding.ItemTaskBinding
 import com.example.todo.ui.addTask.AddTaskButtomSheet
 import com.example.todo.ui.home.tasks.Task
 import com.zerobranch.layout.SwipeLayout


class TasksAdapter(private var taskList: MutableList<Task> = mutableListOf()) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTaskBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.title.text = task.title
            binding.description.text = task.content
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val itemBinding: ItemTaskBinding = ItemTaskBinding.inflate(LayoutInflater.from(context),
            parent, false)
        return ViewHolder(itemBinding, context)
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
                val position = holder.adapterPosition
                if(direction == SwipeLayout.RIGHT){
                    if (position != RecyclerView.NO_POSITION) {
                        val taskToDelete = taskList[position]
                        val builder = AlertDialog.Builder(holder.context)
                        builder.setTitle("Confirm Deletion")
                        builder.setMessage("Are you sure you want to delete this task?")
                        builder.setPositiveButton("Delete") { dialog, which ->
                            TaskDataBase.getInstance().getTaskDao().deleteTask(taskToDelete)
                            taskList.removeAt(position)
                            notifyDataSetChanged()
                        }
                        builder.setNegativeButton("Cancel") { dialog, which ->
                            holder.binding.swipeLayout.close(true)
                        }
                        builder.show()
                    }

                }
                else if(direction==SwipeLayout.LEFT){
                    if (position != RecyclerView.NO_POSITION) {
                        val taskToEdit = taskList[position]
                            TaskDataBase.getInstance().getTaskDao().updateTask(taskToEdit)
                            notifyDataSetChanged()
                        }

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

    fun changeData(allTasks: List<Task>) {
        if(taskList==null){
            taskList= mutableListOf()
        }
        taskList?.clear()
        taskList?.addAll(allTasks)
        notifyDataSetChanged() // affect performance so don't call it many times (full data changed)
    }



}
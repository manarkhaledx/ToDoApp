package com.example.todo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.database.TaskDataBase
import com.example.todo.databinding.FragmentTasksBinding

class TasksListFragment:Fragment() {
    private lateinit var viewBinding: FragmentTasksBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =FragmentTasksBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        retrieveTaskList()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

    }

    private fun retrieveTaskList() {
        val allTasks=TaskDataBase.getInstance().getTaskDao().getAllTasks()
        adapter.changeData(allTasks)

    }

    private val adapter= TasksAdapter()
    private fun setUpRecyclerView() {
        viewBinding.rvTask.adapter=adapter
    }
}
package com.example.todo.ui.tasks

import CalendarExtensions.getDateOnly
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.database.TaskDataBase
import com.example.todo.databinding.FragmentTasksBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar

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
        setUpViews()

    }
val currentDate=Calendar.getInstance()
     fun retrieveTaskList() {
        val allTasks=TaskDataBase.getInstance().getTaskDao().getTasksByDate(currentDate.getDateOnly())
        adapter.changeData(allTasks)

    }

    private val adapter= TasksAdapter()
    private fun setUpViews() {
        viewBinding.rvTask.adapter=adapter
        viewBinding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if(selected){
                currentDate.set(date.year,date.month-1 ,date.day )
                retrieveTaskList()
            }
        }
        viewBinding.calendarView.selectedDate=CalendarDay.today()
    }
}
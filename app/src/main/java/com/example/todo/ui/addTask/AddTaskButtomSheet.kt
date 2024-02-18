package com.example.todo.ui.addTask

import CalendarExtensions.formateDate
import CalendarExtensions.formateTime
import CalendarExtensions.getDateOnly
import CalendarExtensions.getTimeOnly
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.todo.database.TaskDataBase
import com.example.todo.databinding.FragmentAddTaskBinding
import com.example.todo.ui.home.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTaskButtomSheet : BottomSheetDialogFragment() {
    private lateinit var viewbinding: FragmentAddTaskBinding
    private var taskToEdit: Task? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewbinding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return viewbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        taskToEdit?.let { populateFields(it) }

    }

    private fun setUpViews() {
        viewbinding.addTaskBtn.setOnClickListener {
            addTask()
        }
        viewbinding.dateTil.setOnClickListener {
            showDatePicker()
        }
        viewbinding.timeTil.setOnClickListener {
            showTimePicker()
        }
        viewbinding.title.addTextChangedListener {
            viewbinding.titleTil.error = null
        }

        viewbinding.description.addTextChangedListener {
            viewbinding.descriptionTil.error = null
        }
    }

    private fun showTimePicker() {
        val timePickerDialog =
            TimePickerDialog(requireActivity(),
                { view, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    viewbinding.timeTv.text = calendar.formateTime()
                    viewbinding.timeTil.error=null
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                false)
        timePickerDialog.show()
    }

    val calendar: Calendar = Calendar.getInstance()

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                viewbinding.dateTv.text = calendar.formateDate()
                viewbinding.dateTil.error=null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun isValidTaskInput(): Boolean {
        var isValid = true
        val title = viewbinding.title.text.toString()
        val description = viewbinding.description.text.toString()
        if (title.isBlank()) {
            viewbinding.titleTil.error = "Please Enter Task Title"
            isValid = false
        }
        if (description.isBlank()) {
            viewbinding.descriptionTil.error = "Please Enter Task Description"
            isValid = false
        }
        if(viewbinding.timeTv.text.isBlank()){
            viewbinding.timeTil.error="Please Select Time"
            isValid = false
        }
        if(viewbinding.dateTv.text.isBlank()){
            viewbinding.dateTil.error="Please Select Date"
            isValid = false
        }
        return isValid
    }

    private fun addTask() {
        if (!isValidTaskInput()) {
            return
        }
        val calendar = Calendar.getInstance() // Create a new calendar instance
        TaskDataBase.getInstance().getTaskDao().insertTask(Task(
            title = viewbinding.title.text.toString(),
            content = viewbinding.description.text.toString(),
            date = calendar.getDateOnly(),
            time = calendar.getTimeOnly()
        ))
        Toast.makeText(requireContext(), "Task Added Successfully", Toast.LENGTH_SHORT).show()
        dismiss()
        onTaskAddedListener?.onTaskAdded()

    }




    private fun populateFields(task: Task) {
        viewbinding.title.setText(task.title)
        viewbinding.description.setText(task.content)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = task.date!!
        }
        viewbinding.dateTv.text = calendar.formateDate()
        viewbinding.timeTv.text = calendar.formateTime()
    }


    // Your existing methods

    fun editTask(task: Task) {
        taskToEdit = task
    }

    var onTaskAddedListener: OnTaskAddedListener?=null
    fun interface OnTaskAddedListener{
        fun onTaskAdded()

    }






}
package com.example.todo.ui.tasks

import CalendarExtensions.formatDate
import CalendarExtensions.formatTime
import CalendarExtensions.getDateOnly
import CalendarExtensions.getTimeOnly
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.database.TaskDataBase
import com.example.todo.database.model.Task
import com.example.todo.databinding.FragmentEditTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import java.util.Locale

class EditTaskButtomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEditTaskBinding
    var taskToEdit: Task? = null
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskToEdit?.let { populateFields(it) }
        setupListeners()
        setupDateTimePickers()
    }

    private fun setupDateTimePickers() {
        binding.dateTv.setOnClickListener {
            showDatePicker()
        }

        binding.timeTv.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val timePickerDialog =
            TimePickerDialog(
                requireActivity(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    binding.timeTv.text = calendar.formatTime()
                    binding.timeTil.error = null
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                false
            )
        timePickerDialog.show()
    }



    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->

                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                binding.dateTv.text = calendar.formatDate()
                binding.dateTil.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }



    private fun populateFields(task: Task) {
        binding.title.setText(task.title)
        binding.description.setText(task.content)
        calendar.timeInMillis = task.time!!
        calendar.timeInMillis = task.date!!
        binding.dateTv.text = calendar.formatDate()
        val formatter = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formatDate=formatter.format(task.time)
        binding.timeTv.text = formatDate
        binding.checkBox.isChecked = task.isDone
    }


    private fun setupListeners() {
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            updateIsDoneStatus(isChecked)

        }
        binding.saveTaskBtn.setOnClickListener {
            saveChanges()
        }
    }
    private fun updateIsDoneStatus(isChecked: Boolean) {
        taskToEdit?.let {
            it.isDone = isChecked
            TaskDataBase.getInstance().getTaskDao().updateTask(it)
            Log.d("test", "${it.title},${it.content},${it.time},${it.date},${it.isDone}")
        }
    }
    private fun saveChanges() {
        val newTitle = binding.title.text.toString()
        val newDescription = binding.description.text.toString()
        taskToEdit?.let {
            it.title = newTitle
            it.content = newDescription
            it.date = calendar.getDateOnly()
            it.time = calendar.getTimeOnly()
            it.isDone = binding.checkBox.isChecked
            TaskDataBase.getInstance().getTaskDao().updateTask(it)
            Log.d("test","${it.title},${it.content},${it.time},${it.date},${it.isDone}")
        }
        dismiss()
    }

}

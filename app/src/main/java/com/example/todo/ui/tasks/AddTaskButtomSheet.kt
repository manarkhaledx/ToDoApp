package com.example.todo.ui.tasks

import CalendarExtensions.formatDate
import CalendarExtensions.formatTime
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
import com.example.todo.database.model.Task
import com.example.todo.databinding.FragmentAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTaskButtomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()


    }

    private fun setUpViews() {
        binding.addTaskBtn.setOnClickListener {
            addTask()
        }
        binding.dateTil.setOnClickListener {
            showDatePicker()
        }
        binding.timeTil.setOnClickListener {
            showTimePicker()
        }
        binding.title.addTextChangedListener {
            binding.titleTil.error = null
        }

        binding.description.addTextChangedListener {
            binding.descriptionTil.error = null
        }
    }

    private fun showTimePicker() {
        val timePickerDialog =
            TimePickerDialog(
                requireActivity(),
                { view, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    binding.timeTv.text = calendar.formatTime()
                    binding.timeTil.error = null
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                false
            )
        timePickerDialog.show()
    }

    private val calendar: Calendar = Calendar.getInstance()

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

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

    private fun isValidTaskInput(): Boolean {
        var isValid = true
        val title = binding.title.text.toString()
        val description = binding.description.text.toString()
        if (title.isBlank()) {
            binding.titleTil.error = "Please Enter Task Title"
            isValid = false
        }
        if (description.isBlank()) {
            binding.descriptionTil.error = "Please Enter Task Description"
            isValid = false
        }
        if (binding.timeTv.text.isBlank()) {
            binding.timeTil.error = "Please Select Time"
            isValid = false
        }
        if (binding.dateTv.text.isBlank()) {
            binding.dateTil.error = "Please Select Date"
            isValid = false
        }
        return isValid
    }

    private fun addTask() {
        if (!isValidTaskInput()) {
            return
        }
        TaskDataBase.getInstance().getTaskDao().insertTask(
            Task(
                title = binding.title.text.toString(),
                content = binding.description.text.toString(),
                date = calendar.getDateOnly(),
                time = calendar.getTimeOnly()
            )
        )
        Toast.makeText(requireContext(), "Task Added Successfully", Toast.LENGTH_SHORT).show()
        dismiss()
        onTaskAddedListener?.onTaskAdded()
    }



    var onTaskAddedListener: OnTaskAddedListener? = null

    fun interface OnTaskAddedListener {
        fun onTaskAdded()

    }


}
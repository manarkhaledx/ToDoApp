package com.example.todo.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.ActivityHomeBinding
import com.example.todo.ui.addTask.AddTaskButtomSheet
import com.example.todo.ui.settings.SettingsFragment
import com.example.todo.ui.tasks.TasksListFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupViews()
    }

    private fun setupViews()  {
        viewBinding.bottomNavigation.setOnItemSelectedListener {
            if (it.itemId == R.id.nav_task) {
                showFragment(TasksListFragment())
            } else {
                showFragment(SettingsFragment())
            }
            return@setOnItemSelectedListener true
        }
        viewBinding.bottomNavigation.selectedItemId = R.id.nav_task
        viewBinding.fabAddTask.setOnClickListener {
            showAddTaskBottomSheet()
        }
    }

    private var currentBottomSheet: BottomSheetDialogFragment? = null
    private fun showAddTaskBottomSheet() {
        currentBottomSheet?.dismissAllowingStateLoss()
        val addTaskBottomSheet = AddTaskButtomSheet()
        addTaskBottomSheet.onTaskAddedListener=AddTaskButtomSheet.OnTaskAddedListener {
            //notifity task list fragment
            supportFragmentManager.fragments.forEach{fragment->
                if(fragment is TasksListFragment && fragment.isAdded){
                    fragment.retrieveTaskList()
                }
            }
        }
        addTaskBottomSheet.show(supportFragmentManager, null)
        currentBottomSheet = addTaskBottomSheet
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}
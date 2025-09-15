package com.andro.gallery.alarmappdemo.ui

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andro.gallery.alarmappdemo.databinding.ActivityMainBinding
import com.andro.gallery.alarmappdemo.viewmodel.ReminderListViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: ReminderListViewModel by viewModels()
    private val adapter = ReminderAdapter(
        onEdit = { id ->
            startActivity(
                Intent(this, AddEditReminderActivity::class.java).putExtra(
                    AddEditReminderActivity.EXTRA_ID,
                    id
                )
            )
        },
        onDelete = { reminder -> vm.delete(reminder) }
    )

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestNotificationPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }


        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter


        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditReminderActivity::class.java))
        }


        // Observe data
        lifecycleScope.launch {
            vm.reminders.collect { list ->
                adapter.submitList(list)
            }
        }

    }
}
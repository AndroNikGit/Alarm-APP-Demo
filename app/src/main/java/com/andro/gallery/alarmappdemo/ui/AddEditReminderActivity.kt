package com.andro.gallery.alarmappdemo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andro.gallery.alarmappdemo.databinding.ActivityAddEditBinding
import com.andro.gallery.alarmappdemo.util.DateTimeUtils
import com.andro.gallery.alarmappdemo.viewmodel.AddEditViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class AddEditReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private val vm: AddEditViewModel by viewModels()
    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        editingId = intent.getLongExtra(EXTRA_ID, -1L).takeIf { it > 0 }


// Load if editing
        if (editingId != null) {
            lifecycleScope.launch {
                val r = vm.load(editingId!!)
                if (r != null) {
                    vm.name.value = r.name
                    vm.details.value = r.details
                    vm.timeMillis.value = r.timeMillis
                    binding.edtName.setText(r.name)
                    binding.edtDetails.setText(r.details)
                    binding.txtWhen.text = DateTimeUtils.format(r.timeMillis)
                }
            }
        } else {
            binding.txtWhen.text = DateTimeUtils.format(vm.timeMillis.value!!)
        }


        binding.btnPickDateTime.setOnClickListener { pickDateTime() }


        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val details = binding.edtDetails.text.toString().trim()
            if (name.isEmpty()) { Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            vm.name.value = name
            vm.details.value = details
            vm.save(editingId) {
                Toast.makeText(this, "Saved & scheduled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun pickDateTime() {
        val cal = Calendar.getInstance().apply { timeInMillis = vm.timeMillis.value ?: System.currentTimeMillis() }
        DatePickerDialog(this, { _, y, m, d ->
            cal.set(Calendar.YEAR, y)
            cal.set(Calendar.MONTH, m)
            cal.set(Calendar.DAY_OF_MONTH, d)
            TimePickerDialog(this, { _, h, min ->
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, min)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val chosen = cal.timeInMillis
                vm.timeMillis.value = chosen
                binding.txtWhen.text = DateTimeUtils.format(chosen)
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    companion object { const val EXTRA_ID = "extra_id" }
}
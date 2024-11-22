package com.example.taskmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.taskmanager.Database.User
import com.example.taskmanager.Database.UserDao
import com.example.taskmanager.Database.UserDatabase
import com.example.taskmanager.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class AddTaskActivity : AppCompatActivity() {


    companion object{
        const val editKey = "edit"
    }

    lateinit var binding: ActivityAddTaskBinding
    private lateinit var dao: UserDao
    private var currentUser: User? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "task_table"
        ).allowMainThreadQueries().build()
        dao = db.getUserDao()

        if (intent.hasExtra(editKey)) {
            currentUser = intent.getParcelableExtra(editKey)
            binding.apply {
                inputTask.setText(currentUser?.title)
                inputNote.setText(currentUser?.note)
                inputStatus.setText(currentUser?.status)
                inputDate.setText(currentUser?.date?.let { dateFormat.format(it) }) // Display date
            }
        }

        // Set up DatePickerDialog for inputDate
        binding.inputDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val selectedDate = calendar.time
                    binding.inputDate.setText(dateFormat.format(selectedDate)) // Display selected date
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
        binding.saveBtn.setOnClickListener{
            val title = binding.inputTask.text.toString()
            val note = binding.inputNote.text.toString()
            val stat = binding.inputStatus.text.toString()
            val date = binding.inputDate.text.toString()

            if (title.isNotBlank()){
                val date = dateFormat.parse(date)
                if (this.currentUser != null) {
                updateUser(this.currentUser!!.id, title, note, date,stat)
            } else {
                addUser(title, note, date,stat)
            }
            }
            else{
                finish()
            }
        }
    }
    private fun addUser(title: String, note: String, date: Date,status:String) {
        val user = User(0,title,note,date,status)
        dao.addUser(user)
        val intent = Intent(this@AddTaskActivity,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
    private fun updateUser(id:Int, title: String, note: String, date: Date,status: String) {
        val updatedUser =  User(id,title,note,date,status)
         dao.editUser(updatedUser)
        finish()
    }
    }


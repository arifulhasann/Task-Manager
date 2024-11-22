package com.example.taskmanager
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.taskmanager.Database.User
import com.example.taskmanager.Database.UserDao
import com.example.taskmanager.Database.UserDatabase
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.recyclerview.TaskAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),TaskAdapter.HandleUserClick{
    lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    lateinit var dao:UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "task_table"
        ).allowMainThreadQueries().build()
        dao = db.getUserDao()


        loadDatabase()

        binding.addBtn.setOnClickListener{
            val intent = Intent(this@MainActivity,AddTaskActivity::class.java)
            startActivity(intent)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearch(query)
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    performSearch(newText)
                }
                return true
            }
        })

    }
    private fun loadDatabase() {
        dao.getAllUsers().apply {
            val taskAdapter = TaskAdapter(this@MainActivity, this)
            binding.rv.adapter = taskAdapter
        }
    }

        override fun onResume(){
            super.onResume()
            loadDatabase()
        }
    private fun performSearch(query: String) {
        lifecycleScope.launch {
            val filteredUsers = dao.searchDatabase("%$query%")
            taskAdapter = TaskAdapter(this@MainActivity, filteredUsers)
            binding.rv.adapter = taskAdapter
        }
    }

   override fun onEditClick(user: User) {
        val editIntent = Intent(this@MainActivity,AddTaskActivity::class.java)
        editIntent.putExtra(AddTaskActivity.editKey,user)
        startActivity(editIntent)
    }

    override fun onDeleteClick(user: User) {
        dao.deleteUser(user)
        Toast.makeText(this@MainActivity,"Delete Done",Toast.LENGTH_SHORT).show()
        loadDatabase()

    }
}
package com.example.taskmanager.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Database.User
import com.example.taskmanager.databinding.TaskViewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private val listener: HandleUserClick,
    private val userList: List<User>
) : RecyclerView.Adapter<TaskHolder>() {

    interface HandleUserClick {
        fun onEditClick(user: User)
        fun onDeleteClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = TaskViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val user = userList[position]
        holder.itembinding.apply {
            tvTask.text = user.title
            tvNote.text = user.note
//            tvDate.text = user.date
            tvStatus.text = user.status
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            tvDate.text = dateFormat.format(user.date)

            editImgBtn.setOnClickListener {
                listener.onEditClick(user)
            }
            deleteImgBtn.setOnClickListener {
                listener.onDeleteClick(user)
            }
        }
    }
}

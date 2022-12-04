package com.korneliuszbarwinski.todolist.presentation.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.korneliuszbarwinski.todolist.R
import com.korneliuszbarwinski.todolist.databinding.ListItemToDoBinding
import com.korneliuszbarwinski.todolist.domain.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.*

class ToDoListAdapter :
    ListAdapter<ToDoItem, ToDoListAdapter.UserListHolder>(ToDoItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val binding =
            ListItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListHolder(binding)
    }

    inner class UserListHolder(private val binding: ListItemToDoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(toDoItem: ToDoItem) {
            binding.apply {
                createdAtTV.text =
                    SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(toDoItem.createdAt!!))
                titleTV.text = toDoItem.title
                descriptionTV.text = toDoItem.description

                Glide.with(this.root)
                    .load(toDoItem.iconUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_error)
                    .into(photoIV)

                root.setOnClickListener {
                    onItemClickListener?.let { it(toDoItem) }
                }

                root.setOnLongClickListener {
                    onLongItemClickListener?.let { it(toDoItem) }
                    true
                }
            }
        }
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val user = getItem(position)
        holder.bindData(user)
    }

    private var onItemClickListener: ((ToDoItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ToDoItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onLongItemClickListener: ((ToDoItem) -> Unit)? = null

    fun setOnLongItemClickListener(listener: (ToDoItem) -> Unit) {
        onLongItemClickListener = listener
    }

}

class ToDoItemDiffCallback : DiffUtil.ItemCallback<ToDoItem>() {
    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
        return oldItem == newItem
    }
}
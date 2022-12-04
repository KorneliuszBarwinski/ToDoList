package com.korneliuszbarwinski.todolist.domain.repository

import com.korneliuszbarwinski.todolist.domain.model.Response
import com.korneliuszbarwinski.todolist.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getToDoItemsFromFirestore(): Flow<Response<List<ToDoItem>>>

    suspend fun addToDoItemToFirestore(title: String, description: String, iconUrl: String?, id: String?): Response<Boolean>

    suspend fun deleteToDoItemFromFirestore(id: String): Response<Boolean>
}
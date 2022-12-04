package com.korneliuszbarwinski.todolist.domain.model

import java.io.Serializable

data class ToDoItem(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val createdAt: Long? = null,
    val iconUrl: String? = null
): Serializable

package com.korneliuszbarwinski.todolist.domain.usecase

import com.korneliuszbarwinski.todolist.domain.repository.ToDoRepository

class AddToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(title: String, description: String, iconUrl: String?, id: String?) = toDoRepository.addToDoItemToFirestore(title, description, iconUrl, id)
}
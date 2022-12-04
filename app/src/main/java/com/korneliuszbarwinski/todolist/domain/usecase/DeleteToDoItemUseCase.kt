package com.korneliuszbarwinski.todolist.domain.usecase

import com.korneliuszbarwinski.todolist.domain.repository.ToDoRepository

class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(id: String) = toDoRepository.deleteToDoItemFromFirestore(id)
}
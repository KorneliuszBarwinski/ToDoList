package com.korneliuszbarwinski.todolist.domain.usecase

import com.korneliuszbarwinski.todolist.domain.repository.ToDoRepository

class GetToDoItemsUseCase(
    private val toDoRepository: ToDoRepository
) {
    operator fun invoke() = toDoRepository.getToDoItemsFromFirestore()
}
package com.korneliuszbarwinski.todolist.data.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.korneliuszbarwinski.todolist.data.repository.ToDoRepositoryImpl
import com.korneliuszbarwinski.todolist.domain.repository.ToDoRepository
import com.korneliuszbarwinski.todolist.domain.usecase.AddToDoItemUseCase
import com.korneliuszbarwinski.todolist.domain.usecase.DeleteToDoItemUseCase
import com.korneliuszbarwinski.todolist.domain.usecase.GetToDoItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ToDoModule {
    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideToDoRef(
        db: FirebaseFirestore
    ) = db.collection("todoitems")

    @Provides
    fun provideToDoRepository(
        toDoRef: CollectionReference
    ): ToDoRepository = ToDoRepositoryImpl(toDoRef)

    @Provides
    fun provideGetToDoItemsUseCase(
        repo: ToDoRepository
    ) = GetToDoItemsUseCase(repo)

    @Provides
    fun provideAddToDoItemUseCase(
        repo: ToDoRepository
    ) = AddToDoItemUseCase(repo)

    @Provides
    fun provideDeleteToDoItemUseCase(
        repo: ToDoRepository
    ) = DeleteToDoItemUseCase(repo)
}
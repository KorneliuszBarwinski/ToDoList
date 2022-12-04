package com.korneliuszbarwinski.todolist.data.repository

import com.google.firebase.firestore.CollectionReference
import com.korneliuszbarwinski.todolist.domain.model.Response
import com.korneliuszbarwinski.todolist.domain.model.ToDoItem
import com.korneliuszbarwinski.todolist.domain.repository.ToDoRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToDoRepositoryImpl @Inject constructor(
    private val toDoRef: CollectionReference
) : ToDoRepository {
    override fun getToDoItemsFromFirestore() = callbackFlow {
        val snapshotListener = toDoRef.orderBy("createdAt").addSnapshotListener { snapshot, e ->
            val toDoListResponse = if (snapshot != null) {
                val toDoList = snapshot.toObjects(ToDoItem::class.java)
                Response.Success(toDoList)
            } else {
                Response.Failure(e)
            }
            trySend(toDoListResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addToDoItemToFirestore(
        title: String,
        description: String,
        iconUrl: String?,
        id: String?
    ): Response<Boolean> {
        return try {
            val idToSave = id ?: toDoRef.document().id
            val toDoItem = ToDoItem(
                id = idToSave,
                title = title,
                description = description,
                iconUrl = iconUrl,
                createdAt = Date().time
            )
            toDoRef.document(idToSave).set(toDoItem).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deleteToDoItemFromFirestore(id: String): Response<Boolean> {
        return try {
            toDoRef.document(id).delete().await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
package com.korneliuszbarwinski.todolist.presentation.todolist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korneliuszbarwinski.todolist.domain.model.Response
import com.korneliuszbarwinski.todolist.domain.model.ToDoItem
import com.korneliuszbarwinski.todolist.domain.usecase.DeleteToDoItemUseCase
import com.korneliuszbarwinski.todolist.domain.usecase.GetToDoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel
@Inject constructor(
    private val getToDoItemsUseCase: GetToDoItemsUseCase,
    private val deleteToDoItemUseCase: DeleteToDoItemUseCase
) : ViewModel() {

    private val _toDoItems = MutableLiveData<Response<List<ToDoItem>>>()
    val toDoItems: LiveData<Response<List<ToDoItem>>>
        get() = _toDoItems

    init {
        getToDoItems()
    }

    private fun getToDoItems() = viewModelScope.launch {
        _toDoItems.postValue(Response.Loading)
        getToDoItemsUseCase.invoke().collect { response ->
            _toDoItems.postValue(response)
        }
    }

    fun deleteToDoItem(id: String) = viewModelScope.launch {
        deleteToDoItemUseCase.invoke(id)
    }
}
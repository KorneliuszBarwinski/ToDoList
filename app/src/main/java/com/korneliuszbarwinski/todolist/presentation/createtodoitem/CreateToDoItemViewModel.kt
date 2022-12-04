package com.korneliuszbarwinski.todolist.presentation.createtodoitem


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korneliuszbarwinski.todolist.domain.model.Response
import com.korneliuszbarwinski.todolist.domain.usecase.AddToDoItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateToDoItemViewModel
@Inject constructor(
    private val addToDoItemUseCase: AddToDoItemUseCase
) : ViewModel() {

    private val _addToDoItemResponse = MutableLiveData<Response<Boolean>>()
    val addToDoItemResponse: LiveData<Response<Boolean>>
        get() = _addToDoItemResponse


    fun addToDoItem(title: String, description: String, url: String = "", id: String? = null) =
        viewModelScope.launch {
            _addToDoItemResponse.postValue(Response.Loading)
            _addToDoItemResponse.postValue(addToDoItemUseCase.invoke(title, description, url, id))
        }
}
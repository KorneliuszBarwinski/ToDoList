package com.korneliuszbarwinski.todolist.presentation.todolist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.korneliuszbarwinski.todolist.R
import com.korneliuszbarwinski.todolist.common.gone
import com.korneliuszbarwinski.todolist.common.showToast
import com.korneliuszbarwinski.todolist.common.visible
import com.korneliuszbarwinski.todolist.databinding.FragmentToDoListBinding
import com.korneliuszbarwinski.todolist.domain.model.Response
import com.korneliuszbarwinski.todolist.domain.model.ToDoItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDoListFragment : Fragment(R.layout.fragment_to_do_list) {

    private val viewModel: ToDoListViewModel by viewModels()
    private val toDoListAdapter = ToDoListAdapter()

    private var _binding: FragmentToDoListBinding? = null
    private val binding: FragmentToDoListBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToDoListBinding.bind(view)



        toDoListAdapter.apply {
            setOnLongItemClickListener { toDoItem ->
                showDeleteDialog(toDoItem)
            }
            setOnItemClickListener {
                findNavController().navigate(
                    ToDoListFragmentDirections.actionToDoListFragmentToCreateToDoItemFragment(
                        it
                    )
                )
            }
        }
        binding.apply {
            toDoListRV.adapter = toDoListAdapter
            addToDoItemFAB.setOnClickListener {
                findNavController().navigate(
                    ToDoListFragmentDirections.actionToDoListFragmentToCreateToDoItemFragment(
                        ToDoItem()
                    )
                )
            }
        }

        viewModel.apply {
            toDoItems.observe(viewLifecycleOwner) {
                when (it) {
                    is Response.Loading -> showLoader()
                    is Response.Success -> {
                        toDoListAdapter.submitList(it.data)
                        hideLoader()
                    }
                    is Response.Failure -> {
                        hideLoader()
                        requireContext().showToast(it.e.toString())
                    }
                }
            }
        }
    }

    private fun showDeleteDialog(toDoItem: ToDoItem) {
        val dialog: AlertDialog = AlertDialog.Builder(requireContext()).apply {
            setMessage("Czy na pewno chcesz usunąć ${toDoItem.title}?")
            setPositiveButton("Tak") { _, _ ->
                toDoItem.id?.let { viewModel.deleteToDoItem(it) }
            }
            setNegativeButton("Nie") { _, _ ->
            }
        }.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoader() {
        binding.toDoListPB.visible()
    }

    private fun hideLoader() {
        binding.toDoListPB.gone()
    }
}
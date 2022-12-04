package com.korneliuszbarwinski.todolist.presentation.createtodoitem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.korneliuszbarwinski.todolist.R
import com.korneliuszbarwinski.todolist.common.gone
import com.korneliuszbarwinski.todolist.common.showToast
import com.korneliuszbarwinski.todolist.common.visible
import com.korneliuszbarwinski.todolist.databinding.FragmentCreateToDoItemBinding
import com.korneliuszbarwinski.todolist.domain.model.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateToDoItemFragment : Fragment(R.layout.fragment_create_to_do_item) {

    private val viewModel: CreateToDoItemViewModel by viewModels()
    private val args: CreateToDoItemFragmentArgs by navArgs()

    private var _binding: FragmentCreateToDoItemBinding? = null
    private val binding: FragmentCreateToDoItemBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateToDoItemBinding.bind(view)

        viewModel.addToDoItemResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> showLoader()
                is Response.Success -> {
                    hideLoader()
                    findNavController().navigate(CreateToDoItemFragmentDirections.actionCreateToDoItemFragmentToToDoListFragment())
                }
                is Response.Failure -> {
                    hideLoader()
                    requireContext().showToast("Nie udało się utworzyć to-do item!")
                }
            }
        }

        binding.apply {
            descriptionET.setText(args.todoitem.description)
            titleET.setText(args.todoitem.title)
            photoUrlET.setText(args.todoitem.iconUrl)
            photoIV.loadImage(args.todoitem.iconUrl, this.root)

            photoUrlET.doAfterTextChanged {
                photoIV.loadImage(it.toString(), this.root)
            }

            saveBTN.setOnClickListener {
                viewModel.addToDoItem(
                    titleET.text.toString(),
                    descriptionET.text.toString(),
                    photoUrlET.text.toString(),
                    args.todoitem.id
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun ImageView.loadImage(url: String?, view: View) {
        Glide.with(view)
            .load(url)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_image_error)
            .into(this)
    }

    private fun showLoader() {
        binding.createItemPB.visible()
    }

    private fun hideLoader() {
        binding.createItemPB.gone()
    }
}
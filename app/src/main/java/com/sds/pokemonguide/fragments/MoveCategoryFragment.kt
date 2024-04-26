package com.sds.pokemonguide.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.adapter.MoveAilmentAdapter
import com.sds.pokemonguide.adapter.MoveCategoryAdapter
import com.sds.pokemonguide.databinding.MoveAilmentFragmentBinding
import com.sds.pokemonguide.databinding.MoveCategoryFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoveCategoryFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: MoveCategoryFragmentArgs by navArgs()

    private var moveCategoryAdapter: MoveCategoryAdapter? = null

    private var _binding: MoveCategoryFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusMoveCategory.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoveCategoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadData()
        setDataViews()
        setUI()
        setAdapter()
        setButtons()
    }

    private fun setDataViews() {

        binding.errorContainer.setOnClickListener {
            downloadData()
        }

        viewModel.loadingStatusMoveCategory.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idMoveCategoryName.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    binding.moveCategoryItemCategory.visibility = View.GONE
                    binding.idMoveCategoryDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveCategoryName.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.moveCategoryItemCategory.visibility = View.VISIBLE
                    binding.idMoveCategoryDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idMoveCategoryName.visibility = View.GONE
                    binding.moveCategoryItemCategory.visibility = View.GONE
                    binding.idMoveCategoryDetailContainer.visibility = View.GONE
                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.moveCategoryId.removePrefix("https://pokeapi.co/api/v2/move-category/").dropLast(1)
            viewModel.downloadMoveCategory(id)
        }
    }

    private fun setUI() {
        viewModel.moveCategoryDetailResponse.observe(viewLifecycleOwner) {
            binding.idMoveCategoryName.text = it.name.replace("-", " ")
            binding.idDescriptionValue.text = it.descriptions[0].description

            moveCategoryAdapter?.addMoves(it.moves)
        }
    }

    private fun setAdapter() {
        moveCategoryAdapter = MoveCategoryAdapter()

        binding.idRvMoveCategoryMoves.adapter = moveCategoryAdapter
        binding.idRvMoveCategoryMoves.layoutManager =
            GridLayoutManager(this.context, 2)


        moveCategoryAdapter!!.setOnItemClickListenerUrlMove {
            findNavController().navigate(
                MoveCategoryFragmentDirections.actionMoveCategoryFragmentToMovePokedexDetailFragment(
                    it, false
                )
            )
        }
    }

    private fun setButtons(){

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        moveCategoryAdapter = null

        _binding = null
    }
}
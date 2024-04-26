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
import com.sds.pokemonguide.adapter.MoveCategoryAdapter
import com.sds.pokemonguide.databinding.MoveCategoryFragmentBinding
import com.sds.pokemonguide.databinding.MoveDamageClassFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoveDamageClassFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: MoveDamageClassFragmentArgs by navArgs()

    private var moveDamageClassAdapter: MoveCategoryAdapter? = null

    private var _binding: MoveDamageClassFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusMoveDamageClass.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoveDamageClassFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusMoveDamageClass.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.idMoveDamageClassName.visibility = View.GONE
                    binding.moveDamageClassItemCategory.visibility = View.GONE
                    binding.idMoveDamageClassDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    binding.idMoveDamageClassName.visibility = View.VISIBLE
                    binding.moveDamageClassItemCategory.visibility = View.VISIBLE
                    binding.idMoveDamageClassDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idMoveDamageClassName.visibility = View.GONE
                    binding.moveDamageClassItemCategory.visibility = View.GONE
                    binding.idMoveDamageClassDetailContainer.visibility = View.GONE
                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.moveDamageClassId.removePrefix("https://pokeapi.co/api/v2/move-damage-class/").dropLast(1)
            viewModel.downloadMoveDamageClass(id)
        }
    }

    private fun setUI() {
        viewModel.moveDamageClassDetailResponse.observe(viewLifecycleOwner) {
            binding.idMoveDamageClassName.text = it.name

            for (value in it.descriptions) {
                if (value.language.name == "en") binding.idDescriptionValue.text = value.description
            }

            moveDamageClassAdapter?.addMoves(it.moves)
        }
    }

    private fun setAdapter() {
        moveDamageClassAdapter = MoveCategoryAdapter()

        binding.idRvMoveDamageClassMoves.adapter = moveDamageClassAdapter
        binding.idRvMoveDamageClassMoves.layoutManager =
            GridLayoutManager(this.context, 2)


        moveDamageClassAdapter!!.setOnItemClickListenerUrlMove {
            findNavController().navigate(
                MoveDamageClassFragmentDirections.actionMoveDamageClassFragmentToMovePokedexDetailFragment(
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

        moveDamageClassAdapter = null

        _binding = null
    }
}
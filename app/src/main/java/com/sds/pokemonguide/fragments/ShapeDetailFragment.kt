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
import com.sds.pokemonguide.adapter.HabitatDetailAdapter
import com.sds.pokemonguide.adapter.ShapeDetailAdapter
import com.sds.pokemonguide.databinding.HabitatDetailFragmentBinding
import com.sds.pokemonguide.databinding.ShapeDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShapeDetailFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: ShapeDetailFragmentArgs by navArgs()

    private var shapeAdapter: ShapeDetailAdapter? = null

    private var _binding: ShapeDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusShape.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShapeDetailFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusShape.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idShapeName.visibility = View.GONE
                    binding.shapeItemCategory.visibility = View.GONE
                    binding.idShapeDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idShapeName.visibility = View.VISIBLE
                    binding.shapeItemCategory.visibility = View.VISIBLE
                    binding.idShapeDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idShapeName.visibility = View.GONE
                    binding.shapeItemCategory.visibility = View.GONE
                    binding.idShapeDetailContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.shapeId.removePrefix("https://pokeapi.co/api/v2/pokemon-shape/").dropLast(1)
            viewModel.downloadShape(id)
        }
    }

    private fun setUI() {
        viewModel.shapeDetailResponse.observe(viewLifecycleOwner) {
            binding.idShapeName.text = it.name.replace("-", " ")

            shapeAdapter?.addPokemons(it.pokemonSpecies)
        }
    }

    private fun setAdapter() {
        shapeAdapter = ShapeDetailAdapter()

        binding.idRvShapePokemons.adapter = shapeAdapter
        binding.idRvShapePokemons.layoutManager =
            GridLayoutManager(this.context, 2)


        shapeAdapter!!.setOnItemClickListenerUrlShape {
            findNavController().navigate(
                ShapeDetailFragmentDirections.actionShapeFragmentToPokedexDetailFragment(
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

        shapeAdapter = null

        _binding = null
    }
}
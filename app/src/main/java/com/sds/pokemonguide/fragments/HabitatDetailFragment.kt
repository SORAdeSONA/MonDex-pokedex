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
import com.sds.pokemonguide.adapter.EggGroupDetailAdapter
import com.sds.pokemonguide.adapter.HabitatDetailAdapter
import com.sds.pokemonguide.databinding.EggGroupFragmentBinding
import com.sds.pokemonguide.databinding.HabitatDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HabitatDetailFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: HabitatDetailFragmentArgs by navArgs()

    private var habitatAdapter: HabitatDetailAdapter? = null

    private var _binding: HabitatDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusHabitat.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HabitatDetailFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusHabitat.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idHabitatName.visibility = View.GONE
                    binding.habitatItemCategory.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    binding.idHabitatDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idHabitatName.visibility = View.VISIBLE
                    binding.habitatItemCategory.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.idHabitatDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idHabitatName.visibility = View.GONE
                    binding.habitatItemCategory.visibility = View.GONE
                    binding.idHabitatDetailContainer.visibility = View.GONE
                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.habitatId.removePrefix("https://pokeapi.co/api/v2/pokemon-habitat/").dropLast(1)
            viewModel.downloadHabitat(id)
        }
    }

    private fun setUI() {
        viewModel.habitatDetailResponse.observe(viewLifecycleOwner) {
            binding.idHabitatName.text = it.name.replace("-", " ")

            habitatAdapter?.addPokemons(it.pokemonSpecies)
        }
    }

    private fun setAdapter() {
        habitatAdapter = HabitatDetailAdapter()

        binding.idRvHabitatPokemons.adapter = habitatAdapter
        binding.idRvHabitatPokemons.layoutManager =
            GridLayoutManager(this.context, 2)


        habitatAdapter!!.setOnItemClickListenerUrlEggGroup {
            findNavController().navigate(
                HabitatDetailFragmentDirections.actionHabitatFragmentToPokedexDetailFragment(
                    it, false
                )
            )
        }
    }

    private fun setButtons() {

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        habitatAdapter = null

        _binding = null
    }

}
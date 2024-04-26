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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.adapter.AbilityUsedByPokemonsAdapter
import com.sds.pokemonguide.adapter.BerryFirmnessAdapter
import com.sds.pokemonguide.databinding.BerryFirmnessFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BerryFirmnessFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: BerryFirmnessFragmentArgs by navArgs()

    private var berryFirmnessAdapter: BerryFirmnessAdapter? = null

    private var _binding: BerryFirmnessFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusBerryFirmness.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BerryFirmnessFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusBerryFirmness.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.idBerryFirmnessName.visibility = View.GONE
                    binding.berryFirmnessItemCategory.visibility = View.GONE
                    binding.berryFirmnessDetailContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE

                }

                LoadingStatus.SUCCESS -> {
                    binding.idBerryFirmnessName.visibility = View.VISIBLE
                    binding.berryFirmnessItemCategory.visibility = View.VISIBLE
                    binding.berryFirmnessDetailContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE


                }

                LoadingStatus.ERROR -> {
                    binding.idBerryFirmnessName.visibility = View.GONE
                    binding.berryFirmnessItemCategory.visibility = View.GONE
                    binding.berryFirmnessDetailContainer.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE

                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            viewModel.downloadBerryFirmnessDetail(args.url)
        }
    }

    private fun setUI() {
        viewModel.berryFirmnessDetailResponse.observe(viewLifecycleOwner) {
            binding.idBerryFirmnessName.text = it.name.replace("-", " ")
            berryFirmnessAdapter?.addBerries(it.berriesList)
        }
    }

    private fun setAdapter() {
        berryFirmnessAdapter = BerryFirmnessAdapter()

        binding.idRvBerryFirmnessItems.adapter = berryFirmnessAdapter
        binding.idRvBerryFirmnessItems.layoutManager =
            GridLayoutManager(this.context, 2)



        berryFirmnessAdapter!!.setOnItemClickListenerUrlAbility {
            findNavController().navigate(
                BerryFirmnessFragmentDirections.actionBerryFirmnessPokedexFragmentToBerryDetailPokedexFragment(
                    it
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

        berryFirmnessAdapter = null

        _binding = null
    }
}
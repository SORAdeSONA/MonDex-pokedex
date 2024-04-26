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
import com.sds.pokemonguide.adapter.BerryFlavorAdapter
import com.sds.pokemonguide.databinding.BerryFlavorFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BerryFlavorFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: BerryFlavorFragmentArgs by navArgs()

    private var berryFlavorAdapter: BerryFlavorAdapter? = null

    private var _binding: BerryFlavorFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusBerryFlavor.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BerryFlavorFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusBerryFlavor.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idBerryFlavorName.visibility = View.GONE
                    binding.berryFlavorItemCategory.visibility = View.GONE
                    binding.berryFlavorDetailContainer.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idBerryFlavorName.visibility = View.VISIBLE
                    binding.berryFlavorItemCategory.visibility = View.VISIBLE
                    binding.berryFlavorDetailContainer.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idBerryFlavorName.visibility = View.GONE
                    binding.berryFlavorItemCategory.visibility = View.GONE
                    binding.berryFlavorDetailContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            viewModel.downloadBerryFlavorDetail(args.url)
        }
    }

    private fun setUI() {
        viewModel.berryFlavorDetailResponse.observe(viewLifecycleOwner) {
            binding.idBerryFlavorName.text = it.name
            berryFlavorAdapter?.addBerries(it.berriesList)
        }
    }

    private fun setAdapter() {
        berryFlavorAdapter = BerryFlavorAdapter()

        binding.idRvBerryFlavorItems.adapter = berryFlavorAdapter
        binding.idRvBerryFlavorItems.layoutManager =
            GridLayoutManager(this.context, 2)



        berryFlavorAdapter!!.setOnItemClickListenerUrlFlavor {
            findNavController().navigate(
                BerryFlavorFragmentDirections.actionBerryFlavorPokedexFragmentToBerryDetailPokedexFragment(
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

        berryFlavorAdapter = null

        _binding = null
    }
}
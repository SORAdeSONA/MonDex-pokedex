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
import com.sds.pokemonguide.databinding.MoveTargetFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoveTargetFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: MoveTargetFragmentArgs by navArgs()

    private var moveTargetAdapter: MoveCategoryAdapter? = null

    private var _binding: MoveTargetFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusMoveTarget.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoveTargetFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusMoveTarget.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idMoveTargetName.visibility = View.GONE
                    binding.moveTargetItemCategory.visibility = View.GONE
                    binding.idMoveTargetDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveTargetName.visibility = View.VISIBLE
                    binding.moveTargetItemCategory.visibility = View.VISIBLE
                    binding.idMoveTargetDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idMoveTargetName.visibility = View.GONE
                    binding.moveTargetItemCategory.visibility = View.GONE
                    binding.idMoveTargetDetailContainer.visibility = View.GONE
                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.moveTargetId.removePrefix("https://pokeapi.co/api/v2/move-target/").dropLast(1)
            viewModel.downloadMoveTarget(id)
        }
    }

    private fun setUI() {
        viewModel.moveTargetDetailResponse.observe(viewLifecycleOwner) {
            binding.idMoveTargetName.text = it.name.replace("-", " ")

            for (value in it.descriptions) {
                if (value.language.name == "en") binding.idDescriptionValue.text = value.description
            }

            moveTargetAdapter?.addMoves(it.moves)
        }
    }

    private fun setAdapter() {
        moveTargetAdapter = MoveCategoryAdapter()

        binding.idRvMoveTargetMoves.adapter = moveTargetAdapter
        binding.idRvMoveTargetMoves.layoutManager =
            GridLayoutManager(this.context, 2)


        moveTargetAdapter!!.setOnItemClickListenerUrlMove {
            findNavController().navigate(
                MoveTargetFragmentDirections.actionTargetFragmentToMovePokedexDetailFragment(
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

        moveTargetAdapter = null

        _binding = null
    }
}
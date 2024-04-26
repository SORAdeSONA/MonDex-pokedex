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
import com.sds.pokemonguide.adapter.ShapeDetailAdapter
import com.sds.pokemonguide.databinding.MoveAilmentFragmentBinding
import com.sds.pokemonguide.databinding.ShapeDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoveAilmentFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: MoveAilmentFragmentArgs by navArgs()

    private var moveAilmentAdapter: MoveAilmentAdapter? = null

    private var _binding: MoveAilmentFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusMoveAilment.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoveAilmentFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusMoveAilment.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idMoveAilmentName.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    binding.moveAilmentItemCategory.visibility = View.GONE
                    binding.idMoveAilmentDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    binding.idMoveAilmentName.visibility = View.VISIBLE
                    binding.moveAilmentItemCategory.visibility = View.VISIBLE
                    binding.idMoveAilmentDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idMoveAilmentName.visibility = View.GONE
                    binding.moveAilmentItemCategory.visibility = View.GONE
                    binding.idMoveAilmentDetailContainer.visibility = View.GONE
                }
            }
        }



    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.moveAilmentId.removePrefix("https://pokeapi.co/api/v2/move-ailment/").dropLast(1)
            viewModel.downloadMoveAilment(id)
        }
    }

    private fun setUI() {
        viewModel.moveAilmentDetailResponse.observe(viewLifecycleOwner) {
            binding.idMoveAilmentName.text = it.name.replace("-", " ")

            moveAilmentAdapter?.addMoves(it.moves)
        }
    }

    private fun setAdapter() {
        moveAilmentAdapter = MoveAilmentAdapter()

        binding.idRvMoveAilmentMoves.adapter = moveAilmentAdapter
        binding.idRvMoveAilmentMoves.layoutManager =
            GridLayoutManager(this.context, 2)


        moveAilmentAdapter!!.setOnItemClickListenerUrlMove {
            findNavController().navigate(
                MoveAilmentFragmentDirections.actionMoveAilmentFragmentToMovePokedexDetailFragment(
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

        moveAilmentAdapter = null

        _binding = null
    }
}
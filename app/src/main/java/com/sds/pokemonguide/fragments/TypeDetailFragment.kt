package com.sds.pokemonguide.fragments

import android.annotation.SuppressLint
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
import com.sds.pokemonguide.adapter.CommonDamageRelationsAdapter
import com.sds.pokemonguide.adapter.TypePokemonsAdapter
import com.sds.pokemonguide.databinding.TypeDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TypeDetailFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: TypeDetailFragmentArgs by navArgs()

    private var doubleDamageFromAdapter: CommonDamageRelationsAdapter? = null
    private var doubleDamageToAdapter: CommonDamageRelationsAdapter? = null
    private var halfDamageFromAdapter: CommonDamageRelationsAdapter? = null
    private var halfDamageToAdapter: CommonDamageRelationsAdapter? = null
    private var noDamageFromAdapter: CommonDamageRelationsAdapter? = null
    private var noDamageToAdapter: CommonDamageRelationsAdapter? = null

    private var pokemonsAdapter: TypePokemonsAdapter? = null

    var typeDownloadBoolean = false


    private var _binding: TypeDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusPokemonType.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TypeDetailFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusPokemonType.observe(viewLifecycleOwner) {
            when (it) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idTypeDetailContainer.visibility = View.GONE
                    binding.typeItemCategory.visibility = View.GONE
                    binding.idTypeName.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.typeItemCategory.visibility = View.VISIBLE
                    binding.idTypeName.visibility = View.VISIBLE
                    binding.idTypeDetailContainer.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idTypeDetailContainer.visibility = View.GONE
                    binding.typeItemCategory.visibility = View.GONE
                    binding.idTypeName.visibility = View.GONE
                }


            }
        }
    }

    private fun downloadData() {
        if (typeDownloadBoolean == false){
            val id = args.typeId.removePrefix("https://pokeapi.co/api/v2/type/").dropLast(1)
            viewModel.viewModelScope.launch(coroutineExceptionHandler) {
                viewModel.downloadPokemonTypeDetail(id)
            }
            typeDownloadBoolean = true
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUI() {
        viewModel.pokemonTypeDetailResponse.observe(viewLifecycleOwner) {

            binding.idTypeName.text = it.name
            binding.idGenerationValue.text = it.generation.generationName.replace("-", " ")

            if (it.damageRelations.doubleDamageFrom.isEmpty()) {
                binding.idTypeDoubleDamageFrom.text = "x2 damage from:\n\n0 types"
            } else doubleDamageFromAdapter?.addTypes(it.damageRelations.doubleDamageFrom)

            if (it.damageRelations.doubleDamageTo.isEmpty()) {
                binding.idTypeDoubleDamageTo.text = "x2 damage to:\n\n0 types"
            } else doubleDamageToAdapter?.addTypes(it.damageRelations.doubleDamageTo)

            if (it.damageRelations.halfDamageFrom.isEmpty()) {
                binding.idHalfDamageFrom.text = "half damage from:\n\n0 types"
            } else halfDamageFromAdapter?.addTypes(it.damageRelations.halfDamageFrom)

            if (it.damageRelations.halfDamageTo.isEmpty()) {
                binding.idHalfDamageTo.text = "half damage to:\n\n0 types"
            } else halfDamageToAdapter?.addTypes(it.damageRelations.halfDamageTo)

            if (it.damageRelations.noDamageTo.isEmpty()) {
                binding.idNoDamageTo.text = "no dmg to:\n\n0 types"
            } else noDamageToAdapter?.addTypes(it.damageRelations.noDamageTo)

            if (it.damageRelations.noDamageFrom.isEmpty()) {
                binding.idNoDamageFrom.text = "no damage from:\n\n0 types"
            } else noDamageFromAdapter?.addTypes(it.damageRelations.noDamageFrom)

            if (it.pokemon.isNotEmpty()) {
                it.pokemon.forEach {
                    pokemonsAdapter?.addPokemons(it.pokemon)
                }
            } else binding.idPokemons.text = "no pokemon found"

        }

    }

    private fun setAdapter() {
        doubleDamageFromAdapter = CommonDamageRelationsAdapter()

        binding.idDoubleDamageFromRv.adapter = doubleDamageFromAdapter
        binding.idDoubleDamageFromRv.layoutManager =
            GridLayoutManager(this.context, 3)

        doubleDamageToAdapter = CommonDamageRelationsAdapter()

        binding.idDoubleDamageToRv.adapter = doubleDamageToAdapter
        binding.idDoubleDamageToRv.layoutManager =
            GridLayoutManager(this.context, 3)

        halfDamageFromAdapter = CommonDamageRelationsAdapter()

        binding.idHalfDamageFromRv.adapter = halfDamageFromAdapter
        binding.idHalfDamageFromRv.layoutManager =
            GridLayoutManager(this.context, 3)

        halfDamageToAdapter = CommonDamageRelationsAdapter()

        binding.idHalfDamageToRv.adapter = halfDamageToAdapter
        binding.idHalfDamageToRv.layoutManager =
            GridLayoutManager(this.context, 3)

        noDamageFromAdapter = CommonDamageRelationsAdapter()

        binding.idNoDamageFromRv.adapter = noDamageFromAdapter
        binding.idNoDamageFromRv.layoutManager =
            GridLayoutManager(this.context, 3)

        noDamageToAdapter = CommonDamageRelationsAdapter()

        binding.idNoDamageToRv.adapter = noDamageToAdapter
        binding.idNoDamageToRv.layoutManager =
            GridLayoutManager(this.context, 3)

        pokemonsAdapter = TypePokemonsAdapter()

        binding.idPokemonsRv.adapter = pokemonsAdapter
        binding.idPokemonsRv.layoutManager =
            GridLayoutManager(this.context, 2)

        pokemonsAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToPokemonDetailFragment(
                    it, false
                )
            )
        }


        doubleDamageFromAdapter!!.setOnItemClickListenerUrlCommonType {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToTypeFragment(
                    it
                )
            )
        }

        doubleDamageToAdapter!!.setOnItemClickListenerUrlCommonType {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToTypeFragment(
                    it
                )
            )
        }

        halfDamageFromAdapter!!.setOnItemClickListenerUrlCommonType {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToTypeFragment(
                    it
                )
            )
        }

        halfDamageToAdapter!!.setOnItemClickListenerUrlCommonType {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToTypeFragment(
                    it
                )
            )
        }

        noDamageFromAdapter!!.setOnItemClickListenerUrlCommonType {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToTypeFragment(
                    it
                )
            )
        }

        noDamageToAdapter!!.setOnItemClickListenerUrlCommonType {
            findNavController().navigate(
                TypeDetailFragmentDirections.actionTypeFragmentToTypeFragment(
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

        doubleDamageFromAdapter = null
        doubleDamageToAdapter = null
        halfDamageFromAdapter = null
        halfDamageToAdapter = null
        noDamageFromAdapter = null
        noDamageToAdapter = null

        pokemonsAdapter = null

        _binding = null
    }
}
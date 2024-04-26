package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.AlsoUsedByAdapter
import com.sds.pokemonguide.databinding.MoveDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MovesDetailFragment : Fragment() {


    private val viewModel: PokemonViewModel by viewModels()

    private var alsoUsedByAdapter: AlsoUsedByAdapter? = null
    private val args: MovesDetailFragmentArgs by navArgs()

    private var _binding: MoveDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusMove.value = LoadingStatus.ERROR
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoveDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadData()
        setDataViews()
        setUI()
        setAdapter()
        setButtons()
        loadBanner()
    }


    private fun setDataViews() {

        binding.errorContainer.setOnClickListener {
            downloadData()
        }

        viewModel.loadingStatusMove.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.moveItemCategory.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idMoveDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.moveItemCategory.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.moveItemCategory.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveDetailContainer.visibility = View.GONE

                }
            }
        }
    }

    private fun downloadData() {
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            if (args.isSearch) {
                viewModel.downloadMoveDetailsById(args.moveId)
            } else {
                val id = args.moveId.removePrefix("https://pokeapi.co/api/v2/move/").dropLast(1)
                viewModel.downloadMoveDetailsById(id)
            }

        }
    }

    private fun setUI() {

        viewModel.moveDetailResponse.observe(viewLifecycleOwner) { response ->


            viewModel.viewModelScope.launch {
                val list = withContext(Dispatchers.IO){
                    viewModel.getAllMovesFromDb()
                }
                if (list.isEmpty()) {
                    binding.savedNo.visibility = View.VISIBLE
                } else {
                    for (value in list) {
                        if (value.moveName == response.moveName) {
                            println("saved yes")
                            binding.savedYes.visibility = View.VISIBLE
                        } else {
                            println("saved no")
                            binding.savedNo.visibility = View.VISIBLE
                        }
                    }
                }

            }

            binding.savedNo.setOnClickListener {
                viewModel.viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        viewModel.addMoveToDb(
                            dbId = 0,
                            moveName = response.moveName,
                            moveUrl = args.moveId
                        )
                    }

                    withContext(Dispatchers.Main) {
                        binding.savedNo.visibility = View.GONE
                        binding.savedYes.visibility = View.VISIBLE
                        alertDialog(response.moveName, "added to")
                    }
                }
            }

            binding.savedYes.setOnClickListener {
               viewModel.viewModelScope.launch {
                   withContext(Dispatchers.IO){
                       viewModel.deleteMoveFromDb(response.moveName)
                   }
                    withContext(Dispatchers.Main) {
                        binding.savedNo.visibility = View.VISIBLE
                        binding.savedYes.visibility = View.GONE
                        alertDialog(response.moveName, "removed from")
                    }
                }
            }




            try {
                binding.idPokemonMoveName.text = response.moveName.replace("-", " ")
            } catch (e: Exception) {
                binding.idPokemonMoveName.text = "-"
            }



            fun getImageByType(type: String): Int {
                when (type) {
                    "grass" -> return R.drawable.icon_grass
                    "fire" -> return R.drawable.type_fire
                    "poison" -> return R.drawable.type_poison
                    "fighting" -> return R.drawable.type_fighting
                    "flying" -> return R.drawable.type_flying
                    "ground" -> return R.drawable.type_ground
                    "rock" -> return R.drawable.type_rock
                    "bug" -> return R.drawable.type_bug
                    "ghost" -> return R.drawable.type_ghost
                    "steel" -> return R.drawable.type_steel
                    "water" -> return R.drawable.type_water
                    "electric" -> return R.drawable.electir_type_
                    "psychic" -> return R.drawable.type_psychic
                    "ice" -> return R.drawable.type_ice
                    "dragon" -> return R.drawable.type_dragon
                    "dark" -> return R.drawable.type_dark
                    "fairy" -> return R.drawable.type_fairy
                    "normal" -> return R.drawable.type_normal
                }
                return R.drawable.type_normal
            }

            try {
                binding.idMoveTypeValue.text = response.typeClass.typeName
                binding.moveTypeImage.setImageResource(getImageByType(response.typeClass.typeName))
            } catch (e: Exception) {
                binding.idPokemonMoveType.text = "-"
                binding.moveTypeImage.visibility = View.GONE
            }

            try {
                binding.idMoveCategoryValue.text =
                    response.metaClass.categoryClass.categoryName.replace(
                        "-",
                        " "
                    )
                binding.idMoveCategoryValue.setOnClickListener {
                    findNavController().navigate(
                        MovesDetailFragmentDirections.actionMovePokedexFragmentToMoveCategoryPokedexFragment(
                            response.metaClass.categoryClass.url
                        )
                    )
                }
            } catch (e: Exception) {
                binding.idMoveCategoryValue.text = "-"
            }

            try {
                binding.idPokemonMoveAilmentValue.text =
                    response.metaClass.ailmentClass.name
                binding.idPokemonMoveAilmentValue.setOnClickListener {
                    findNavController().navigate(
                        MovesDetailFragmentDirections.actionMovePokedexFragmentToMoveAilmentPokedexFragment(
                            response.metaClass.ailmentClass.url
                        )
                    )
                }
            } catch (e: Exception) {
                binding.idPokemonMoveAilmentValue.text = "-"
            }

            try {
                binding.idMovePowerValue.text = response.power.toString()
            } catch (e: Exception) {
                binding.idMovePowerValue.text = "-"
            }

            try {
                binding.idMoveAccuracyValue.text = response.accuracy.toString()
            } catch (e: Exception) {
                binding.idMoveAccuracyValue.text = "-"
            }

            try {
                for (value in response.effectEntries) {
                    println(value.language.languageName)
                    if (value.language.languageName == "en") binding.idMoveEffectValue.text =
                        value.shortEffect
                }
            } catch (e: Exception) {
                binding.idMoveEffectValue.text = "-"
            }

            try {
                binding.idMovePpValue.text = response.pp.toString()
            } catch (e: Exception) {
                binding.idMovePpValue.text = "-"
            }

            try {
                binding.idMoveIntroducedValue.text =
                    response.generation.generationName.removePrefix("generation-")
            } catch (e: Exception) {
                binding.idMoveIntroducedValue.text = "-"
            }

            try {
                binding.idTargetValue.text =
                    response.targetClass.targetName.replace("-", " ")
                binding.idTargetValue.setOnClickListener {
                    findNavController().navigate(
                        MovesDetailFragmentDirections.actionMovePokedexFragmentToMoveTargetPokedexFragment(
                            response.targetClass.url
                        )
                    )
                }
            } catch (e: Exception) {
                binding.idTargetValue.text = "-"
            }

            try {
                for (value in response.flavorTextEntries) {
                    println(value.language.languageName)
                    if (value.language.languageName == "en") binding.idFlavorTextMove.text =
                        value.flavorText
                }
            } catch (e: Exception) {
                binding.idFlavorTextMove.text = "no description found"
            }

            alsoUsedByAdapter?.addAlsoUsedByPokemons(response.learnedByPokemon)


        }


    }

    private fun setAdapter() {

        alsoUsedByAdapter = AlsoUsedByAdapter()
        binding.idRvMovesAlsoUsedByPokemons.adapter = alsoUsedByAdapter
        binding.idRvMovesAlsoUsedByPokemons.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        alsoUsedByAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                MovesDetailFragmentDirections.actionMovePokedexFragmentToDetailPokedexFragment(
                    it.image, false
                )
            )
        }
    }

    private fun setButtons() {

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun alertDialog(title: String, addedOrDeleted: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom)).create()

        val customLayout = layoutInflater.inflate(R.layout.alert_dialog_pokemon_added_to_list, null)

        val descriptionText = customLayout.findViewById<TextView>(R.id.pokemon_added)

        descriptionText.text = "$title $addedOrDeleted your list"

        customLayout.findViewById<TextView>(R.id.close).setOnClickListener {
            builder.dismiss()
        }

        with(builder) {
            setView(customLayout)
            show()
        }

    }

    private fun loadBanner() {
        val adRequest = AdRequest.Builder().build()
        binding.adViewBanner.loadAd(adRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.adViewBanner.destroy()

        alsoUsedByAdapter = null

        _binding = null
    }


}
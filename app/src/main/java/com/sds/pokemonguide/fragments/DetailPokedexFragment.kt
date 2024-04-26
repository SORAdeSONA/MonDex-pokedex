package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.AbilitiesAdapter
import com.sds.pokemonguide.adapter.AlertDialogListAdapter
import com.sds.pokemonguide.adapter.ItemsAdapter
import com.sds.pokemonguide.adapter.MovesAdapter
import com.sds.pokemonguide.databinding.DetailPokedexFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DetailPokedexFragment : Fragment() {

    private val viewModel: PokemonViewModel by viewModels()

    private var movesAdapter: MovesAdapter? = null
    private var abilitiesAdapter: AbilitiesAdapter? = null
    private var itemsAdapter: ItemsAdapter? = null

    private var alertDialogListsAdapter: AlertDialogListAdapter? = null

    private var alertDialogEvolutionChain: AlertDialog? = null


    private var _binding: DetailPokedexFragmentBinding? = null
    private val binding get() = _binding!!


    private val args: DetailPokedexFragmentArgs by navArgs()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusDetail.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailPokedexFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadData()
        setAdapter()
        setButtons()
        setDataViews()
        setUI()

        loadBanner()
    }

    private fun setDataViews() {

        binding.errorContainer.setOnClickListener {
            downloadData()
        }


        viewModel.loadingStatusDetail.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.idPokemonImageDetail.visibility = View.GONE
                    binding.layoutTypeOne.visibility = View.GONE
                    binding.layoutTypeTwo.visibility = View.GONE
                    binding.layoutExp.visibility = View.GONE
                    binding.openEvolutionChain.visibility = View.GONE
                    binding.pokemonItemCategory.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.flavorTextDetail.visibility = View.GONE
                    binding.detailPokemonName.visibility = View.GONE
                    binding.dataContainerDetail.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.idPokemonImageDetail.visibility = View.VISIBLE
                    binding.layoutTypeOne.visibility = View.VISIBLE
                    binding.layoutTypeTwo.visibility = View.VISIBLE
                    binding.layoutExp.visibility = View.VISIBLE
                    binding.openEvolutionChain.visibility = View.VISIBLE
                    binding.pokemonItemCategory.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.flavorTextDetail.visibility = View.VISIBLE
                    binding.detailPokemonName.visibility = View.VISIBLE
                    binding.dataContainerDetail.visibility = View.VISIBLE

                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.idPokemonImageDetail.visibility = View.GONE
                    binding.layoutTypeOne.visibility = View.GONE
                    binding.openEvolutionChain.visibility = View.GONE
                    binding.layoutTypeTwo.visibility = View.GONE
                    binding.layoutExp.visibility = View.GONE
                    binding.pokemonItemCategory.visibility = View.GONE
                    binding.flavorTextDetail.visibility = View.GONE
                    binding.detailPokemonName.visibility = View.GONE
                    binding.dataContainerDetail.visibility = View.GONE
                }
            }
        }

        viewModel.loadingStatusDetailSpecial.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {}

                LoadingStatus.SUCCESS -> {}

                LoadingStatus.ERROR -> {
                    binding.idBaseHappinessValue.text = "-"
                    binding.idCaptureRateValue.text = "-"
                    binding.idHatchCounterValue.text = "-"
                    binding.idGenerationValue.text = "-"
                    binding.idHabitatValue.text = "-"
                    binding.idShapeValue.text = "-"
                    binding.flavorTextDetail.text = "no description found"
                }
            }
        }
    }

    private fun downloadData() {
        viewLifecycleOwner.lifecycleScope.launch(coroutineExceptionHandler) {
            if (args.isSearch) {
                viewModel.downloadPokemonDetails(args.pokemonImage)
                viewModel.downloadPokemonSpecies(args.pokemonImage)
            } else {
                if (args.pokemonImage.length > 5){
                    val id = args.pokemonImage.removePrefix(CONSTANTS.BASE_POKEMON_PREFIX).dropLast(1)
                    viewModel.downloadPokemonDetails(id)
                    viewModel.downloadPokemonSpecies(id)
                }else{
                    viewModel.downloadPokemonDetails(args.pokemonImage)
                    viewModel.downloadPokemonSpecies(args.pokemonImage)
                }



            }

        }
    }

    private fun setUI() {
        viewModel.pokemonDetailResponse.observe(viewLifecycleOwner) { response ->


            binding.savedNo.setOnClickListener {
                alertDialogAddToList(response.pokemonName, args.pokemonImage)
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
                binding.typeOneImage.setImageResource(getImageByType(response.types[0].type.typeName))
                binding.typeOne.text = response.types[0].type.typeName
                binding.layoutTypeOne.visibility = View.VISIBLE

                binding.layoutTypeOne.setOnClickListener {
                    findNavController().navigate(
                        DetailPokedexFragmentDirections.actionDetailPokedexFragmentToTypePokedexFragment(
                            response.types[0].type.url
                        )
                    )
                }

            } catch (e: Exception) {
                binding.layoutTypeOne.visibility = View.GONE
                println(e)
            }


            try {
                binding.typeTwoImage.setImageResource(getImageByType(response.types[1].type.typeName))
                binding.typeTwo.text = response.types[1].type.typeName
                binding.layoutTypeTwo.visibility = View.VISIBLE

                binding.layoutTypeTwo.setOnClickListener {
                    findNavController().navigate(
                        DetailPokedexFragmentDirections.actionDetailPokedexFragmentToTypePokedexFragment(
                            response.types[1].type.url
                        )
                    )
                }
            } catch (e: Exception) {
                binding.layoutTypeTwo.visibility = View.GONE
                println(e)
            }

            binding.expValue.text = response.baseExperience.toString()


            binding.detailPokemonName.text =
                "#${response.id} " + response.pokemonName.replace("-", " ")


            binding.idPokemonHp.text = "${response.stats[0].baseStat}"
            binding.idPokemonAttack.text = "${response.stats[1].baseStat}"
            binding.idPokemonDefence.text = "${response.stats[2].baseStat}"
            binding.idPokemonSpecialAttack.text = "${response.stats[3].baseStat}"
            binding.idPokemonSpecialDefence.text = "${response.stats[4].baseStat}"
            binding.idPokemonSpeed.text = "${response.stats[5].baseStat}"

            val totalPoints =
                response.stats[0].baseStat +
                        response.stats[1].baseStat +
                        response.stats[2].baseStat +
                        response.stats[3].baseStat +
                        response.stats[4].baseStat +
                        response.stats[5].baseStat
            binding.idPokemonTotal.text = totalPoints.toString()


            val displayWidth = resources.displayMetrics.widthPixels
            val viewWidth = displayWidth / 2 + displayWidth / 4


            val layoutParamsHp = binding.idPokemonHp.layoutParams as ConstraintLayout.LayoutParams

            var widthHp = response.stats[0].baseStat * displayWidth / 200

            if (widthHp <= 180) widthHp = 180
            if (widthHp >= viewWidth) {
                layoutParamsHp.width = viewWidth
                binding.idPokemonHp.layoutParams = layoutParamsHp
            } else {
                layoutParamsHp.width = widthHp
                binding.idPokemonHp.layoutParams = layoutParamsHp
            }


            val layoutParamsAttack =
                binding.idPokemonAttack.layoutParams as ConstraintLayout.LayoutParams
            var widthAttack = response.stats[1].baseStat * displayWidth / 200

            if (widthAttack <= 180) widthAttack = 180
            if (widthAttack >= viewWidth) {
                layoutParamsAttack.width = viewWidth
                binding.idPokemonAttack.layoutParams = layoutParamsAttack
            } else {
                layoutParamsAttack.width = widthAttack
                binding.idPokemonAttack.layoutParams = layoutParamsAttack
            }


            val layoutParamsDefence =
                binding.idPokemonDefence.layoutParams as ConstraintLayout.LayoutParams
            var widthDefence = response.stats[2].baseStat * displayWidth / 200

            if (widthDefence <= 180) widthDefence = 180
            if (widthDefence >= viewWidth) {
                layoutParamsDefence.width = viewWidth
                binding.idPokemonDefence.layoutParams = layoutParamsDefence
            } else {
                layoutParamsDefence.width = widthDefence
                binding.idPokemonDefence.layoutParams = layoutParamsDefence
            }


            val layoutParamsSpAttack =
                binding.idPokemonSpecialAttack.layoutParams as ConstraintLayout.LayoutParams
            var widthSpAttack = response.stats[3].baseStat * displayWidth / 200

            if (widthSpAttack <= 180) widthSpAttack = 180
            if (widthSpAttack >= viewWidth) {
                layoutParamsSpAttack.width = viewWidth
                binding.idPokemonSpecialAttack.layoutParams = layoutParamsSpAttack
            } else {
                layoutParamsSpAttack.width = widthSpAttack
                binding.idPokemonSpecialAttack.layoutParams = layoutParamsSpAttack
            }


            val layoutParamsSpDefence =
                binding.idPokemonSpecialDefence.layoutParams as ConstraintLayout.LayoutParams
            var widthSpDefence = response.stats[4].baseStat * displayWidth / 200

            if (widthSpDefence <= 180) widthSpDefence = 180
            if (widthSpDefence >= viewWidth) {
                layoutParamsSpDefence.width = viewWidth
                binding.idPokemonSpecialDefence.layoutParams = layoutParamsSpDefence
            } else {
                layoutParamsSpDefence.width = widthSpDefence
                binding.idPokemonSpecialDefence.layoutParams = layoutParamsSpDefence
            }

            val layoutParamsSpeed =
                binding.idPokemonSpeed.layoutParams as ConstraintLayout.LayoutParams
            var widthSpeed = response.stats[5].baseStat * displayWidth / 200

            if (widthSpeed <= 180) widthSpeed = 180
            if (widthSpeed >= viewWidth) {
                layoutParamsSpeed.width = viewWidth
                binding.idPokemonSpeed.layoutParams = layoutParamsSpeed
            } else {
                layoutParamsSpeed.width = widthSpeed
                binding.idPokemonSpeed.layoutParams = layoutParamsSpeed
            }

            val layoutParamsTotal =
                binding.idPokemonTotal.layoutParams as ConstraintLayout.LayoutParams
            var widthTotal = totalPoints * displayWidth / 200 / 5

            if (widthTotal <= 180) widthTotal = 180
            if (widthTotal >= viewWidth) {
                layoutParamsTotal.width = viewWidth
                binding.idPokemonTotal.layoutParams = layoutParamsTotal
            } else {
                layoutParamsTotal.width = widthTotal
                binding.idPokemonTotal.layoutParams = layoutParamsTotal
            }


            val layoutParamsViewHealth =
                binding.idViewHealth.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewHealth.width = viewWidth
            binding.idViewHealth.layoutParams = layoutParamsViewHealth


            val layoutParamsViewAttack =
                binding.idViewAttack.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewAttack.width = viewWidth
            binding.idViewAttack.layoutParams = layoutParamsViewAttack


            val layoutParamsViewDefence =
                binding.idViewDefence.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewDefence.width = viewWidth
            binding.idViewDefence.layoutParams = layoutParamsViewDefence


            val layoutParamsViewSpAttack =
                binding.idViewSpAttack.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewSpAttack.width = viewWidth
            binding.idViewSpAttack.layoutParams = layoutParamsViewSpAttack


            val layoutParamsViewSpDefence =
                binding.idViewSpDefence.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewSpDefence.width = viewWidth
            binding.idViewSpDefence.layoutParams = layoutParamsViewSpDefence


            val layoutParamsViewSpeed =
                binding.idViewSpeed.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewSpeed.width = viewWidth
            binding.idViewSpeed.layoutParams = layoutParamsViewSpeed

            val layoutParamsViewTotal =
                binding.idViewTotal.layoutParams as ConstraintLayout.LayoutParams
            layoutParamsViewTotal.width = viewWidth
            binding.idViewTotal.layoutParams = layoutParamsViewTotal


            if (response.moves.isEmpty()) {
                binding.moves.text = "moves not found"
            }

            if (response.moves.size > 6) {

                binding.idShowAllMoves.visibility = View.VISIBLE

            } else {

                binding.idShowAllMoves.visibility = View.GONE
                val layoutParamsMovesRv =
                    binding.idRvMoves.layoutParams as ConstraintLayout.LayoutParams

                layoutParamsMovesRv.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.idRvMoves.layoutParams = layoutParamsMovesRv

            }




            movesAdapter?.addMoves(response.moves)
            abilitiesAdapter?.addAbilities(response.abilities)
            if (response.items.isEmpty()) {
                binding.items.text = "no held items"
            } else itemsAdapter?.addItems(response.items)

            val height = response.height * 100f / 1000f

            val weight = response.weight * 100f / 1000f

            binding.idHeightValueDetail.text = "${height}m"
            binding.idWeightValueDetail.text = "${weight}kg"

            Glide.with(binding.idPokemonImageDetail)
                .load(CONSTANTS.getPokemonSpritesById(response.id))
                .into(binding.idPokemonImageDetail)


        }

        viewModel.pokemonSpeciesResponse.observe(viewLifecycleOwner) { response ->


            binding.openEvolutionChain.setOnClickListener {
                viewModel.pokemonDetailResponse.value?.let { it1 ->
                    alertDialogEvolutionChain(
                        pokemonName = it1.pokemonName
                    )
                }
            }

            try {

                for (value in response.flavorTextEntries) {
                    if (value.language.languageName == "en") binding.flavorTextDetail.text =
                        value.flavorText
                }

                binding.flavorTextDetail.visibility = View.VISIBLE
            } catch (e: Exception) {
                binding.flavorTextDetail.text = "unknown"
            }


            val generation = response.generation.generationName.removePrefix("generation-")

            binding.idBaseHappinessValue.text = response.baseHappiness.toString()
            binding.idCaptureRateValue.text = response.captureRate.toString()
            binding.idHatchCounterValue.text = response.hatchCounter.toString()
            binding.idGenerationValue.text = generation
            try {
                binding.idHabitatValue.text =
                    response.habitat.habitatName.replace("-", " ")
                binding.idHabitatValue.setOnClickListener {
                    findNavController().navigate(
                        DetailPokedexFragmentDirections.actionDetailPokedexFragmentToHabitatPokedexFragment(
                            response.habitat.url
                        )
                    )
                }
            } catch (e: Exception) {
                binding.idHabitatValue.text = "unknown"
            }
            try {
                binding.idShapeValue.text = response.shape.shapeName.replace("-", " ")
                binding.idShapeValue.setOnClickListener {
                    findNavController().navigate(
                        DetailPokedexFragmentDirections.actionDetailPokedexFragmentToShapePokedexFragment(
                            response.shape.url
                        )
                    )
                }
            } catch (e: Exception) {
                binding.idShapeValue.text = "unknown"
            }


        }
    }

    private fun setAdapter() {
        movesAdapter = MovesAdapter()
        binding.idRvMoves.adapter = movesAdapter
        binding.idRvMoves.layoutManager =
            GridLayoutManager(this.context, 2)


        abilitiesAdapter = AbilitiesAdapter()
        binding.idRvAbilities.adapter = abilitiesAdapter
        binding.idRvAbilities.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        itemsAdapter = ItemsAdapter()
        binding.idRvItems.adapter = itemsAdapter
        binding.idRvItems.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun setButtons() {


        binding.idShowAllMoves.setOnClickListener {
            val layoutParamsRvMoves =
                binding.idRvMoves.layoutParams as ConstraintLayout.LayoutParams

            layoutParamsRvMoves.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.idRvMoves.layoutParams = layoutParamsRvMoves
            binding.idShowAllMoves.visibility = View.GONE
        }


        movesAdapter?.setOnItemClickListenerUrlMove {
            findNavController().navigate(
                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToMovePokedexFragment(
                    it, false
                )
            )
        }

        itemsAdapter?.setOnItemClickListenerUrlItem {
            findNavController().navigate(
                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToItemPokedexFragment(
                    it, false
                )
            )
        }

        abilitiesAdapter?.setOnItemClickListenerUrlAbility {
            findNavController().navigate(
                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToAbilityPokedexFragment(
                    it, false
                )
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun alertDialogAddToList(pokemonName: String, pokemonUrl: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_layout_add_to_list, null)

        val close = dialogLayout.findViewById<TextView>(R.id.close_add_to_list)

        val saveToDefaultCategory =
            dialogLayout.findViewById<ConstraintLayout>(R.id.add_to_default_category)

        val isPokemonSavedInDefaultCategory =
            dialogLayout.findViewById<ImageView>(R.id.is_in_list_rv_item)


        close.setOnClickListener {
            builder.dismiss()
        }

        //check if pokemon is in default list
        viewModel.viewModelScope.launch {

            val list = withContext(Dispatchers.IO) {
                viewModel.getAllPokemonsFromDb()
            }


            if (list.isEmpty()) {
                isPokemonSavedInDefaultCategory.visibility = View.GONE
            }

            var pokemonFound = false

            if (list.isNotEmpty()) {
                for (value in list) {
                    if (value.pokemonName == pokemonName) {
                        pokemonFound = true
                        break
                    }
                }
            }

            if (pokemonFound) {
                isPokemonSavedInDefaultCategory.visibility = View.VISIBLE
            } else {
                isPokemonSavedInDefaultCategory.visibility = View.GONE
            }


        }

        saveToDefaultCategory.setOnClickListener {
            viewModel.viewModelScope.launch {

                val list = withContext(Dispatchers.IO) {
                    viewModel.getAllPokemonsFromDb()
                }

                if (list.isEmpty()) {
                    withContext(Dispatchers.IO) {
                        viewModel.addPokemonToDb(pokemonName, pokemonUrl)
                    }
                    isPokemonSavedInDefaultCategory.visibility = View.VISIBLE
                } else {
                    var pokemonFound = false
                    for (i in list) {
                        if (i.pokemonName == pokemonName) {
                            pokemonFound = true

                            withContext(Dispatchers.IO) {
                                viewModel.deletePokemonFromDb(pokemonName)
                            }

                            Toast.makeText(
                                this@DetailPokedexFragment.context,
                                "$pokemonName deleted from default list",
                                Toast.LENGTH_SHORT
                            ).show()

                            isPokemonSavedInDefaultCategory.visibility = View.GONE

                            break
                        }
                    }
                    if (!pokemonFound) {
                        withContext(Dispatchers.IO) {
                            viewModel.addPokemonToDb(pokemonName, pokemonUrl)
                        }
                        Toast.makeText(
                            this@DetailPokedexFragment.context,
                            "$pokemonName added to default list",
                            Toast.LENGTH_SHORT
                        ).show()



                        isPokemonSavedInDefaultCategory.visibility = View.VISIBLE
                    }
                }
            }
        }


        alertDialogListsAdapter = AlertDialogListAdapter(viewModel)
        val recyclerViewListNames = dialogLayout.findViewById<RecyclerView>(R.id.add_list_rv)


        recyclerViewListNames.adapter = alertDialogListsAdapter
        recyclerViewListNames.layoutManager = LinearLayoutManager(this.context)


        viewModel.viewModelScope.launch {

            val listsFromDb = withContext(Dispatchers.IO) {
                viewModel.getAllLists()
            }

            if (listsFromDb.isEmpty()) {
                dialogLayout.findViewById<TextView>(R.id.save_to_my_list).text =
                    "you haven't created any list"
            } else {
                alertDialogListsAdapter!!.addLists(listsFromDb)
            }
        }

        alertDialogListsAdapter!!.setOnItemClickListenerName {
            viewModel.viewModelScope.launch {
                val allPokemonsFromCustomLists = withContext(Dispatchers.IO) {
                    viewModel.getAllSavedListsPokemons()
                }

                var isPokemonInList = false

                for (value in allPokemonsFromCustomLists) {
                    if (value.listName == it.listName && value.pokemonName == pokemonName) {
                        isPokemonInList = true
                        break
                    }
                }

                if (!isPokemonInList) {
                    withContext(Dispatchers.IO) {
                        viewModel.addPokemonToSavedLists(pokemonName, pokemonUrl, it.listName)
                    }


                    val listsFromDb = withContext(Dispatchers.IO) {
                        viewModel.getAllLists()
                    }

                    withContext(Dispatchers.Main) {
                        alertDialogListsAdapter!!.updateList(it.position, listsFromDb[it.position])
                    }

                    Toast.makeText(
                        this@DetailPokedexFragment.context,
                        "$pokemonName added to $it",
                        Toast.LENGTH_SHORT
                    ).show()


                } else {
                    alertDialogPokemonIsInList(pokemonName, it.listName, it.position)
                }
            }
        }

        builder.setView(dialogLayout)
        builder.show()


    }

    private fun alertDialogPokemonIsInList(
        pokemonName: String,
        listName: String,
        positionToUpdate: Int
    ) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_pokemon_is_in_list, null)

        val close = dialogLayout.findViewById<TextView>(R.id.close)
        val delete = dialogLayout.findViewById<TextView>(R.id.delete)
        val pokemonIsInList = dialogLayout.findViewById<TextView>(R.id.pokemon_is_in_list)

        pokemonIsInList.text = "$pokemonName is in $listName list already"

        close.setOnClickListener {
            builder.dismiss()
        }

        delete.setOnClickListener {
            viewModel.viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.deletePokemonFromSavedLists(pokemonName, listName)

                    val listsFromDb = withContext(Dispatchers.IO) {
                        viewModel.getAllLists()
                    }

                    withContext(Dispatchers.Main) {
                        alertDialogListsAdapter!!.updateList(
                            positionToUpdate,
                            listsFromDb[positionToUpdate]
                        )
                    }

                }
                Toast.makeText(
                    this@DetailPokedexFragment.context,
                    "$pokemonName deleted from $listName",
                    Toast.LENGTH_SHORT
                ).show()

                builder.dismiss()
            }
        }

        builder.setView(dialogLayout)
        builder.show()
    }


    private fun alertDialogEvolutionChain(pokemonName: String) {

        alertDialogEvolutionChain?.dismiss()

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_evolution_chain, null)

        val title = dialogLayout.findViewById<TextView>(R.id.evolution_chain_title)
        title.text = "$pokemonName's evolution chain"

        val progressBar = dialogLayout.findViewById<ProgressBar>(R.id.progress_bar_loading_evol)

        val pokemonName1 = dialogLayout.findViewById<TextView>(R.id.evolution_chain_pokemon_title_1)
        val pokemonName2 = dialogLayout.findViewById<TextView>(R.id.evolution_chain_pokemon_title_2)
        val pokemonName3 = dialogLayout.findViewById<TextView>(R.id.evolution_chain_pokemon_title_3)
        val pokemonName4 = dialogLayout.findViewById<TextView>(R.id.evolution_chain_pokemon_title_4)


        val container1 = dialogLayout.findViewById<ConstraintLayout>(R.id.constraintLayout1)
        val container2 = dialogLayout.findViewById<ConstraintLayout>(R.id.constraintLayout2)
        val container3 = dialogLayout.findViewById<ConstraintLayout>(R.id.constraintLayout3)
        val container4 = dialogLayout.findViewById<ConstraintLayout>(R.id.constraintLayout4)

        val view1to2 = dialogLayout.findViewById<ImageView>(R.id.view1to2)
        val view2to3 = dialogLayout.findViewById<ImageView>(R.id.view2to3)
        val view3to4 = dialogLayout.findViewById<ImageView>(R.id.view3to4)

        val trigger1to2 = dialogLayout.findViewById<TextView>(R.id.trigger1to2)
        val trigger2to3 = dialogLayout.findViewById<TextView>(R.id.trigger2to3)
        val trigger3to4 = dialogLayout.findViewById<TextView>(R.id.trigger3to4)

        val pokemonImage1 = dialogLayout.findViewById<ImageView>(R.id.evolution_chain_pokemon_image_1)
        val pokemonImage2 = dialogLayout.findViewById<ImageView>(R.id.evolution_chain_pokemon_image_2)
        val pokemonImage3 = dialogLayout.findViewById<ImageView>(R.id.evolution_chain_pokemon_image_3)
        val pokemonImage4 = dialogLayout.findViewById<ImageView>(R.id.evolution_chain_pokemon_image_4)

        val close = dialogLayout.findViewById<TextView>(R.id.close)
        close.setOnClickListener { builder.dismiss() }


        val evolutionChainId = viewModel.pokemonSpeciesResponse.value?.evolutionChain?.url
            ?.removePrefix("https://pokeapi.co/api/v2/evolution-chain/")!!.dropLast(1)

        viewModel.viewModelScope.launch {
            progressBar.visibility = View.VISIBLE
            withContext(Dispatchers.IO){

                val evolutionChainPokemon = viewModel.downloadPokemonEvolutionChain(evolutionChainId)

                withContext(Dispatchers.Main){

                    try {
                        pokemonName1.text = evolutionChainPokemon.chain.species.name

                        progressBar.visibility = View.GONE

                        val imageId1 = evolutionChainPokemon.chain.species.url
                            .removePrefix("https://pokeapi.co/api/v2/pokemon-species/")
                            .dropLast(1)

                        Glide.with(pokemonImage1)
                            .load(CONSTANTS.getPokemonSpritesById(imageId1))
                            .into(pokemonImage1)

                        pokemonImage1.visibility = View.VISIBLE

                        pokemonName1.visibility = View.VISIBLE


                        container1.setOnClickListener {

                            findNavController().navigate(
                                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToDetailPokedexFragment(imageId1, false))
                            builder.dismiss()
                        }

                    }catch (e:Exception){
                        println("p1")
                    }

                    try {
                        pokemonName2.text = evolutionChainPokemon.chain.evolves_to[0].species.name

                        val imageId2 = evolutionChainPokemon.chain.evolves_to[0].species.url
                            .removePrefix("https://pokeapi.co/api/v2/pokemon-species/")
                            .dropLast(1)

                        Glide.with(pokemonImage2)
                            .load(CONSTANTS.getPokemonSpritesById(imageId2))
                            .into(pokemonImage2)

                        if(evolutionChainPokemon.chain.evolves_to[0].evolution_details[0].trigger.name == "level-up"){
                            trigger1to2.text = evolutionChainPokemon.chain.evolves_to[0].evolution_details[0].min_level.toString()
                        }else{
                            trigger1to2.text = evolutionChainPokemon.chain.evolves_to[0].evolution_details[0].trigger.name.replace("-", " ")
                        }

                        trigger1to2.visibility = View.VISIBLE


                        pokemonImage2.visibility = View.VISIBLE

                        pokemonName2.visibility = View.VISIBLE
                        view1to2.visibility = View.VISIBLE

                        container2.setOnClickListener {
                            findNavController().navigate(
                                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToDetailPokedexFragment(imageId2, false))
                            builder.dismiss()

                        }


                    }catch (e:Exception){
                        println("p2")
                    }

                    try {
                        pokemonName3.text = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].species.name

                        val imageId3 = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].species.url
                            .removePrefix("https://pokeapi.co/api/v2/pokemon-species/")
                            .dropLast(1)

                        Glide.with(pokemonImage3)
                            .load(CONSTANTS.getPokemonSpritesById(imageId3))
                            .into(pokemonImage3)

                        pokemonImage3.visibility = View.VISIBLE

                        if(evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolution_details[0].trigger.name == "level-up"){
                            trigger2to3.text = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolution_details[0].min_level.toString()
                        }else{
                            trigger2to3.text = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolution_details[0].trigger.name.replace("-", " ")
                        }

                        trigger2to3.visibility = View.VISIBLE

                        pokemonName3.visibility = View.VISIBLE
                        view2to3.visibility = View.VISIBLE

                        container3.setOnClickListener {
                            findNavController().navigate(
                                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToDetailPokedexFragment(imageId3, false))
                            builder.dismiss()

                        }

                    }catch (e:Exception){
                        println("p3")
                    }

                    try {
                        pokemonName4.text = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolves_to[0].species.name

                        val imageId4 = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolves_to[0].species.name
                            .removePrefix("https://pokeapi.co/api/v2/pokemon-species/")
                            .dropLast(1)

                        Glide.with(pokemonImage4)
                            .load(CONSTANTS.getPokemonSpritesById(imageId4))
                            .into(pokemonImage3)

                        pokemonImage4.visibility = View.VISIBLE

                        if(evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolves_to[0].evolution_details[0].trigger.name == "level-up"){
                            trigger3to4.text = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolves_to[0].evolution_details[0].min_level.toString()
                        }else{
                            trigger3to4.text = evolutionChainPokemon.chain.evolves_to[0].evolves_to[0].evolves_to[0].evolution_details[0].trigger.name.replace("-", " ")
                        }

                        trigger3to4.visibility = View.VISIBLE

                        pokemonName4.visibility = View.VISIBLE
                        view3to4.visibility = View.VISIBLE

                        container4.setOnClickListener {
                            findNavController().navigate(
                                DetailPokedexFragmentDirections.actionDetailPokedexFragmentToDetailPokedexFragment(imageId4, false))
                            builder.dismiss()

                        }

                    }catch (e:Exception){
                        println("p4")
                    }

                }
            }
        }

        alertDialogEvolutionChain = builder


        builder.setView(dialogLayout)
        builder.show()
    }


    private fun loadBanner() {
        val adRequest = AdRequest.Builder().build()
        binding.adViewBanner.loadAd(adRequest)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        abilitiesAdapter = null
        itemsAdapter = null
        movesAdapter = null
        alertDialogListsAdapter = null
        alertDialogEvolutionChain = null

        binding.adViewBanner.destroy()

        _binding = null
    }


}
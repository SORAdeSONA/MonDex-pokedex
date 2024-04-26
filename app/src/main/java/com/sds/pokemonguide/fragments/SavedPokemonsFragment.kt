package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.SavedBerriesAdapter
import com.sds.pokemonguide.adapter.SavedItemsAdapter
import com.sds.pokemonguide.adapter.SavedMovesAdapter
import com.sds.pokemonguide.adapter.SavedPokemonsAdapter
import com.sds.pokemonguide.adapter.SavedPokemonsFragmentCustomListsAdapter
import com.sds.pokemonguide.databinding.SavedPokemonsFragmentBinding
import com.sds.pokemonguide.room.RoomBerryModel
import com.sds.pokemonguide.room.RoomCustomListNamesModel
import com.sds.pokemonguide.room.RoomItemModel
import com.sds.pokemonguide.room.RoomMoveModel
import com.sds.pokemonguide.room.RoomPokemonModel
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SavedPokemonsFragment : Fragment() {

    private val viewModel: PokemonViewModel by viewModels()

    private var savedPokemonsAdapter: SavedPokemonsAdapter? = null
    private var savedItemsAdapter: SavedItemsAdapter? = null
    private var savedBerriesAdapter: SavedBerriesAdapter? = null
    private var savedMovesAdapter: SavedMovesAdapter? = null
    private var customListsAdapter: SavedPokemonsFragmentCustomListsAdapter? = null

    private var _binding: SavedPokemonsFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentCategory = "pokemon"

    private var sortTypePokemon = "none"
    private var sortTypeItem = "none"
    private var sortTypeBerry = "none"
    private var sortTypeMove = "none"
    private var sortTypeLists = "none"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SavedPokemonsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        downloadPokemonList()
        setButtons()
        setAdapter()
    }

    private fun setButtons() {

        binding.categoryPokemon.setOnClickListener {
            currentCategory = "pokemon"
            downloadPokemonList()
        }

        binding.categoryItem.setOnClickListener {
            currentCategory = "item"
            downloadPokemonList()
        }

        binding.categoryMoves.setOnClickListener {
            currentCategory = "move"
            downloadPokemonList()
        }

        binding.categoryBerries.setOnClickListener {
            currentCategory = "berry"
            downloadPokemonList()
        }

        binding.categoryCustomList.setOnClickListener {
            currentCategory = "custom_list"
            downloadPokemonList()
        }

        binding.filter.setOnClickListener {
            when (currentCategory) {
                "pokemon" -> {
                    alertDialogSort("sort pokemon by")
                }

                "item" -> {
                    alertDialogSort("sort item by")
                }

                "move" -> {
                    alertDialogSort("sort move by")
                }

                "berry" -> {
                    alertDialogSort("sort berry by")
                }
            }
        }

        binding.newCategory.setOnClickListener {
            alertDialogCreateNewList()
        }

        binding.listsFilter.setOnClickListener {
            alertDialogSortLists()
        }


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun downloadPokemonList() {

        viewModel.viewModelScope.launch {
            when (currentCategory) {
                "pokemon" -> {
                    binding.empty.visibility = View.GONE
                    binding.categoryPokemon.setBackgroundResource(R.drawable.left_corner_category)
                    binding.categoryItem.background = null
                    binding.categoryBerries.background = null
                    binding.categoryMoves.background = null
                    binding.categoryCustomList.background = null
                    binding.filter.visibility = View.VISIBLE
                    binding.savedPokemonsRv.visibility = View.VISIBLE
                    binding.savedItemsRv.visibility = View.GONE
                    binding.savedMovesRv.visibility = View.GONE
                    binding.savedBerriesRv.visibility = View.GONE
                    binding.customListsContainer.visibility = View.GONE

                    downloadAndDisplayPokemons()
                }

                "item" -> {
                    binding.empty.visibility = View.GONE
                    binding.categoryItem.setBackgroundResource(R.drawable.center_corner_category)
                    binding.categoryPokemon.background = null
                    binding.categoryBerries.background = null
                    binding.categoryMoves.background = null
                    binding.categoryCustomList.background = null
                    binding.filter.visibility = View.VISIBLE
                    binding.savedPokemonsRv.visibility = View.GONE
                    binding.savedBerriesRv.visibility = View.GONE
                    binding.savedMovesRv.visibility = View.GONE
                    binding.customListsContainer.visibility = View.GONE
                    binding.savedItemsRv.visibility = View.VISIBLE

                    downloadAndDisplayItems()
                }

                "berry" -> {
                    binding.empty.visibility = View.GONE
                    binding.categoryBerries.setBackgroundResource(R.drawable.center_corner_category)
                    binding.categoryItem.background = null
                    binding.categoryPokemon.background = null
                    binding.categoryMoves.background = null
                    binding.categoryCustomList.background = null
                    binding.filter.visibility = View.VISIBLE
                    binding.savedPokemonsRv.visibility = View.GONE
                    binding.savedItemsRv.visibility = View.GONE
                    binding.savedMovesRv.visibility = View.GONE
                    binding.customListsContainer.visibility = View.GONE
                    binding.savedBerriesRv.visibility = View.VISIBLE

                    downloadAndDisplayBerries()
                }

                "move" -> {
                    binding.empty.visibility = View.GONE
                    binding.categoryMoves.setBackgroundResource(R.drawable.center_corner_category)
                    binding.categoryBerries.background = null
                    binding.categoryItem.background = null
                    binding.categoryPokemon.background = null
                    binding.categoryCustomList.background = null
                    binding.filter.visibility = View.VISIBLE
                    binding.savedPokemonsRv.visibility = View.GONE
                    binding.savedItemsRv.visibility = View.GONE
                    binding.savedBerriesRv.visibility = View.GONE
                    binding.customListsContainer.visibility = View.GONE
                    binding.savedMovesRv.visibility = View.VISIBLE

                    downloadAndDisplayMoves()
                }

                "custom_list" -> {
                    binding.empty.visibility = View.GONE
                    binding.categoryCustomList.setBackgroundResource(R.drawable.right_corner_category)
                    binding.categoryMoves.background = null
                    binding.categoryBerries.background = null
                    binding.categoryItem.background = null
                    binding.categoryPokemon.background = null
                    binding.filter.visibility = View.GONE
                    binding.savedPokemonsRv.visibility = View.GONE
                    binding.savedItemsRv.visibility = View.GONE
                    binding.savedBerriesRv.visibility = View.GONE
                    binding.savedMovesRv.visibility = View.GONE
                    binding.customListsContainer.visibility = View.VISIBLE

                    downloadAndDisplayCustomLists()
                }
            }
        }
    }

    private suspend fun downloadAndDisplayPokemons() {
        withContext(Dispatchers.IO) {
            val savedPokemonsList = viewModel.getAllPokemonsFromDb()
            withContext(Dispatchers.Main) {
                sortPokemonsList(savedPokemonsList)
            }
        }
    }

    private suspend fun downloadAndDisplayItems() {
        withContext(Dispatchers.IO) {
            val savedItemsList = viewModel.getAllItemsFromDb()
            withContext(Dispatchers.Main) {
                sortItemsList(savedItemsList)
            }
        }
    }

    private suspend fun downloadAndDisplayBerries() {
        withContext(Dispatchers.IO) {
            val savedBerriesList = viewModel.getAllBerriesFromDb()
            withContext(Dispatchers.Main) {
                sortBerriesList(savedBerriesList)
            }
        }
    }

    private suspend fun downloadAndDisplayMoves() {
        withContext(Dispatchers.IO) {
            val savedMovesList = viewModel.getAllMovesFromDb()
            withContext(Dispatchers.Main) {
                sortMovesList(savedMovesList)
            }
        }
    }

    private suspend fun downloadAndDisplayCustomLists() {
        withContext(Dispatchers.IO) {
            val savedCustomLists = viewModel.getAllLists()
            withContext(Dispatchers.Main) {
                sortCustomLists(savedCustomLists)
            }
        }
    }


    private fun sortPokemonsList(savedPokemonsList: List<RoomPokemonModel>) {
        if (savedPokemonsList.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
        } else {
            when (sortTypePokemon) {
                "none" -> {
                    savedPokemonsAdapter?.addPokemons(savedPokemonsList)
                }

                "by alphabet" -> {
                    savedPokemonsAdapter?.addPokemons(savedPokemonsList.sortedBy { it.pokemonName })
                }

                "by id" -> {
                    savedPokemonsAdapter?.addPokemons(savedPokemonsList.sortedBy {
                        it.pokemonUrl.removePrefix("https://pokeapi.co/api/v2/pokemon/").dropLast(1)
                            .toInt()
                    })
                }
            }
        }
    }

    private fun sortItemsList(savedItemsList: List<RoomItemModel>) {
        if (savedItemsList.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
        } else {
            when (sortTypeItem) {
                "none" -> {
                    savedItemsAdapter?.addItems(savedItemsList)
                }

                "by alphabet" -> {
                    savedItemsAdapter?.addItems(savedItemsList.sortedBy { it.itemName })
                }
            }
        }
    }

    private fun sortBerriesList(savedBerriesList: List<RoomBerryModel>) {
        if (savedBerriesList.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
        } else {
            when (sortTypeBerry) {
                "none" -> {
                    savedBerriesAdapter?.addBerries(savedBerriesList)
                }

                "by alphabet" -> {
                    savedBerriesAdapter?.addBerries(savedBerriesList.sortedBy { it.berryName })
                }
            }
        }
    }

    private fun sortMovesList(savedMovesList: List<RoomMoveModel>) {
        if (savedMovesList.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
        } else {
            when (sortTypeMove) {
                "none" -> {
                    savedMovesAdapter?.addMoves(savedMovesList)
                }

                "by alphabet" -> {
                    savedMovesAdapter?.addMoves(savedMovesList.sortedBy { it.moveName })
                }
            }
        }
    }

    private fun sortCustomLists(savedCustomLists: List<RoomCustomListNamesModel>) {
        if (savedCustomLists.isEmpty()) {
            binding.noLists.visibility = View.VISIBLE
        } else {
            binding.noLists.visibility = View.GONE
            when (sortTypeLists) {
                "none" -> {
                    customListsAdapter?.addLists(savedCustomLists)
                }

                "by alphabet" -> {
                    customListsAdapter?.addLists(savedCustomLists.sortedBy { it.name })
                }
            }
        }
    }

    private fun setAdapter() {

        savedPokemonsAdapter = SavedPokemonsAdapter()

        val layoutManagerPokemon = GridLayoutManager(this.context, 2)

        binding.savedPokemonsRv.layoutManager = layoutManagerPokemon
        binding.savedPokemonsRv.adapter = savedPokemonsAdapter


        savedPokemonsAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                SavedPokemonsFragmentDirections.actionSavedPokemonsFragmentToPokemonDetailFragment(
                    it, false
                )
            )
        }


        savedItemsAdapter = SavedItemsAdapter()

        val layoutManagerItem = GridLayoutManager(this.context, 2)

        binding.savedItemsRv.layoutManager = layoutManagerItem
        binding.savedItemsRv.adapter = savedItemsAdapter


        savedItemsAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                SavedPokemonsFragmentDirections.actionSavedPokemonsFragmentToItemDetailFragment(
                    it, false
                )
            )
        }


        savedBerriesAdapter = SavedBerriesAdapter()

        val layoutManagerBerry = GridLayoutManager(this.context, 2)

        binding.savedBerriesRv.layoutManager = layoutManagerBerry
        binding.savedBerriesRv.adapter = savedBerriesAdapter


        savedBerriesAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                SavedPokemonsFragmentDirections.actionSavedPokemonsFragmentToBerryDetailFragment(
                    it
                )
            )
        }

        savedMovesAdapter = SavedMovesAdapter()

        val layoutManagerMove = GridLayoutManager(this.context, 2)

        binding.savedMovesRv.layoutManager = layoutManagerMove
        binding.savedMovesRv.adapter = savedMovesAdapter


        savedMovesAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                SavedPokemonsFragmentDirections.actionSavedPokemonsFragmentToMoveDetailFragment(
                    it, false
                )
            )
        }

        customListsAdapter = SavedPokemonsFragmentCustomListsAdapter(viewModel)

        val layoutManagerLists = LinearLayoutManager(this.context)

        binding.customListsRv.layoutManager = layoutManagerLists
        binding.customListsRv.adapter = customListsAdapter

        customListsAdapter!!.setOnItemClickListenerListName {
            findNavController().navigate(
                SavedPokemonsFragmentDirections.actionSavedPokemonsFragmentToCustomListFragment(
                    it.listName, it.listDescription
                )
            )
        }


    }

    private fun alertDialogSort(title: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))

        val items = arrayOf("default", "by alphabet")
        val itemsPokemon = arrayOf("default", "by alphabet", "by id")

        val customTitle = layoutInflater.inflate(R.layout.alert_dialog_sort_by_title, null)

        customTitle.findViewById<TextView>(R.id.sort_by_title).text = title

        with(builder) {

            if (currentCategory == "pokemon") {
                setItems(itemsPokemon) { dialog, which ->
                    when (itemsPokemon[which]) {
                        "default" -> {
                            sortTypePokemon = "none"
                            downloadPokemonList()
                        }

                        "by alphabet" -> {
                            sortTypePokemon = "by alphabet"
                            downloadPokemonList()
                        }

                        "by id" -> {
                            sortTypePokemon = "by id"
                            downloadPokemonList()
                        }
                    }
                }
            } else {
                setItems(items) { dialog, which ->
                    when (items[which]) {
                        "default" -> {
                            when (currentCategory) {

                                "item" -> {
                                    sortTypeItem = "none"
                                    downloadPokemonList()
                                }

                                "move" -> {
                                    sortTypeMove = "none"
                                    downloadPokemonList()
                                }

                                "berry" -> {
                                    sortTypeBerry = "none"
                                    downloadPokemonList()
                                }
                            }
                        }

                        "by alphabet" -> {
                            when (currentCategory) {

                                "item" -> {
                                    sortTypeItem = "by alphabet"
                                    downloadPokemonList()
                                }

                                "move" -> {
                                    sortTypeMove = "by alphabet"
                                    downloadPokemonList()
                                }

                                "berry" -> {
                                    sortTypeBerry = "by alphabet"
                                    downloadPokemonList()
                                }
                            }
                        }
                    }
                }
            }

            setCustomTitle(customTitle)
            show()
        }

    }

    private fun alertDialogSortLists() {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))

        val customTitle = layoutInflater.inflate(R.layout.alert_dialog_sort_by_title, null)

        customTitle.findViewById<TextView>(R.id.sort_by_title).text = "sort lists by"

        val items = arrayOf("default", "by alphabet")

        builder.setItems(items) { dialog, which ->
            when (items[which]) {
                "default" -> {
                    sortTypeLists = "none"
                    downloadPokemonList()
                }

                "by alphabet" -> {
                    sortTypeLists = "by alphabet"
                    downloadPokemonList()
                }
            }
        }
        builder.setCustomTitle(customTitle)
        builder.show()
    }

    private fun alertDialogCreateNewList() {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()


        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_layout_create_new_list, null)
        val editTextName = dialogLayout.findViewById<EditText>(R.id.editTextName)
        val editTextDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
        val buttonCreate = dialogLayout.findViewById<TextView>(R.id.create)
        val buttonCancel = dialogLayout.findViewById<TextView>(R.id.close)

        buttonCancel.setOnClickListener {
            builder.dismiss()
        }

        buttonCreate.setOnClickListener {
            if (editTextName.text.isEmpty()) {
                editTextName.setHintTextColor(resources.getColor(R.color.pokemon_red))
                editTextName.hint = "enter list name"
            } else {
                viewModel.viewModelScope.launch {

                    val allLists = withContext(Dispatchers.IO) {
                        viewModel.getAllLists()
                    }


                    var listWithSameNameExists = false

                    for (value in allLists) {
                        if (value.name == editTextName.text.toString()) {
                            listWithSameNameExists = true
                            break
                        }
                    }

                    if (!listWithSameNameExists) {

                        withContext(Dispatchers.IO) {
                            viewModel.addListToDb(
                                dbId = 0,
                                listName = editTextName.text.toString(),
                                listDescription = editTextDescription.text.toString()
                            )
                        }

                        downloadPokemonList()
                        builder.dismiss()

                    } else {

                        Toast.makeText(
                            this@SavedPokemonsFragment.context,
                            "list with such name already exists",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }

        }

        builder.setView(dialogLayout)
        builder.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        savedPokemonsAdapter = null
        savedBerriesAdapter = null
        savedItemsAdapter = null
        savedMovesAdapter = null
        customListsAdapter = null

        _binding = null
    }
}
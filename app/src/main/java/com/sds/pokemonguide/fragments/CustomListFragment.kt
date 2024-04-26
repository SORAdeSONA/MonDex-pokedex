package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.CustomListAdapter
import com.sds.pokemonguide.databinding.CustomListLayoutBinding
import com.sds.pokemonguide.room.RoomSavedListsPokemonsModel
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CustomListFragment : Fragment() {

    private val viewModel: PokemonViewModel by viewModels()

    private var customListAdapter: CustomListAdapter? = null

    private var _binding: CustomListLayoutBinding? = null
    private val binding get() = _binding!!

    private val args: CustomListFragmentArgs by navArgs()

    private var isFirstChange = true

    private var listName = ""
    private var listDescription = ""

    private var sortTypePokemon = "none"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomListLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        getPokemonsFromList()
        setViews()
        setButtons()
    }

    private fun setViews() {
        binding.listName.text = args.listName
        if (args.listDescription.isNotEmpty()) {
            binding.listDescription.text = args.listDescription
        } else binding.listDescription.text = "no description"
    }

    private fun getPokemonsFromList() {

        viewModel.viewModelScope.launch {


            val allPokemonsFromCustomLists = withContext(Dispatchers.IO) {
                viewModel.getAllSavedListsPokemons()
            }

            val listOfPokemons = mutableListOf<RoomSavedListsPokemonsModel>()

            for (value in allPokemonsFromCustomLists) {
                if (value.listName == args.listName) {
                    listOfPokemons.add(value)
                }
            }

            when (sortTypePokemon) {
                "none" -> {
                    if (listOfPokemons.isNotEmpty()) {
                        customListAdapter?.addPokemons(listOfPokemons)
                        binding.empty.visibility = View.GONE
                    } else {
                        binding.empty.visibility = View.VISIBLE
                    }
                }

                "by alphabet" -> {
                    if (listOfPokemons.isNotEmpty()) {
                        customListAdapter?.addPokemons(listOfPokemons.sortedBy { it.pokemonName })
                        binding.empty.visibility = View.GONE
                    } else {
                        binding.empty.visibility = View.VISIBLE
                    }
                }

                "by id" -> {
                    if (listOfPokemons.isNotEmpty()) {
                        customListAdapter?.addPokemons(listOfPokemons.sortedBy {
                            it.pokemonUrl.removePrefix(
                                "https://pokeapi.co/api/v2/pokemon/"
                            ).dropLast(1).toInt()
                        })

                        binding.empty.visibility = View.GONE
                    } else {
                        binding.empty.visibility = View.VISIBLE
                    }
                }

            }

        }
    }

    private fun setAdapter() {
        customListAdapter = CustomListAdapter()
        binding.listItemsRv.adapter = customListAdapter
        binding.listItemsRv.layoutManager = GridLayoutManager(this.context, 2)

        customListAdapter!!.setOnItemClickListenerUrl {
            findNavController().navigate(
                CustomListFragmentDirections.actionCustomListFragmentToDetailPokedexFragment(
                    it, false
                )
            )
        }

    }

    private fun setButtons() {

        binding.filter.setOnClickListener {
            alertDialogSortPokemons()
        }

        binding.editListName.setOnClickListener {
            alertDialogChangeListName()
        }

        binding.deleteCategory.setOnClickListener {
            val listNameForAlertDialog = if (isFirstChange) args.listName else listName
            alertDialogDeleteListAndPokemons(listNameForAlertDialog)
        }


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun alertDialogSortPokemons() {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))


        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_sort_by_title, null)

        val customTitle = dialogLayout.findViewById<TextView>(R.id.sort_by_title)

        val itemsPokemon = arrayOf("default", "by alphabet", "by id")

        builder.setItems(itemsPokemon) { dialog, which ->
            when (itemsPokemon[which]) {

                "default" -> {
                    sortTypePokemon = "none"
                    getPokemonsFromList()
                }

                "by alphabet" -> {
                    sortTypePokemon = "by alphabet"
                    getPokemonsFromList()
                }

                "by id" -> {
                    sortTypePokemon = "by id"
                    getPokemonsFromList()
                }

            }
        }
        builder.setCustomTitle(dialogLayout)
        builder.show()
    }

    private fun alertDialogChangeListName() {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()


        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_layout_create_new_list, null)
        val editTextName = dialogLayout.findViewById<EditText>(R.id.editTextName)
        val editTextDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
        val buttonApply = dialogLayout.findViewById<TextView>(R.id.create)
        val buttonClose = dialogLayout.findViewById<TextView>(R.id.close)
        val title = dialogLayout.findViewById<TextView>(R.id.search_title)


        title.text = "edit list"
        buttonApply.text = "apply"



        if (isFirstChange) {
            editTextName.setText(args.listName)
            editTextDescription.setText(args.listDescription)
        } else {
            editTextDescription.setText(listDescription)
            args.listDescription + listDescription
        }

        buttonClose.setOnClickListener {
            builder.dismiss()
        }


        buttonApply.setOnClickListener {

            if (editTextName.text.isEmpty()) {
                editTextName.setHintTextColor(resources.getColor(R.color.pokemon_red))
                editTextName.hint = "enter new list name"
            } else {

                viewLifecycleOwner.lifecycleScope.launch {

                    if (isFirstChange) {

                        withContext(Dispatchers.IO) {
                            viewModel.updateListNameAndDescription(
                                newCategoryDescription = editTextDescription.text.toString(),
                                newCategoryName = editTextName.text.toString(),
                                oldCategoryDescription = args.listDescription,
                                oldCategoryName = args.listName
                            )

                            viewModel.updateListNameForPokemons(
                                args.listName,
                                editTextName.text.toString()
                            )
                        }

                        listName = editTextName.text.toString()
                        listDescription = editTextDescription.text.toString()
                        binding.listName.text = listName
                        binding.listDescription.text = listDescription
                        isFirstChange = false
                        builder.dismiss()

                    } else {

                        withContext(Dispatchers.IO) {
                            viewModel.updateListNameAndDescription(
                                newCategoryDescription = editTextDescription.text.toString(),
                                newCategoryName = editTextName.text.toString(),
                                oldCategoryDescription = listDescription,
                                oldCategoryName = listName
                            )

                            viewModel.updateListNameForPokemons(
                                listName,
                                editTextName.text.toString()
                            )
                        }

                        listName = editTextName.text.toString()
                        listDescription = editTextDescription.text.toString()
                        binding.listName.text = listName
                        binding.listDescription.text = listDescription
                        builder.dismiss()

                    }


                }
            }


        }

        builder.setView(dialogLayout)
        builder.show()

    }

    private fun alertDialogDeleteListAndPokemons(listNameDisplay: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_delete_list, null)
        val areYouSure = dialogLayout.findViewById<TextView>(R.id.are_you_sure)
        val cancel = dialogLayout.findViewById<TextView>(R.id.cancel)
        val delete = dialogLayout.findViewById<TextView>(R.id.delete)

        areYouSure.text =
            "are you sure you want to delete the list $listNameDisplay? this action will also delete all pokemon within the list"

        cancel.setOnClickListener {
            builder.dismiss()
        }

        delete.setOnClickListener {

            viewModel.viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (isFirstChange) {
                        viewModel.deleteListFromDb(args.listName)
                        viewModel.deletePokemonWhenDeleteList(args.listName)
                    } else {
                        viewModel.deleteListFromDb(listName)
                        viewModel.deletePokemonWhenDeleteList(listName)
                    }
                }
                builder.dismiss()
                findNavController().popBackStack()
            }

        }

        builder.setView(dialogLayout)
        builder.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        customListAdapter = null

        _binding = null
    }

}
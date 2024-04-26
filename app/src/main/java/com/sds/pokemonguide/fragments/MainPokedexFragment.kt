package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.LoadMoreAdapter
import com.sds.pokemonguide.adapter.MainBerrieAdapter
import com.sds.pokemonguide.adapter.MainItemAdapter
import com.sds.pokemonguide.adapter.MainMoveAdapter
import com.sds.pokemonguide.adapter.PokemonAdapter
import com.sds.pokemonguide.databinding.MainPokedexFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.status.SearchStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainPokedexFragment : Fragment() {

    private val viewModel: PokemonViewModel by viewModels()

    private var pokemonAdapter: PokemonAdapter? = null
    private var itemAdapter: MainItemAdapter? = null
    private var moveAdapter: MainMoveAdapter? = null
    private var berrieAdapter: MainBerrieAdapter? = null

    private var alertDialogSearch: AlertDialog? = null


    private var _binding: MainPokedexFragmentBinding? = null
    private val binding get() = _binding!!

    private var searchValue = ""


    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        alertDialogError(
            "downloading error",
            "check your connection and try again.\nerror message: $exception"
        )
    }


    private var currentCategory = "main"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainPokedexFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setEnums()
        downloadPokemonList()
        setButtons()
    }


    private fun setEnums() {

        viewModel.searchType.value = SearchStatus.NOT_FOUND

        viewModel.searchType.observe(viewLifecycleOwner) {

            when (it) {
                SearchStatus.POKEMON -> {
                    alertDialogSearch?.dismiss()
                    findNavController().navigate(
                        MainPokedexFragmentDirections.actionMainPokedexFragmentToDetailPokedexFragment(
                            searchValue, true
                        )
                    )
                }

                SearchStatus.ITEM -> {
                    alertDialogSearch?.dismiss()
                    findNavController().navigate(
                        MainPokedexFragmentDirections.actionMainPokedexFragmentToItemPokedexFragment(
                            searchValue, true
                        )
                    )
                }

                SearchStatus.ABILITY -> {
                    alertDialogSearch?.dismiss()
                    findNavController().navigate(
                        MainPokedexFragmentDirections.actionMainPokedexFragmentToAbilityPokedexFragment(
                            searchValue, true
                        )
                    )
                }

                SearchStatus.MOVE -> {
                    alertDialogSearch?.dismiss()
                    findNavController().navigate(
                        MainPokedexFragmentDirections.actionMainPokedexFragmentToMovePokedexFragment(
                            searchValue, true
                        )
                    )
                }

                SearchStatus.NOT_FOUND -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.pokemonRv.visibility = View.VISIBLE

                }

                else -> println("livedata is null")
            }

        }

        viewModel.loadingStatusMain.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                }

                LoadingStatus.ERROR -> {
                    println("ERROR WHILE DOWNLOADING")
                }

                else -> {}
            }
        }

    }

    private fun setButtons() {

        binding.search.setOnClickListener {
            alertDialogSearch()
        }

        binding.idButtonToCategoryFragment.setOnClickListener {
            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainPokedexFragmentToCategoryPokedexFragment()
            )
        }

        binding.errorContainer.setOnClickListener {
            downloadPokemonList()
        }

        binding.savedPokemons.setOnClickListener {
            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainFragmentToSavedPokemonsFragment()
            )
        }

        binding.mainCategoryPokemon.setOnClickListener {
            if (currentCategory == "main") {
                val currentPosition =
                    (binding.pokemonRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (currentPosition > 50) {
                    binding.pokemonRv.scrollToPosition(0)
                } else {
                    binding.pokemonRv.smoothScrollToPosition(0)
                }
            } else {
                currentCategory = "main"
                downloadPokemonList()
            }

        }

        binding.mainCategoryItem.setOnClickListener {
            if (currentCategory == "item") {
                val currentPosition =
                    (binding.itemRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (currentPosition > 50) {
                    binding.itemRv.scrollToPosition(0)
                } else {
                    binding.itemRv.smoothScrollToPosition(0)
                }
            } else {
                currentCategory = "item"
                downloadPokemonList()
            }
        }


        binding.mainCategoryMove.setOnClickListener {
            if (currentCategory == "move") {
                val currentPosition =
                    (binding.moveRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (currentPosition > 50) {
                    binding.moveRv.scrollToPosition(0)
                } else {
                    binding.moveRv.smoothScrollToPosition(0)
                }
            } else {
                currentCategory = "move"
                downloadPokemonList()
            }
        }

        binding.mainCategoryBerrie.setOnClickListener {
            if (currentCategory == "berry") {
                val currentPosition =
                    (binding.berrieRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (currentPosition > 50) {
                    binding.berrieRv.scrollToPosition(0)
                } else {
                    binding.berrieRv.smoothScrollToPosition(0)
                }
            } else {
                currentCategory = "berry"
                downloadPokemonList()
            }
        }

        binding.idSettingsButton.setOnClickListener {
            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainFragmentToSettingsFragment()
            )
        }

    }

    private fun downloadPokemonList() {

        viewLifecycleOwner.lifecycleScope.launch {
            when (currentCategory) {

                "main" -> {
                    binding.mainCategoryPokemon.setBackgroundResource(R.drawable.left_corner_category)
                    binding.mainCategoryItem.background = null
                    binding.mainCategoryMove.background = null
                    binding.mainCategoryBerrie.background = null
                    binding.itemRv.visibility = View.GONE
                    binding.moveRv.visibility = View.GONE
                    binding.berrieRv.visibility = View.GONE
                    binding.pokemonRv.visibility = View.VISIBLE

                    viewModel.pokemonList.collect {
                        pokemonAdapter?.submitData(it)
                        pokemonAdapter?.retry()
                    }
                }

                "item" -> {
                    binding.mainCategoryItem.setBackgroundResource(R.drawable.center_corner_category)
                    binding.mainCategoryPokemon.background = null
                    binding.mainCategoryMove.background = null
                    binding.mainCategoryBerrie.background = null
                    binding.pokemonRv.visibility = View.GONE
                    binding.moveRv.visibility = View.GONE
                    binding.berrieRv.visibility = View.GONE
                    binding.itemRv.visibility = View.VISIBLE

                    viewModel.itemList.collect {
                        itemAdapter?.submitData(it)
                        itemAdapter?.retry()
                    }

                }

                "move" -> {
                    binding.mainCategoryMove.setBackgroundResource(R.drawable.right_corner_category)
                    binding.mainCategoryPokemon.background = null
                    binding.mainCategoryItem.background = null
                    binding.mainCategoryBerrie.background = null
                    binding.itemRv.visibility = View.GONE
                    binding.pokemonRv.visibility = View.GONE
                    binding.berrieRv.visibility = View.GONE
                    binding.moveRv.visibility = View.VISIBLE

                    viewModel.moveList.collect {
                        moveAdapter?.submitData(it)
                        moveAdapter?.retry()
                    }
                }

                "berry" -> {
                    binding.mainCategoryBerrie.setBackgroundResource(R.drawable.center_corner_category)
                    binding.mainCategoryPokemon.background = null
                    binding.mainCategoryItem.background = null
                    binding.mainCategoryMove.background = null
                    binding.itemRv.visibility = View.GONE
                    binding.pokemonRv.visibility = View.GONE
                    binding.moveRv.visibility = View.GONE
                    binding.berrieRv.visibility = View.VISIBLE

                    viewModel.berrieList.collect {
                        berrieAdapter?.submitData(it)
                        berrieAdapter?.retry()
                    }
                }

            }
        }
    }

    private fun setAdapter() {

        pokemonAdapter = PokemonAdapter()

        val footerAdapterPokemon = LoadMoreAdapter(viewModel) { pokemonAdapter!!.retry() }

        val layoutManagerPokemon = GridLayoutManager(this.context, 2)

        binding.pokemonRv.layoutManager = layoutManagerPokemon
        binding.pokemonRv.adapter = pokemonAdapter!!.withLoadStateFooter(footerAdapterPokemon)

        layoutManagerPokemon.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == pokemonAdapter!!.itemCount && footerAdapterPokemon.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        pokemonAdapter!!.setOnItemClickListenerUrl { pokemonDataForDetailFragment ->

            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainPokedexFragmentToDetailPokedexFragment(
                    pokemonDataForDetailFragment.image, false
                )
            )
        }


        itemAdapter = MainItemAdapter()

        val footerAdapterItem = LoadMoreAdapter(viewModel) { itemAdapter!!.retry() }

        val layoutManagerItem = GridLayoutManager(this.context, 2)

        binding.itemRv.layoutManager = layoutManagerItem
        binding.itemRv.adapter = itemAdapter!!.withLoadStateFooter(footerAdapterItem)

        layoutManagerItem.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == itemAdapter!!.itemCount && footerAdapterItem.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }


        itemAdapter!!.setOnItemClickListenerUrlMainItem {
            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainPokedexFragmentToItemPokedexFragment(
                    it, false
                )
            )
        }

        moveAdapter = MainMoveAdapter()

        val footerAdapterMove = LoadMoreAdapter(viewModel) { moveAdapter!!.retry() }

        val layoutManagerMove = GridLayoutManager(this.context, 2)

        binding.moveRv.layoutManager = layoutManagerMove
        binding.moveRv.adapter = moveAdapter!!.withLoadStateFooter(footerAdapterMove)

        layoutManagerMove.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == moveAdapter!!.itemCount && footerAdapterMove.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        moveAdapter!!.setOnItemClickListenerUrlMainMove {
            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainPokedexFragmentToMovePokedexFragment(
                    it, false
                )
            )
        }


        berrieAdapter = MainBerrieAdapter()

        val footerAdapterBerrie = LoadMoreAdapter(viewModel) { berrieAdapter!!.retry() }

        val layoutManagerBerrie = GridLayoutManager(this.context, 2)

        binding.berrieRv.layoutManager = layoutManagerBerrie
        binding.berrieRv.adapter = berrieAdapter!!.withLoadStateFooter(footerAdapterBerrie)

        layoutManagerBerrie.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == berrieAdapter!!.itemCount && footerAdapterBerrie.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        berrieAdapter!!.setOnItemClickListenerUrlMainBerrie {
            findNavController().navigate(
                MainPokedexFragmentDirections.actionMainPokedexFragmentToBerriePokedexFragment(
                    it
                )
            )
        }


    }

    private fun alertDialogSearch() {

        alertDialogSearch?.dismiss()

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val customLayout = layoutInflater.inflate(R.layout.alert_dialog_search, null)


        val searchEditText = customLayout.findViewById<EditText>(R.id.editTextSearch)
        val startSearch = customLayout.findViewById<TextView>(R.id.start_search)
        val close = customLayout.findViewById<TextView>(R.id.close_search)
        val progressBar = customLayout.findViewById<ProgressBar>(R.id.progress_bar_loading2)

        close.setOnClickListener {
            builder.dismiss()
        }

       startSearch.setOnClickListener{
            if (searchEditText.text.isNotEmpty()){
                searchValue = searchEditText.text.toString()

                progressBar.visibility = View.VISIBLE

                viewModel.viewModelScope.launch(coroutineExceptionHandler) {
                    viewModel.apply {
                        val responses = listOf(
                            async { searchPokemon(searchValue) },
                            async { searchItem(searchValue) },
                            async { searchMove(searchValue) },
                            async { searchAbility(searchValue) },
                        )
                        responses.awaitAll()

                        if (viewModel.searchType.value == SearchStatus.NOT_FOUND) {
                            progressBar.visibility = View.GONE

                            alertDialogError(
                                "search error",
                                "nothing found by keyword $searchValue"
                            )
                        }else{
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }else{
                Toast.makeText(this.context, "enter search keyword", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialogSearch = builder

        with(builder) {
            setView(customLayout)
            show()
        }

    }


    private fun alertDialogError(title: String, description: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val customLayout = layoutInflater.inflate(R.layout.alert_dialog_main_error, null)


        customLayout.findViewById<TextView>(R.id.close_main_error).setOnClickListener {
            builder.dismiss()
        }

        with(builder) {
            setTitle(title)
            setMessage(description)
            setView(customLayout)
            setCancelable(false)
            show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        pokemonAdapter = null
        itemAdapter = null
        moveAdapter = null
        berrieAdapter = null

        _binding = null
    }

}
package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.ItemAttributesAdapter
import com.sds.pokemonguide.adapter.ItemHeldByAdapter
import com.sds.pokemonguide.databinding.ItemDetailFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ItemsDetailFragment : Fragment() {


    private val viewModel: PokemonViewModel by viewModels()

    private val args: ItemsDetailFragmentArgs by navArgs()

    private var attributesAdapter: ItemAttributesAdapter? = null
    private var heldByAdapter: ItemHeldByAdapter? = null

    private var _binding: ItemDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusItem.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemDetailFragmentBinding.inflate(inflater, container, false)
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
        viewModel.loadingStatusItem.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.itemItemCategory.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idMoveDetailContainer.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.itemItemCategory.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveDetailContainer.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.itemItemCategory.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idMoveDetailContainer.visibility = View.GONE

                }
            }
        }
    }

    private fun setUI() {

        viewModel.itemDetailResponse.observe(viewLifecycleOwner) { response ->

            viewModel.viewModelScope.launch {
                withContext(Dispatchers.IO){
                    val list = viewModel.getAllItemsFromDb()
                    if (list.isEmpty()){
                        withContext(Dispatchers.Main){
                            binding.savedNo.visibility = View.VISIBLE
                        }
                    }else{
                        for (value in list) {
                            if (value.itemName == response.name){
                                withContext(Dispatchers.Main){
                                    binding.savedYes.visibility = View.VISIBLE
                                }
                            }else {
                                withContext(Dispatchers.Main){
                                    binding.savedNo.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                }
            }


            binding.savedNo.setOnClickListener {
                viewModel.viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        viewModel.addItemToDb(
                            dbId = 0,
                            itemName = response.name,
                            itemUrl = args.itemId
                        )
                        withContext(Dispatchers.Main){
                            binding.savedNo.visibility = View.GONE
                            binding.savedYes.visibility = View.VISIBLE
                            alertDialog(response.name, "added to")
                        }
                    }
                }

            }

            binding.savedYes.setOnClickListener {
                viewModel.viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        viewModel.deleteItemFromDb(response.name)
                        withContext(Dispatchers.Main){
                            binding.savedNo.visibility = View.VISIBLE
                            binding.savedYes.visibility = View.GONE
                            alertDialog(response.name, "deleted from")
                        }
                    }
                }

            }


            binding.idPokemonItemName.text = response.name.replace("-", " ")

            Glide.with(binding.idItemImageDetail)
                .load(response.sprite.defaultSprite)
                .into(binding.idItemImageDetail)

            try {
                binding.idItemCategoryValue.text = response.category.name.replace("type-", "").replace("-", " ")
            } catch (e: Exception) {
                binding.idItemCategoryValue.text = "-"
            }

            try {
                for (value in response.effectEntries){
                    println(value.language.languageName)
                    if(value.language.languageName == "en")  binding.idItemEffectValue.text = value.shortEffect
                }
            } catch (e: Exception) {
                binding.idItemEffectValue.text = "-"
            }

            try {
                binding.idItemCostValue.text = response.cost.toString()
            } catch (e: Exception) {
                binding.idItemCostValue.text = "-"
            }

            try {
                binding.idItemFlingPowerValue.text = response.fling_power.toString()
            } catch (e: Exception) {
                binding.idItemFlingPowerValue.text = "-"
            }

            try {
                for (value in response.flavor_text_entries){
                    println(value.language.languageName)
                    if(value.language.languageName == "en")  binding.flavorTextItem.text = value.text
                }
            } catch (e: Exception) {
                binding.flavorTextItem.text = "no description found"
            }

            if (response.heldByPokemon.isEmpty()){
                binding.idItemHeldBy.text = "no pokemons found"
            }else {
                heldByAdapter?.addHeldByPokemons(response.heldByPokemon)
            }

            if (response.attributes.isEmpty()) {
                binding.idPokemonItemAttributes.text = "no attributes found"
            } else {
                attributesAdapter?.addAttributes(response.attributes)
            }


            binding.idItemCategoryValue.setOnClickListener {
                findNavController().navigate(
                    ItemsDetailFragmentDirections.actionItemPokedexFragmentToItemCategoryPokedexFragment(
                        response.category.url
                    )
                )
            }
        }

    }

    private fun downloadData(){
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            if (args.isSearch){
                viewModel.downloadItemDetailsById(args.itemId)
            }else {
                val id = args.itemId.removePrefix("https://pokeapi.co/api/v2/item/").dropLast(1)
                viewModel.downloadItemDetailsById(id)
            }
        }
    }

    private fun setAdapter() {
        attributesAdapter = ItemAttributesAdapter()
        binding.idItemRvAttributes.adapter = attributesAdapter
        binding.idItemRvAttributes.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        heldByAdapter = ItemHeldByAdapter()
        binding.idItemRvHeldBy.adapter = heldByAdapter
        binding.idItemRvHeldBy.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)


        attributesAdapter!!.setOnItemClickListenerUrlAttribute {
            findNavController().navigate(
                ItemsDetailFragmentDirections.actionItemPokedexFragmentToAttributePokedexFragment(
                    it
                )
            )
        }

        heldByAdapter!!.setOnItemClickListenerUrlHeldBy {
            findNavController().navigate(
                ItemsDetailFragmentDirections.actionItemPokedexFragmentToDetailPokedexFragment(
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

        attributesAdapter = null
        heldByAdapter = null

        _binding = null
    }
}
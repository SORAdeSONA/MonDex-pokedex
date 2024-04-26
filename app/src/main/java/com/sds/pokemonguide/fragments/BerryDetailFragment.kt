package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.BerryFirmnessAdapter
import com.sds.pokemonguide.adapter.BerryFlavorAdapter
import com.sds.pokemonguide.adapter.BerryFlavorDetailAdapter
import com.sds.pokemonguide.databinding.BerryDetailFragmentBinding
import com.sds.pokemonguide.databinding.BerryFirmnessFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import com.sds.pokemonguide.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BerryDetailFragment : Fragment() {
    private val viewModel: PokemonViewModel by viewModels()

    private val args: BerryDetailFragmentArgs by navArgs()

    private var berryFlavorAdapter: BerryFlavorDetailAdapter? = null

    private var _binding: BerryDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusBerryDetail.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BerryDetailFragmentBinding.inflate(inflater, container, false)
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

        viewModel.loadingStatusBerryDetail.observe(viewLifecycleOwner) {
            when (it) {
                LoadingStatus.LOADING -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.idBerryDetailContainer.visibility = View.GONE
                    binding.idBerryCategory.visibility = View.GONE
                    binding.idBerryName.visibility = View.GONE


                }

                LoadingStatus.SUCCESS -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idBerryDetailContainer.visibility = View.VISIBLE
                    binding.idBerryCategory.visibility = View.VISIBLE
                    binding.idBerryName.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.idBerryDetailContainer.visibility = View.GONE
                    binding.idBerryCategory.visibility = View.GONE
                    binding.idBerryName.visibility = View.GONE
                }

            }
        }


    }

    private fun downloadData() {
        viewModel.viewModelScope.launch(coroutineExceptionHandler) {
            val id = args.berryId.removePrefix("https://pokeapi.co/api/v2/berry/").dropLast(1)
            viewModel.downloadBerryDetailsById(id)
        }
    }

    private fun setUI() {
        viewModel.berryDetailResponse.observe(viewLifecycleOwner) { response ->


            viewModel.viewModelScope.launch {

                val list = withContext(Dispatchers.IO) {
                    viewModel.getAllBerriesFromDb()
                }

                if (list.isEmpty()) {
                    binding.savedNo.visibility = View.VISIBLE
                } else {
                    for (value in list) {
                        if (value.berryName == response.name) {
                            println("saved yes")
                            binding.savedYes.visibility = View.VISIBLE
                        } else {
                            println("saved no")
                            binding.savedNo.visibility = View.VISIBLE
                        }
                    }
                }



                binding.savedNo.setOnClickListener {
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.addPokemonToDb(
                                pokemonName = response.name,
                                pokemonUrl = args.berryId
                            )
                        }
                    }
                    binding.savedNo.visibility = View.GONE
                    binding.savedYes.visibility = View.VISIBLE
                    alertDialog(response.name, "added to")
                }

                binding.savedYes.setOnClickListener {
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.deleteBerryFromDb(response.name)
                            withContext(Dispatchers.Main) {
                                binding.savedNo.visibility = View.VISIBLE
                                binding.savedYes.visibility = View.GONE
                                alertDialog(response.name, "deleted from")
                            }
                        }
                    }
                }

            }


            Glide.with(binding.idBerryImage.context)
                .load(CONSTANTS.getBerrySpritesByName(response.name))
                .into(binding.idBerryImage)

            binding.idBerryName.text = response.name

            binding.idFirmnessValue.text =
                response.firmness.name.replace("-", " ")
            binding.idFirmnessValue.setOnClickListener {
                findNavController().navigate(
                    BerryDetailFragmentDirections.actionBerryDetailPokedexFragmentToBerryFirmnessPokedexFragment(
                        response.firmness.url
                    )
                )
            }

            binding.idItemValue.text = response.item.name.replace("-", " ")
            binding.idItemValue.setOnClickListener {
                findNavController().navigate(
                    BerryDetailFragmentDirections.actionBerryDetailPokedexFragmentToItemDetailPokedexFragment(
                        response.item.url, false
                    )
                )
            }

            binding.idGrowthTimeValue.text = response.growthTime.toString()
            binding.idMaxHarvestValue.text = response.maxHarvest.toString()


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
                binding.idNaturalGiftTypeValue.text = response.naturalGiftType.name
                binding.giftTypeImageValue.setImageResource(getImageByType(response.naturalGiftType.name))
            }catch (e:Exception){
                binding.idNaturalGiftTypeValue.text = "-"
                binding.giftTypeImageValue.visibility = View.GONE
            }
            binding.idNaturalGiftPowerValue.text = response.naturalGiftPower.toString()


            binding.idSizeValue.text = response.size.toString()
            binding.idSmoothnessValue.text = response.smoothness.toString()
            binding.idSoilDrynessValue.text = response.soilDryness.toString()

            berryFlavorAdapter?.addFlavors(response.flavors)
        }
    }

    private fun setAdapter() {
        berryFlavorAdapter = BerryFlavorDetailAdapter()

        binding.idBerryFlavorsRv.adapter = berryFlavorAdapter
        binding.idBerryFlavorsRv.layoutManager =
            GridLayoutManager(this.context, 2)

        berryFlavorAdapter!!.setOnItemClickListenerUrlBerryFlavor {
            findNavController().navigate(
                BerryDetailFragmentDirections.actionBerryDetailPokedexFragmentToBerryFlavorPokedexFragment(
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
        berryFlavorAdapter = null
        _binding = null
    }
}
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.sds.pokemonguide.CONSTANTS
import com.sds.pokemonguide.R
import com.sds.pokemonguide.adapter.CommonCategoryAdapter
import com.sds.pokemonguide.databinding.CategoryPokedexFragmentBinding
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryPokedexFragment : Fragment() {

    private val viewModel: PokemonViewModel by viewModels()

    private var typesAdapter: CommonCategoryAdapter? = null
    private var eggGroupAdapter: CommonCategoryAdapter? = null
    private var gendersAdapter: CommonCategoryAdapter? = null
    private var habitatsAdapter: CommonCategoryAdapter? = null
    private var shapesAdapter: CommonCategoryAdapter? = null

    private var attributeAdapter: CommonCategoryAdapter? = null
    private var categoryAdapter: CommonCategoryAdapter? = null
    private var flingEffectAdapter: CommonCategoryAdapter? = null
    private var pocketAdapter: CommonCategoryAdapter? = null

    private var berryFirmnessAdapter: CommonCategoryAdapter? = null
    private var berryFlavorAdapter: CommonCategoryAdapter? = null

    private var moveAilmentAdapter: CommonCategoryAdapter? = null
    private var moveCategoryAdapter: CommonCategoryAdapter? = null
    private var moveDamageClassAdapter: CommonCategoryAdapter? = null
    private var moveTargetAdapter: CommonCategoryAdapter? = null


    private var currentCategory = "pokemon"
    private var categoryDownloadBoolean = false

    private var _binding: CategoryPokedexFragmentBinding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { handler, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
        viewModel.loadingStatusCategory.value = LoadingStatus.ERROR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryPokedexFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDataViews()
        downloadData()
        setUI()
        setAdapters()
        setButtons()
        loadBanner()

    }


    private fun setDataViews() {

        binding.errorContainer.setOnClickListener {
            downloadData()
        }

        viewModel.loadingStatusCategory.observe(viewLifecycleOwner) {
            when (it) {
                LoadingStatus.LOADING -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.containerCategories.visibility = View.GONE
                    binding.containerMove.visibility = View.GONE
                    binding.containerItem.visibility = View.GONE
                    binding.containerBerrie.visibility = View.GONE
                    binding.containerPokemon.visibility = View.GONE
                }

                LoadingStatus.SUCCESS -> {
                    binding.errorContainer.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.containerCategories.visibility = View.VISIBLE
                    when (currentCategory) {
                        "pokemon" -> binding.containerPokemon.visibility = View.VISIBLE
                        "item" -> binding.containerItem.visibility = View.VISIBLE
                        "berry" -> binding.containerBerrie.visibility = View.VISIBLE
                        "move" -> binding.containerMove.visibility = View.VISIBLE
                    }
                }

                LoadingStatus.ERROR -> {
                    binding.errorContainer.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    binding.containerCategories.visibility = View.GONE
                    binding.containerMove.visibility = View.GONE
                    binding.containerItem.visibility = View.GONE
                    binding.containerBerrie.visibility = View.GONE
                    binding.containerPokemon.visibility = View.GONE
                }

            }
        }
    }

    private fun downloadData() {
        when (currentCategory) {
            "pokemon" -> {
                binding.categoryPokemon.setBackgroundResource(R.drawable.left_corner_category)
                binding.categoryItem.background = null
                binding.categoryBerries.background = null
                binding.categoryMoves.background = null
                binding.containerPokemon.visibility = View.VISIBLE
                binding.containerItem.visibility = View.GONE
                binding.containerBerrie.visibility = View.GONE
                binding.containerMove.visibility = View.GONE
            }

            "item" -> {
                binding.categoryItem.setBackgroundResource(R.drawable.center_corner_category)
                binding.categoryPokemon.background = null
                binding.categoryBerries.background = null
                binding.categoryMoves.background = null
                binding.containerPokemon.visibility = View.GONE
                binding.containerBerrie.visibility = View.GONE
                binding.containerMove.visibility = View.GONE
                binding.containerItem.visibility = View.VISIBLE
            }

            "berry" -> {
                binding.categoryBerries.setBackgroundResource(R.drawable.center_corner_category)
                binding.categoryItem.background = null
                binding.categoryPokemon.background = null
                binding.categoryMoves.background = null
                binding.containerBerrie.visibility = View.VISIBLE
                binding.containerPokemon.visibility = View.GONE
                binding.containerItem.visibility = View.GONE
                binding.containerMove.visibility = View.GONE
            }

            "move" -> {
                binding.categoryMoves.setBackgroundResource(R.drawable.right_corner_category)
                binding.categoryItem.background = null
                binding.categoryPokemon.background = null
                binding.categoryBerries.background = null
                binding.containerMove.visibility = View.VISIBLE
                binding.containerPokemon.visibility = View.GONE
                binding.containerItem.visibility = View.GONE
                binding.containerBerrie.visibility = View.GONE
            }
        }

        if (categoryDownloadBoolean == false) {

            viewModel.viewModelScope.launch(coroutineExceptionHandler) {
                viewModel.loadingStatusCategory.value = LoadingStatus.LOADING
                viewModel.apply {
                    val responses = listOf(
                        async { downloadTypesList() },
                        async { downloadEggGroupList() },
                        async { downloadGendersList() },
                        async { downloadHabitatsList() },
                        async { downloadShapesList() },
                        async { downloadItemAttributeList() },
                        async { downloadItemCategoryList() },
                        async { downloadItemFlingEffectList() },
                        async { downloadItemPocketList() },
                        async { downloadBerryFirmnessList() },
                        async { downloadBerryFlavorList() },
                        async { downloadMoveAilmentList() },
                        async { downloadMoveBattleStyleList() },
                        async { downloadMoveCategoryList() },
                        async { downloadMoveDamageClassList() },
                        async { downloadMoveTargetList() }
                    )
                    responses.awaitAll()
                    if (responses[0].isCompleted && responses[15].isCompleted) {
                        viewModel.loadingStatusCategory.value = LoadingStatus.SUCCESS
                    } else viewModel.loadingStatusCategory.value = LoadingStatus.ERROR
                    categoryDownloadBoolean = true
                }


            }
        }
    }

    private fun setUI() {
        viewModel.typesListDetailResponse.observe(viewLifecycleOwner) {
            typesAdapter?.addTypes(it.results)
        }
        viewModel.eggGroupListDetailResponse.observe(viewLifecycleOwner) {
            eggGroupAdapter?.addTypes(it.results)
        }
        viewModel.gendersListDetailResponse.observe(viewLifecycleOwner) {
            gendersAdapter?.addTypes(it.results)
        }
        viewModel.habitatsListDetailResponse.observe(viewLifecycleOwner) {
            habitatsAdapter?.addTypes(it.results)
        }
        viewModel.shapesListDetailResponse.observe(viewLifecycleOwner) {
            shapesAdapter?.addTypes(it.results)
        }
        viewModel.itemAttributeListDetailResponse.observe(viewLifecycleOwner) {
            attributeAdapter?.addTypes(it.results)
        }
        viewModel.itemCategoryListDetailResponse.observe(viewLifecycleOwner) {
            categoryAdapter?.addTypes(it.results)
        }
        viewModel.itemFlingEffectListDetailResponse.observe(viewLifecycleOwner) {
            flingEffectAdapter?.addTypes(it.results)
        }
        viewModel.itemPocketListDetailResponse.observe(viewLifecycleOwner) {
            pocketAdapter?.addTypes(it.results)
        }
        viewModel.berryFirmnessListDetailResponse.observe(viewLifecycleOwner) {
            berryFirmnessAdapter?.addTypes(it.results)
        }
        viewModel.berryFlavorListDetailResponse.observe(viewLifecycleOwner) {
            berryFlavorAdapter?.addTypes(it.results)
        }
        viewModel.moveAilmentListDetailResponse.observe(viewLifecycleOwner) {
            moveAilmentAdapter?.addTypes(it.results)
        }

        viewModel.moveCategoryListDetailResponse.observe(viewLifecycleOwner) {
            moveCategoryAdapter?.addTypes(it.results)
        }
        viewModel.moveDamageClassListDetailResponse.observe(viewLifecycleOwner) {
            moveDamageClassAdapter?.addTypes(it.results)
        }
        viewModel.moveTargetListDetailResponse.observe(viewLifecycleOwner) {
            moveTargetAdapter?.addTypes(it.results)
        }
    }

    private fun setAdapters() {
        typesAdapter = CommonCategoryAdapter()

        binding.idRvType.adapter = typesAdapter
        binding.idRvType.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)


        eggGroupAdapter = CommonCategoryAdapter()
        binding.idRvEggGroup.adapter = eggGroupAdapter
        binding.idRvEggGroup.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        gendersAdapter = CommonCategoryAdapter()
        binding.idRvGender.adapter = gendersAdapter
        binding.idRvGender.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        habitatsAdapter = CommonCategoryAdapter()
        binding.idRvHabitat.adapter = habitatsAdapter
        binding.idRvHabitat.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        shapesAdapter = CommonCategoryAdapter()
        binding.idRvShape.adapter = shapesAdapter
        binding.idRvShape.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        attributeAdapter = CommonCategoryAdapter()
        binding.idRvItemAttribute.adapter = attributeAdapter
        binding.idRvItemAttribute.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        categoryAdapter = CommonCategoryAdapter()
        binding.idRvItemCategory.adapter = categoryAdapter
        binding.idRvItemCategory.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        flingEffectAdapter = CommonCategoryAdapter()
        binding.idRvItemFlingEffect.adapter = flingEffectAdapter
        binding.idRvItemFlingEffect.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        pocketAdapter = CommonCategoryAdapter()
        binding.idRvItemPocket.adapter = pocketAdapter
        binding.idRvItemPocket.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        berryFirmnessAdapter = CommonCategoryAdapter()
        binding.idRvBerryFirmnesses.adapter = berryFirmnessAdapter
        binding.idRvBerryFirmnesses.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        berryFlavorAdapter = CommonCategoryAdapter()
        binding.idRvBerryFlavors.adapter = berryFlavorAdapter
        binding.idRvBerryFlavors.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        moveAilmentAdapter = CommonCategoryAdapter()
        binding.idRvMoveAilment.adapter = moveAilmentAdapter
        binding.idRvMoveAilment.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        moveCategoryAdapter = CommonCategoryAdapter()
        binding.idRvMoveCategory.adapter = moveCategoryAdapter
        binding.idRvMoveCategory.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        moveDamageClassAdapter = CommonCategoryAdapter()
        binding.idRvMoveDamageClass.adapter = moveDamageClassAdapter
        binding.idRvMoveDamageClass.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        moveTargetAdapter = CommonCategoryAdapter()
        binding.idRvMoveTarget.adapter = moveTargetAdapter
        binding.idRvMoveTarget.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)



        typesAdapter!!.setOnItemClickListenerUrlCommonCategory {
            println(it)
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToTypeDetailPokedexFragment(
                    it
                )
            )
        }

        eggGroupAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToEggGroupDetailPokedexFragment(
                    it
                )
            )
        }

        gendersAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToGenderDetailPokedexFragment(
                    it
                )
            )
        }

        habitatsAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToHabitatDetailPokedexFragment(
                    it
                )
            )
        }

        shapesAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToShapeDetailPokedexFragment(
                    it
                )
            )
        }

        attributeAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToItemAttributePokedexFragment(
                    it
                )
            )
        }

        categoryAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToItemCategoryPokedexFragment(
                    it
                )
            )
        }

        flingEffectAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToItemFlingEffectPokedexFragment(
                    it
                )
            )
        }

        pocketAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToItemPocketPokedexFragment(
                    it
                )
            )
        }

        berryFlavorAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToBerryFlavorDetailPokedexFragment(
                    it
                )
            )
        }

        berryFirmnessAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToBerryFirmnessDetailPokedexFragment(
                    it
                )
            )
        }

        moveAilmentAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToMoveAilmentPokedexFragment(
                    it
                )
            )
        }

        moveCategoryAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToMoveCategoryPokedexFragment(
                    it
                )
            )
        }

        moveDamageClassAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToMoveDamageClassPokedexFragment(
                    it
                )
            )
        }

        moveTargetAdapter!!.setOnItemClickListenerUrlCommonCategory {
            findNavController().navigate(
                CategoryPokedexFragmentDirections.actionCategoryPokedexFragmentToMoveTargetPokedexFragment(
                    it
                )
            )
        }


    }


    private fun setButtons() {
        binding.typeInfoButton.setOnClickListener {
            alertDialog(
                "type",
                "Types refer to different elemental properties associated with both Pokémon and their moves"
            )
        }

        binding.eggGroupInfoButton.setOnClickListener {
            alertDialog(
                "egg group",
                "Egg Groups are categories which determine which Pokémon are able to interbreed. Similar to types, a Pokémon may belong to either one or two Egg Groups"
            )
        }

        binding.genderInfoButton.setOnClickListener {
            alertDialog("gender", "Gender is a characteristic of Pokémon in the Pokémon world")
        }

        binding.habitatInfoButton.setOnClickListener {
            alertDialog("habitat", "List of Pokémon according to their habitats")
        }

        binding.shapeInfoButton.setOnClickListener {
            alertDialog("shape", "List of Pokémon by their shape")
        }

        binding.itemAttributeInfoButton.setOnClickListener {
            alertDialog(
                "attribute",
                "Item attributes define particular aspects of items, e.g. usable in battle or consumable"
            )
        }

        binding.itemCategoryInfoButton.setOnClickListener {
            alertDialog(
                "category",
                "Item categories determine where items will be placed in the players bag"
            )
        }

        binding.itemFlingEffectInfoButton.setOnClickListener {
            alertDialog(
                "fling effect",
                "The various effects of the move \"Fling\" when used with different items"
            )
        }

        binding.itemPocketInfoButton.setOnClickListener {
            alertDialog(
                "pocket",
                "Pockets within the players bag used for storing items by category"
            )
        }

        binding.berryFirmnessInfoButton.setOnClickListener {
            alertDialog(
                "flavor",
                "Flavors determine whether a Pokémon will benefit or suffer from eating a berry based on their nature"
            )
        }

        binding.berryFlavorInfoButton.setOnClickListener {
            alertDialog(
                "firmness", "Berries can be soft or hard"
            )
        }

        binding.moveAilmentButtonInfo.setOnClickListener {
            alertDialog(
                "ailment",
                "Move Ailments are status conditions caused by moves used during battle"
            )
        }

        binding.moveCategoryButtonInfo.setOnClickListener {
            alertDialog(
                "category",
                "Very general categories that loosely group move effects"
            )
        }

        binding.moveTargetButtonInfo.setOnClickListener {
            alertDialog(
                "target",
                "Targets moves can be directed at during battle. Targets can be Pokémon, environments or even other moves"
            )
        }


        binding.moveDamageClassButtonInfo.setOnClickListener {
            alertDialog(
                "damage class",
                "Damage classes moves can have, e.g. physical, special, or non-damaging"
            )
        }




        binding.categoryPokemon.setOnClickListener {
            currentCategory = "pokemon"
            downloadData()
        }

        binding.categoryItem.setOnClickListener {
            currentCategory = "item"
            downloadData()
        }

        binding.categoryBerries.setOnClickListener {
            currentCategory = "berry"
            downloadData()
        }

        binding.categoryMoves.setOnClickListener {
            currentCategory = "move"
            downloadData()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun alertDialog(title: String, description: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))
                .create()

        val customLayout = layoutInflater.inflate(R.layout.alert_dialog_categories_info, null)

        customLayout.findViewById<TextView>(R.id.title_info).text = title
        customLayout.findViewById<TextView>(R.id.description_info).text = description


        customLayout.findViewById<TextView>(R.id.close).setOnClickListener {
            builder.dismiss()
        }

        with(builder)
        {
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

        typesAdapter = null
        eggGroupAdapter = null
        gendersAdapter = null
        habitatsAdapter = null
        shapesAdapter = null
        attributeAdapter = null
        categoryAdapter = null
        flingEffectAdapter = null
        pocketAdapter = null
        berryFirmnessAdapter = null
        berryFlavorAdapter = null
        moveAilmentAdapter = null
        moveCategoryAdapter = null
        moveDamageClassAdapter = null
        moveTargetAdapter = null

        _binding = null
    }

}
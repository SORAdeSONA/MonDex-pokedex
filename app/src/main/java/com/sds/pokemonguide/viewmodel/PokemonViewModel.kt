package com.sds.pokemonguide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sds.pokemonguide.api.PokemonRepository
import com.sds.pokemonguide.model.AbilityDetailModel
import com.sds.pokemonguide.model.AttributeDetailModel
import com.sds.pokemonguide.model.BerriesFirmnessModel
import com.sds.pokemonguide.model.BerriesFlavorModel
import com.sds.pokemonguide.model.BerryDetailModel
import com.sds.pokemonguide.model.EggGroupDetailModel
import com.sds.pokemonguide.model.EvolutionChainModel
import com.sds.pokemonguide.model.GenderDetailModel
import com.sds.pokemonguide.model.HabitatDetailModel
import com.sds.pokemonguide.model.ItemAttributeDetailModel
import com.sds.pokemonguide.model.ItemCategoryDetailModel
import com.sds.pokemonguide.model.ItemDetailModel
import com.sds.pokemonguide.model.ItemFlingEffectDetailModel
import com.sds.pokemonguide.model.ItemPocketDetailModel
import com.sds.pokemonguide.model.MoveAilmentDetailModel
import com.sds.pokemonguide.model.MoveCommonDetailModel
import com.sds.pokemonguide.model.MoveDetailModel
import com.sds.pokemonguide.model.PokemonByTypeModel
import com.sds.pokemonguide.model.PokemonDetailModel
import com.sds.pokemonguide.model.PokemonSpeciesModel
import com.sds.pokemonguide.model.ShapeDetailModel
import com.sds.pokemonguide.model.TypesListDetailModel
import com.sds.pokemonguide.paging.BerriePagingSource
import com.sds.pokemonguide.paging.ItemPagingSource
import com.sds.pokemonguide.paging.MovePagingSource
import com.sds.pokemonguide.paging.PokemonPagingSource
import com.sds.pokemonguide.room.RoomBerryModel
import com.sds.pokemonguide.room.RoomCustomListNamesModel
import com.sds.pokemonguide.room.RoomItemModel
import com.sds.pokemonguide.room.RoomMoveModel
import com.sds.pokemonguide.room.RoomPokemonModel
import com.sds.pokemonguide.room.RoomRepository
import com.sds.pokemonguide.room.RoomSavedListsPokemonsModel
import com.sds.pokemonguide.status.LoadingStatus
import com.sds.pokemonguide.status.SearchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val pokemonRoomRepository: RoomRepository
) : ViewModel() {

    val loadingStatusMain = MutableLiveData<LoadingStatus?>()

    val loadingStatusDetail = MutableLiveData<LoadingStatus>()

    val loadingStatusDetailSpecial = MutableLiveData<LoadingStatus>()

    val loadingStatusBerryDetail = MutableLiveData<LoadingStatus>()

    val loadingStatusMove = MutableLiveData<LoadingStatus>()

    val loadingStatusItem = MutableLiveData<LoadingStatus>()

    val loadingStatusAttribute = MutableLiveData<LoadingStatus>()

    val loadingStatusAbility = MutableLiveData<LoadingStatus>()

    val loadingStatusCategory = MutableLiveData<LoadingStatus>()

    val loadingStatusBerryFlavor = MutableLiveData<LoadingStatus>()

    val loadingStatusBerryFirmness = MutableLiveData<LoadingStatus>()

    val loadingStatusPokemonType = MutableLiveData<LoadingStatus>()

    val loadingStatusGender = MutableLiveData<LoadingStatus>()

    val loadingStatusEggGroup = MutableLiveData<LoadingStatus>()

    val loadingStatusHabitat = MutableLiveData<LoadingStatus>()

    val loadingStatusShape = MutableLiveData<LoadingStatus>()

    val loadingStatusMoveAilment = MutableLiveData<LoadingStatus>()

    val loadingStatusMoveCategory = MutableLiveData<LoadingStatus>()

    val loadingStatusMoveDamageClass = MutableLiveData<LoadingStatus>()

    val loadingStatusMoveTarget = MutableLiveData<LoadingStatus>()

    val loadingStatusItemAttribute = MutableLiveData<LoadingStatus>()

    val loadingStatusItemCategory = MutableLiveData<LoadingStatus>()

    val loadingStatusItemFlingEffect = MutableLiveData<LoadingStatus>()

    val loadingStatusItemPocket = MutableLiveData<LoadingStatus>()

    val pokemonList = Pager(PagingConfig(pageSize = 20, initialLoadSize = 40)) {
        PokemonPagingSource(pokemonRepository, loadingStatusMain)
    }.flow.cachedIn(viewModelScope)

    val itemList = Pager(PagingConfig(pageSize = 20, initialLoadSize = 40)) {
        ItemPagingSource(pokemonRepository, loadingStatusMain)
    }.flow.cachedIn(viewModelScope)

    val moveList = Pager(PagingConfig(pageSize = 20, initialLoadSize = 40)) {
        MovePagingSource(pokemonRepository, loadingStatusMain)
    }.flow.cachedIn(viewModelScope)

    val berrieList = Pager(PagingConfig(pageSize = 20, initialLoadSize = 40)) {
        BerriePagingSource(pokemonRepository, loadingStatusMain)
    }.flow.cachedIn(viewModelScope)

    private val _pokemonDetailResponse = MutableLiveData<PokemonDetailModel>()
    val pokemonDetailResponse: LiveData<PokemonDetailModel> get() = _pokemonDetailResponse

    private val _pokemonSpeciesResponse = MutableLiveData<PokemonSpeciesModel>()
    val pokemonSpeciesResponse: LiveData<PokemonSpeciesModel> get() = _pokemonSpeciesResponse

    private val _pokemonEvolutionChainResponse = MutableLiveData<EvolutionChainModel>()
    val pokemonEvolutionChainResponse: LiveData<EvolutionChainModel> get() = _pokemonEvolutionChainResponse


    private val _moveDetailResponse = MutableLiveData<MoveDetailModel>()
    val moveDetailResponse: LiveData<MoveDetailModel> get() = _moveDetailResponse

    private val _itemDetailResponse = MutableLiveData<ItemDetailModel>()
    val itemDetailResponse: LiveData<ItemDetailModel> get() = _itemDetailResponse

    private val _berryDetailResponse = MutableLiveData<BerryDetailModel>()
    val berryDetailResponse: LiveData<BerryDetailModel> get() = _berryDetailResponse

    private val _attributeDetailResponse = MutableLiveData<AttributeDetailModel>()
    val attributeDetailResponse: LiveData<AttributeDetailModel> get() = _attributeDetailResponse

    private val _abilityDetailResponse = MutableLiveData<AbilityDetailModel>()
    val abilityDetailResponse: LiveData<AbilityDetailModel> get() = _abilityDetailResponse

    private val _typesListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val typesListDetailResponse: LiveData<TypesListDetailModel> get() = _typesListDetailResponse

    private val _eggGroupListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val eggGroupListDetailResponse: LiveData<TypesListDetailModel> get() = _eggGroupListDetailResponse

    private val _gendersListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val gendersListDetailResponse: LiveData<TypesListDetailModel> get() = _gendersListDetailResponse

    private val _habitatsListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val habitatsListDetailResponse: LiveData<TypesListDetailModel> get() = _habitatsListDetailResponse

    private val _shapesListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val shapesListDetailResponse: LiveData<TypesListDetailModel> get() = _shapesListDetailResponse

    private val _itemAttributeListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val itemAttributeListDetailResponse: LiveData<TypesListDetailModel> get() = _itemAttributeListDetailResponse

    private val _itemCategoryListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val itemCategoryListDetailResponse: LiveData<TypesListDetailModel> get() = _itemCategoryListDetailResponse

    private val _itemFlingEffectListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val itemFlingEffectListDetailResponse: LiveData<TypesListDetailModel> get() = _itemFlingEffectListDetailResponse

    private val _itemPocketListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val itemPocketListDetailResponse: LiveData<TypesListDetailModel> get() = _itemPocketListDetailResponse

    private val _berryFlavorListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val berryFlavorListDetailResponse: LiveData<TypesListDetailModel> get() = _berryFlavorListDetailResponse

    private val _berryFirmnessListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val berryFirmnessListDetailResponse: LiveData<TypesListDetailModel> get() = _berryFirmnessListDetailResponse

    private val _moveAilmentListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val moveAilmentListDetailResponse: LiveData<TypesListDetailModel> get() = _moveAilmentListDetailResponse

    private val _moveBattleStyleListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val moveBattleStyleListDetailResponse: LiveData<TypesListDetailModel> get() = _moveBattleStyleListDetailResponse

    private val _moveCategoryListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val moveCategoryListDetailResponse: LiveData<TypesListDetailModel> get() = _moveCategoryListDetailResponse

    private val _moveDamageClassListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val moveDamageClassListDetailResponse: LiveData<TypesListDetailModel> get() = _moveDamageClassListDetailResponse

    private val _moveTargetListDetailResponse = MutableLiveData<TypesListDetailModel>()
    val moveTargetListDetailResponse: LiveData<TypesListDetailModel> get() = _moveTargetListDetailResponse

    private val _berryFirmnessDetailResponse = MutableLiveData<BerriesFirmnessModel>()
    val berryFirmnessDetailResponse: LiveData<BerriesFirmnessModel> get() = _berryFirmnessDetailResponse

    private val _berryFlavorDetailResponse = MutableLiveData<BerriesFlavorModel>()
    val berryFlavorDetailResponse: LiveData<BerriesFlavorModel> get() = _berryFlavorDetailResponse

    private val _pokemonTypeDetailResponse = MutableLiveData<PokemonByTypeModel>()
    val pokemonTypeDetailResponse: LiveData<PokemonByTypeModel> get() = _pokemonTypeDetailResponse

    private val _genderDetailResponse = MutableLiveData<GenderDetailModel>()
    val genderDetailResponse: LiveData<GenderDetailModel> get() = _genderDetailResponse

    private val _eggGroupDetailResponse = MutableLiveData<EggGroupDetailModel>()
    val eggGroupDetailResponse: LiveData<EggGroupDetailModel> get() = _eggGroupDetailResponse

    private val _habitatDetailResponse = MutableLiveData<HabitatDetailModel>()
    val habitatDetailResponse: LiveData<HabitatDetailModel> get() = _habitatDetailResponse

    private val _shapeDetailResponse = MutableLiveData<ShapeDetailModel>()
    val shapeDetailResponse: LiveData<ShapeDetailModel> get() = _shapeDetailResponse

    private val _moveAilmentDetailResponse = MutableLiveData<MoveAilmentDetailModel>()
    val moveAilmentDetailResponse: LiveData<MoveAilmentDetailModel> get() = _moveAilmentDetailResponse

    private val _moveCategoryDetailResponse = MutableLiveData<MoveCommonDetailModel>()
    val moveCategoryDetailResponse: LiveData<MoveCommonDetailModel> get() = _moveCategoryDetailResponse

    private val _moveDamageClassDetailResponse = MutableLiveData<MoveCommonDetailModel>()
    val moveDamageClassDetailResponse: LiveData<MoveCommonDetailModel> get() = _moveDamageClassDetailResponse

    private val _moveTargetDetailResponse = MutableLiveData<MoveCommonDetailModel>()
    val moveTargetDetailResponse: LiveData<MoveCommonDetailModel> get() = _moveTargetDetailResponse

    private val _itemAttributeDetailResponse = MutableLiveData<ItemAttributeDetailModel>()
    val itemAttributeDetailResponse: LiveData<ItemAttributeDetailModel> get() = _itemAttributeDetailResponse

    private val _itemCategoryDetailResponse = MutableLiveData<ItemCategoryDetailModel>()
    val itemCategoryDetailResponse: LiveData<ItemCategoryDetailModel> get() = _itemCategoryDetailResponse

    private val _itemFlingEffectDetailResponse = MutableLiveData<ItemFlingEffectDetailModel>()
    val itemFlingEffectDetailResponse: LiveData<ItemFlingEffectDetailModel> get() = _itemFlingEffectDetailResponse

    private val _itemPocketDetailResponse = MutableLiveData<ItemPocketDetailModel>()
    val itemPocketDetailResponse: LiveData<ItemPocketDetailModel> get() = _itemPocketDetailResponse

    val searchType = MutableLiveData<SearchStatus>()

    suspend fun downloadPokemonDetails(pokemonUrl: String) {
        loadingStatusDetail.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getPokemonDetail(pokemonUrl)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusDetail.value = LoadingStatus.SUCCESS
                    _pokemonDetailResponse.value = response.body()
                } else {
                    loadingStatusDetail.value = LoadingStatus.ERROR
                }

            }
        }
    }

    suspend fun downloadPokemonSpecies(id: String) {
        loadingStatusDetailSpecial.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getPokemonSpecies(id)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusDetailSpecial.value = LoadingStatus.SUCCESS
                    _pokemonSpeciesResponse.value = response.body()
                } else {
                    loadingStatusDetailSpecial.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadPokemonEvolutionChain(id: String) : EvolutionChainModel {
            val response = pokemonRepository.getPokemonEvolutionChain(id)
            return response.body()!!
    }

    suspend fun downloadMoveDetailsById(id: String) {
        loadingStatusMove.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveDetailById(id)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusMove.value = LoadingStatus.SUCCESS
                    _moveDetailResponse.value = response.body()
                } else {
                    loadingStatusMove.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadItemDetailsById(id: String) {
        loadingStatusItem.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemDetailById(id)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusItem.value = LoadingStatus.SUCCESS
                    _itemDetailResponse.value = response.body()
                } else {
                    loadingStatusItem.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadBerryDetailsById(id: String) {
        loadingStatusBerryDetail.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getBerryDetailById(id)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusBerryDetail.value = LoadingStatus.SUCCESS
                    _berryDetailResponse.value = response.body()
                } else {
                    loadingStatusBerryDetail.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadAttributeDetailsById(id: String) {
        loadingStatusAttribute.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getAttributeDetailById(id)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusAttribute.value = LoadingStatus.SUCCESS
                    _attributeDetailResponse.value = response.body()
                } else {
                    loadingStatusAttribute.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadAbilityDetailsById(id: String) {
        loadingStatusAbility.value = LoadingStatus.LOADING

        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getAbilityDetailById(id)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusAbility.value = LoadingStatus.SUCCESS
                    _abilityDetailResponse.value = response.body()
                } else {
                    loadingStatusAbility.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun searchPokemon(name: String) {
        withContext(Dispatchers.IO) {
            val pokemonResponse = pokemonRepository.getPokemonByName(name)
            if (pokemonResponse.isSuccessful && pokemonResponse.code() != 404) {
                withContext(Dispatchers.Main) {
                    searchType.value = SearchStatus.POKEMON
                }
            } else println("nothing found in category POKEMON")
        }

    }

    suspend fun searchItem(name: String) {
        withContext(Dispatchers.IO) {
            val itemResponse = pokemonRepository.getItemDetailById(name)
            if (itemResponse.isSuccessful && itemResponse.code() != 404) {
                withContext(Dispatchers.Main) {
                    searchType.value = SearchStatus.ITEM
                }
            } else {
                println("nothing found in category ITEM")
            }
        }
    }

    suspend fun searchAbility(name: String) {
        withContext(Dispatchers.IO) {
            val abilityResponse = pokemonRepository.getAbilityDetailById(name)
            if (abilityResponse.isSuccessful && abilityResponse.code() != 404) {
                withContext(Dispatchers.Main) {
                    searchType.value = SearchStatus.ABILITY
                }
            } else {
                println("nothing found in category ABILITY")
            }
        }
    }

    suspend fun searchMove(name: String) {
        withContext(Dispatchers.IO) {
            val moveResponse = pokemonRepository.getMoveDetailById(name)
            if (moveResponse.isSuccessful && moveResponse.code() != 404) {
                withContext(Dispatchers.Main) {
                    searchType.value = SearchStatus.MOVE
                }
            } else {
                println("nothing found in category MOVE")
            }
        }
    }

    suspend fun downloadTypesList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getTypesList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _typesListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadEggGroupList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getEggGroupList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _eggGroupListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadGendersList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getGendersList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _gendersListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadHabitatsList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getHabitatsList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _habitatsListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadShapesList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getShapesList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _shapesListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadItemAttributeList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemAttributeList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _itemAttributeListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadItemCategoryList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemCategoryList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _itemCategoryListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadItemFlingEffectList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemFlingEffectList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _itemFlingEffectListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadItemPocketList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemPocketList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _itemPocketListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadBerryFlavorList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getBerryFlavorList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _berryFlavorListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadBerryFirmnessList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getBerryFirmnessList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _berryFirmnessListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadMoveAilmentList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveAilmentList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _moveAilmentListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadMoveBattleStyleList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveBattleStyleList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _moveBattleStyleListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadMoveCategoryList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveCategoryList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _moveCategoryListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadMoveDamageClassList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveDamageClassList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _moveDamageClassListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadMoveTargetList() {
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveTargetsList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _moveTargetListDetailResponse.value = response.body()
                }
            }
        }
    }

    suspend fun downloadBerryFirmnessDetail(url: String) {
        loadingStatusBerryFirmness.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getBerryFirmnessDetail(url)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    loadingStatusBerryFirmness.value = LoadingStatus.SUCCESS
                    _berryFirmnessDetailResponse.value = response.body()
                }

            } else {
                withContext(Dispatchers.Main) {
                    loadingStatusBerryFirmness.value = LoadingStatus.ERROR
                }
            }
        }

    }

    suspend fun downloadBerryFlavorDetail(url: String) {
        loadingStatusBerryFlavor.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getBerryFlavorDetail(url)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusBerryFlavor.value = LoadingStatus.SUCCESS
                    _berryFlavorDetailResponse.value = response.body()
                } else {
                    loadingStatusBerryFlavor.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadPokemonTypeDetail(id: String) {
        loadingStatusPokemonType.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getTypeDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusPokemonType.value = LoadingStatus.SUCCESS
                    _pokemonTypeDetailResponse.value = response.body()
                } else {
                    loadingStatusPokemonType.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadPokemonGender(id: String) {
        loadingStatusGender.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getGenderDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusGender.value = LoadingStatus.SUCCESS
                    _genderDetailResponse.value = response.body()
                } else {
                    loadingStatusGender.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadEggGroup(id: String) {
        loadingStatusEggGroup.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getEggGroupDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusEggGroup.value = LoadingStatus.SUCCESS
                    _eggGroupDetailResponse.value = response.body()
                } else {
                    loadingStatusEggGroup.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadHabitat(id: String) {
        loadingStatusHabitat.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getHabitatDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusHabitat.value = LoadingStatus.SUCCESS
                    _habitatDetailResponse.value = response.body()
                } else {
                    loadingStatusHabitat.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadShape(id: String) {
        loadingStatusShape.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getShapeDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusShape.value = LoadingStatus.SUCCESS
                    _shapeDetailResponse.value = response.body()
                } else {
                    loadingStatusShape.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadMoveAilment(id: String) {
        loadingStatusMoveAilment.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveAilmentDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusMoveAilment.value = LoadingStatus.SUCCESS
                    _moveAilmentDetailResponse.value = response.body()
                } else {
                    loadingStatusMoveAilment.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadMoveCategory(id: String) {
        loadingStatusMoveCategory.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveCategoryDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusMoveCategory.value = LoadingStatus.SUCCESS
                    _moveCategoryDetailResponse.value = response.body()
                } else {
                    loadingStatusMoveCategory.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadMoveDamageClass(id: String) {
        loadingStatusMoveDamageClass.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveDamageClassDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusMoveDamageClass.value = LoadingStatus.SUCCESS
                    _moveDamageClassDetailResponse.value = response.body()
                } else {
                    loadingStatusMoveDamageClass.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadMoveTarget(id: String) {
        loadingStatusMoveTarget.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getMoveTargetDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusMoveTarget.value = LoadingStatus.SUCCESS
                    _moveTargetDetailResponse.value = response.body()
                } else {
                    loadingStatusMoveTarget.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadItemAttribute(id: String) {
        loadingStatusItemAttribute.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemAttributeDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusItemAttribute.value = LoadingStatus.SUCCESS
                    _itemAttributeDetailResponse.value = response.body()
                } else {
                    loadingStatusItemAttribute.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadItemCategory(id: String) {
        loadingStatusItemCategory.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemCategoryDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusItemCategory.value = LoadingStatus.SUCCESS
                    _itemCategoryDetailResponse.value = response.body()
                } else {
                    loadingStatusItemCategory.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadItemFlingEffect(id: String) {
        loadingStatusItemFlingEffect.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemFlingEffectDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusItemFlingEffect.value = LoadingStatus.SUCCESS
                    _itemFlingEffectDetailResponse.value = response.body()
                } else {
                    loadingStatusItemFlingEffect.value = LoadingStatus.ERROR
                }
            }
        }
    }

    suspend fun downloadItemPocket(id: String) {
        loadingStatusItemPocket.value = LoadingStatus.LOADING
        withContext(Dispatchers.IO) {
            val response = pokemonRepository.getItemPocketDetailById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loadingStatusItemPocket.value = LoadingStatus.SUCCESS
                    _itemPocketDetailResponse.value = response.body()
                } else {
                    loadingStatusItemPocket.value = LoadingStatus.ERROR
                }
            }
        }
    }

    fun getAllPokemonsFromDb(): List<RoomPokemonModel> {
        return pokemonRoomRepository.getAllPokemons()
    }

    fun addPokemonToDb(pokemonName: String, pokemonUrl: String) {
        pokemonRoomRepository.addPokemon(RoomPokemonModel(pokemonName, pokemonUrl))
    }

    fun deletePokemonFromDb(pokemonName: String) {
        pokemonRoomRepository.deletePokemon(pokemonName)
    }

    fun getAllItemsFromDb(): List<RoomItemModel> {
        return pokemonRoomRepository.getAllItems()
    }

    fun addItemToDb(dbId: Int, itemName: String, itemUrl: String) {
        pokemonRoomRepository.addItem(RoomItemModel(dbId, itemName, itemUrl))
    }

    fun deleteItemFromDb(itemName: String) {
        pokemonRoomRepository.deleteItem(itemName)
    }

    fun getAllBerriesFromDb(): List<RoomBerryModel> {
        return pokemonRoomRepository.getAllBerries()
    }

    fun addBerryToDb(dbId: Int, berryName: String, berryUrl: String) {
        pokemonRoomRepository.addBerry(RoomBerryModel(dbId, berryName, berryUrl))
    }

    fun deleteBerryFromDb(berryName: String) {
        pokemonRoomRepository.deleteBerry(berryName)
    }

    fun getAllMovesFromDb(): List<RoomMoveModel> {
        return pokemonRoomRepository.getAllMoves()
    }

    fun addMoveToDb(dbId: Int, moveName: String, moveUrl: String) {
        pokemonRoomRepository.addMove(RoomMoveModel(dbId, moveName, moveUrl))
    }

    fun deleteMoveFromDb(moveName: String) {
        pokemonRoomRepository.deleteMove(moveName)
    }

    suspend fun getAllLists(): List<RoomCustomListNamesModel> {
        return pokemonRoomRepository.getAllCategories()

    }

    fun addListToDb(dbId: Int, listName: String, listDescription: String) {
        pokemonRoomRepository.addCategory(RoomCustomListNamesModel(dbId, listName, listDescription))
    }

    fun deleteListFromDb(listName: String) {
        pokemonRoomRepository.deleteCategory(listName)
    }

    fun updateListNameAndDescription(
        oldCategoryName: String,
        newCategoryName: String,
        oldCategoryDescription: String,
        newCategoryDescription: String
    ) {
        pokemonRoomRepository.updateCategoryNameAndDescription(
            oldCategoryName,
            newCategoryName,
            oldCategoryDescription,
            newCategoryDescription
        )
    }

    fun getAllSavedListsPokemons(): List<RoomSavedListsPokemonsModel> {
        return pokemonRoomRepository.getAllSavedListsPokemons()
    }

    fun addPokemonToSavedLists(pokemonName: String, pokemonUrl: String, listName: String) {
        pokemonRoomRepository.addPokemonToSavedLists(
            RoomSavedListsPokemonsModel(
                pokemonName,
                pokemonUrl,
                listName
            )
        )
    }

    fun deletePokemonFromSavedLists(pokemonName: String, listName: String) {
        pokemonRoomRepository.deletePokemonFromSavedLists(pokemonName, listName)
    }

    fun deletePokemonWhenDeleteList(listName: String) {
        pokemonRoomRepository.deletePokemonWhenDeleteList(listName)
    }

    fun updateListNameForPokemons(oldListName: String, newListName: String) {
        pokemonRoomRepository.updateListNameForPokemons(oldListName, newListName)
    }

    override fun onCleared() {
        super.onCleared()
        loadingStatusMain.value = null
        viewModelScope.coroutineContext.cancel()
    }

}
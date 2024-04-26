package com.sds.pokemonguide.api

import com.sds.pokemonguide.model.AbilityDetailModel
import com.sds.pokemonguide.model.AttributeDetailModel
import com.sds.pokemonguide.model.BerriesFirmnessModel
import com.sds.pokemonguide.model.BerriesFlavorModel
import com.sds.pokemonguide.model.BerryDetailModel
import com.sds.pokemonguide.model.CommonListModel
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
import retrofit2.Response
import javax.inject.Inject

class PokemonRepository @Inject constructor(private val apiService: ApiService) :
    PokemonRepositoryInterface {

    override suspend fun getPokemonList(pokemonOffset: Int): Response<CommonListModel> {
        return apiService.getPokemonList(pokemonOffset)
    }

    override suspend fun getItemList(itemOffset: Int): Response<CommonListModel> {
        return apiService.getItemList(itemOffset)
    }

    override suspend fun getMoveList(itemOffset: Int): Response<CommonListModel> {
        return apiService.getMoveList(itemOffset)
    }

    override suspend fun getBerryList(itemOffset: Int): Response<CommonListModel> {
        return apiService.getBerryList(itemOffset)
    }

    override suspend fun getPokemonDetail(id: String): Response<PokemonDetailModel> {
        return apiService.getPokemonDetail(id)
    }

    override suspend fun getPokemonSpecies(id: String): Response<PokemonSpeciesModel> {
        return apiService.getPokemonSpecies(id)
    }

    override suspend fun getPokemonEvolutionChain(id: String): Response<EvolutionChainModel> {
        return apiService.getPokemonEvolutionChain(id)
    }

    override suspend fun getPokemonByName(name: String): Response<PokemonDetailModel> {
        return apiService.getPokemonByName(name)
    }

    override suspend fun getMoveDetailById(id: String): Response<MoveDetailModel> {
        return apiService.getMoveById(id)
    }

    override suspend fun getItemDetailById(id: String): Response<ItemDetailModel> {
        return apiService.getItemById(id)
    }

    override suspend fun getBerryDetailById(id: String): Response<BerryDetailModel> {
        return apiService.getBerryById(id)
    }

    override suspend fun getAttributeDetailById(id: String): Response<AttributeDetailModel> {
        return apiService.getAttributeById(id)
    }

    override suspend fun getAbilityDetailById(id: String): Response<AbilityDetailModel> {
        return apiService.getAbilityById(id)
    }

    override suspend fun getTypesList(): Response<TypesListDetailModel> {
        return apiService.getTypesList()
    }

    override suspend fun getEggGroupList(): Response<TypesListDetailModel> {
        return apiService.getEggGroupList()
    }

    override suspend fun getGendersList(): Response<TypesListDetailModel> {
        return apiService.getGendersList()
    }

    override suspend fun getHabitatsList(): Response<TypesListDetailModel> {
        return apiService.getHabitatsList()
    }

    override suspend fun getShapesList(): Response<TypesListDetailModel> {
        return apiService.getShapesList()
    }

    override suspend fun getItemAttributeList(): Response<TypesListDetailModel> {
        return apiService.getItemAttributeList()
    }

    override suspend fun getItemCategoryList(): Response<TypesListDetailModel> {
        return apiService.getItemCategoryList()
    }

    override suspend fun getItemFlingEffectList(): Response<TypesListDetailModel> {
        return apiService.getItemFlingEffectList()
    }

    override suspend fun getItemPocketList(): Response<TypesListDetailModel> {
        return apiService.getItemPocketList()
    }

    override suspend fun getBerryFirmnessList(): Response<TypesListDetailModel> {
        return apiService.getBerryFirmnessList()
    }

    override suspend fun getBerryFlavorList(): Response<TypesListDetailModel> {
        return apiService.getBerryFlavorList()
    }

    override suspend fun getMoveAilmentList(): Response<TypesListDetailModel> {
        return apiService.getMoveAilmentList()
    }

    override suspend fun getMoveBattleStyleList(): Response<TypesListDetailModel> {
        return apiService.getMoveBattleStyleList()
    }

    override suspend fun getMoveCategoryList(): Response<TypesListDetailModel> {
        return apiService.getMoveCategoryStyleList()
    }

    override suspend fun getMoveDamageClassList(): Response<TypesListDetailModel> {
        return apiService.getMoveDamageClassList()
    }

    override suspend fun getMoveTargetsList(): Response<TypesListDetailModel> {
        return apiService.getMoveTargetList()
    }

    override suspend fun getBerryFirmnessDetail(url: String): Response<BerriesFirmnessModel> {
        return apiService.getBerryFirmnessDetail(url)
    }

    override suspend fun getBerryFlavorDetail(url: String): Response<BerriesFlavorModel> {
        return apiService.getBerryFlavorDetail(url)
    }

    override suspend fun getTypeDetailById(id: String): Response<PokemonByTypeModel> {
        return apiService.getTypeDetailById(id)
    }

    override suspend fun getGenderDetailById(id: String): Response<GenderDetailModel> {
        return apiService.getGenderDetailById(id)
    }

    override suspend fun getEggGroupDetailById(id: String): Response<EggGroupDetailModel> {
        return apiService.getEggGroupIdDetailById(id)
    }

    override suspend fun getHabitatDetailById(id: String): Response<HabitatDetailModel> {
        return apiService.getHabitatDetailById(id)
    }

    override suspend fun getShapeDetailById(id: String): Response<ShapeDetailModel> {
        return apiService.getShapeDetailById(id)
    }

    override suspend fun getMoveAilmentDetailById(id: String): Response<MoveAilmentDetailModel> {
        return apiService.getMoveAilmentDetailById(id)
    }

    override suspend fun getMoveCategoryDetailById(id: String): Response<MoveCommonDetailModel> {
        return apiService.getMoveCategoryDetailById(id)
    }

    override suspend fun getMoveDamageClassDetailById(id: String): Response<MoveCommonDetailModel> {
        return apiService.getMoveDamageClassDetailById(id)
    }

    override suspend fun getMoveTargetDetailById(id: String): Response<MoveCommonDetailModel> {
        return apiService.getMoveTargetDetailById(id)
    }

    override suspend fun getItemAttributeDetailById(id: String): Response<ItemAttributeDetailModel> {
        return apiService.getItemAttributeDetailById(id)
    }

    override suspend fun getItemCategoryDetailById(id: String): Response<ItemCategoryDetailModel> {
        return apiService.getItemCategoryDetailById(id)
    }

    override suspend fun getItemFlingEffectDetailById(id: String): Response<ItemFlingEffectDetailModel> {
        return apiService.getItemFlingEffectDetailById(id)
    }

    override suspend fun getItemPocketDetailById(id: String): Response<ItemPocketDetailModel> {
        return apiService.getItemPocketDetailById(id)
    }


}
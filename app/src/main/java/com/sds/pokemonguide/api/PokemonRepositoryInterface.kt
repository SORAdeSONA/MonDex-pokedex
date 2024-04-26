package com.sds.pokemonguide.api

import com.sds.pokemonguide.model.AbilityDetailModel
import com.sds.pokemonguide.model.AttributeDetailModel
import com.sds.pokemonguide.model.BerriesFirmnessModel
import com.sds.pokemonguide.model.BerriesFlavorModel
import com.sds.pokemonguide.model.BerryDetailModel
import com.sds.pokemonguide.model.ItemDetailModel
import com.sds.pokemonguide.model.MoveDetailModel
import com.sds.pokemonguide.model.PokemonDetailModel
import com.sds.pokemonguide.model.CommonListModel
import com.sds.pokemonguide.model.EggGroupDetailModel
import com.sds.pokemonguide.model.EvolutionChainModel
import com.sds.pokemonguide.model.GenderDetailModel
import com.sds.pokemonguide.model.HabitatDetailModel
import com.sds.pokemonguide.model.ItemAttributeDetailModel
import com.sds.pokemonguide.model.ItemCategoryDetailModel
import com.sds.pokemonguide.model.ItemFlingEffectDetailModel
import com.sds.pokemonguide.model.ItemPocketDetailModel
import com.sds.pokemonguide.model.MoveAilmentDetailModel
import com.sds.pokemonguide.model.MoveCommonDetailModel
import com.sds.pokemonguide.model.PokemonByTypeModel
import com.sds.pokemonguide.model.PokemonSpeciesModel
import com.sds.pokemonguide.model.ShapeDetailModel
import com.sds.pokemonguide.model.TypesListDetailModel
import retrofit2.Response

interface PokemonRepositoryInterface {
    suspend fun getPokemonList(pokemonOffset: Int): Response<CommonListModel>
    suspend fun getItemList(itemOffset: Int): Response<CommonListModel>
    suspend fun getMoveList(itemOffset: Int): Response<CommonListModel>
    suspend fun getBerryList(itemOffset: Int): Response<CommonListModel>
    suspend fun getPokemonDetail(url:String):Response<PokemonDetailModel>
    suspend fun getPokemonSpecies(id:String):Response<PokemonSpeciesModel>
    suspend fun getPokemonEvolutionChain(id:String):Response<EvolutionChainModel>
    suspend fun getPokemonByName(name:String):Response<PokemonDetailModel>
    suspend fun getMoveDetailById(id: String): Response<MoveDetailModel>
    suspend fun getItemDetailById(id: String): Response<ItemDetailModel>
    suspend fun getBerryDetailById(id: String): Response<BerryDetailModel>
    suspend fun getAttributeDetailById(id: String): Response<AttributeDetailModel>
    suspend fun getAbilityDetailById(id: String): Response<AbilityDetailModel>
    suspend fun getTypesList(): Response<TypesListDetailModel>
    suspend fun getEggGroupList(): Response<TypesListDetailModel>
    suspend fun getGendersList(): Response<TypesListDetailModel>
    suspend fun getHabitatsList(): Response<TypesListDetailModel>
    suspend fun getShapesList(): Response<TypesListDetailModel>
    suspend fun getItemAttributeList(): Response<TypesListDetailModel>
    suspend fun getItemCategoryList(): Response<TypesListDetailModel>
    suspend fun getItemFlingEffectList(): Response<TypesListDetailModel>
    suspend fun getItemPocketList(): Response<TypesListDetailModel>
    suspend fun getBerryFirmnessList(): Response<TypesListDetailModel>
    suspend fun getBerryFlavorList(): Response<TypesListDetailModel>
    suspend fun getMoveAilmentList(): Response<TypesListDetailModel>
    suspend fun getMoveBattleStyleList(): Response<TypesListDetailModel>
    suspend fun getMoveCategoryList(): Response<TypesListDetailModel>
    suspend fun getMoveDamageClassList(): Response<TypesListDetailModel>
    suspend fun getMoveTargetsList(): Response<TypesListDetailModel>
    suspend fun getBerryFirmnessDetail(url:String): Response<BerriesFirmnessModel>
    suspend fun getBerryFlavorDetail(url:String): Response<BerriesFlavorModel>
    suspend fun getTypeDetailById(id:String): Response<PokemonByTypeModel>
    suspend fun getGenderDetailById(id:String): Response<GenderDetailModel>
    suspend fun getEggGroupDetailById(id:String): Response<EggGroupDetailModel>
    suspend fun getHabitatDetailById(id:String): Response<HabitatDetailModel>
    suspend fun getShapeDetailById(id:String): Response<ShapeDetailModel>
    suspend fun getMoveAilmentDetailById(id:String): Response<MoveAilmentDetailModel>
    suspend fun getMoveCategoryDetailById(id:String): Response<MoveCommonDetailModel>
    suspend fun getMoveDamageClassDetailById(id:String): Response<MoveCommonDetailModel>
    suspend fun getMoveTargetDetailById(id:String): Response<MoveCommonDetailModel>
    suspend fun getItemAttributeDetailById(id:String): Response<ItemAttributeDetailModel>
    suspend fun getItemCategoryDetailById(id:String): Response<ItemCategoryDetailModel>
    suspend fun getItemFlingEffectDetailById(id:String): Response<ItemFlingEffectDetailModel>
    suspend fun getItemPocketDetailById(id:String): Response<ItemPocketDetailModel>
}
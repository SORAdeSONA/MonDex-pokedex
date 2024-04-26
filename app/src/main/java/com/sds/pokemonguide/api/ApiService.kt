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
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("pokemon?limit=20")
    suspend fun getPokemonList(
        @Query("offset") offset: Int
    ): Response<CommonListModel>

    @GET("item?limit=20")
    suspend fun getItemList(
        @Query("offset") offset: Int
    ): Response<CommonListModel>

    @GET("move?limit=20")
    suspend fun getMoveList(
        @Query("offset") offset: Int
    ): Response<CommonListModel>

    @GET("berry?limit=20")
    suspend fun getBerryList(
        @Query("offset") offset: Int
    ): Response<CommonListModel>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: String
    ): Response<PokemonDetailModel>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: String
    ): Response<PokemonSpeciesModel>

    @GET("evolution-chain/{id}")
    suspend fun getPokemonEvolutionChain(
        @Path("id") id: String
    ): Response<EvolutionChainModel>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(
        @Path("name") name: String
    ): Response<PokemonDetailModel>

    @GET("move/{moveId}")
    suspend fun getMoveById(
        @Path("moveId") moveId: String
    ): Response<MoveDetailModel>

    @GET("item/{itemId}")
    suspend fun getItemById(
        @Path("itemId") itemId: String
    ): Response<ItemDetailModel>

    @GET("item-attribute/{attributeId}")
    suspend fun getAttributeById(
        @Path("attributeId") attributeId: String
    ): Response<AttributeDetailModel>

    @GET("ability/{abilityId}")
    suspend fun getAbilityById(
        @Path("abilityId") abilityId: String
    ): Response<AbilityDetailModel>

    @GET("berry/{berryId}")
    suspend fun getBerryById(
        @Path("berryId") berryId: String
    ): Response<BerryDetailModel>

    @GET("type")
    suspend fun getTypesList(): Response<TypesListDetailModel>

    @GET("egg-group")
    suspend fun getEggGroupList(): Response<TypesListDetailModel>

    @GET("gender")
    suspend fun getGendersList(): Response<TypesListDetailModel>

    @GET("pokemon-habitat")
    suspend fun getHabitatsList(): Response<TypesListDetailModel>

    @GET("pokemon-shape")
    suspend fun getShapesList(): Response<TypesListDetailModel>

    @GET("item-attribute")
    suspend fun getItemAttributeList(): Response<TypesListDetailModel>

    @GET("item-category")
    suspend fun getItemCategoryList(): Response<TypesListDetailModel>

    @GET("item-fling-effect")
    suspend fun getItemFlingEffectList(): Response<TypesListDetailModel>

    @GET("item-pocket")
    suspend fun getItemPocketList(): Response<TypesListDetailModel>

    @GET("berry-firmness")
    suspend fun getBerryFirmnessList(): Response<TypesListDetailModel>

    @GET("berry-flavor")
    suspend fun getBerryFlavorList(): Response<TypesListDetailModel>

    @GET("move-ailment")
    suspend fun getMoveAilmentList(): Response<TypesListDetailModel>

    @GET("move-battle-style")
    suspend fun getMoveBattleStyleList(): Response<TypesListDetailModel>

    @GET("move-category")
    suspend fun getMoveCategoryStyleList(): Response<TypesListDetailModel>

    @GET("move-damage-class")
    suspend fun getMoveDamageClassList(): Response<TypesListDetailModel>

    @GET("move-target")
    suspend fun getMoveTargetList(): Response<TypesListDetailModel>

    @GET suspend fun getBerryFirmnessDetail(
        @Url url: String,
    ): Response<BerriesFirmnessModel>

    @GET suspend fun getBerryFlavorDetail(
        @Url url: String,
    ): Response<BerriesFlavorModel>

    @GET("type/{typeId}")
    suspend fun getTypeDetailById(
        @Path("typeId") typeId: String
    ): Response<PokemonByTypeModel>

    @GET("gender/{genderId}")
    suspend fun getGenderDetailById(
        @Path("genderId") genderId: String
    ): Response<GenderDetailModel>

    @GET("egg-group/{eggGroupId}")
    suspend fun getEggGroupIdDetailById(
        @Path("eggGroupId") eggGroupId: String
    ): Response<EggGroupDetailModel>

    @GET("pokemon-habitat/{habitatId}")
    suspend fun getHabitatDetailById(
        @Path("habitatId") habitatId: String
    ): Response<HabitatDetailModel>

    @GET("pokemon-shape/{shapeId}")
    suspend fun getShapeDetailById(
        @Path("shapeId") shapeId: String
    ): Response<ShapeDetailModel>

    @GET("move-ailment/{moveAilmentId}")
    suspend fun getMoveAilmentDetailById(
        @Path("moveAilmentId") moveAilmentId: String
    ): Response<MoveAilmentDetailModel>

    @GET("move-category/{moveCategoryId}")
    suspend fun getMoveCategoryDetailById(
        @Path("moveCategoryId") moveCategoryId: String
    ): Response<MoveCommonDetailModel>

    @GET("move-damage-class/{moveDamageClassId}")
    suspend fun getMoveDamageClassDetailById(
        @Path("moveDamageClassId") moveDamageClassId: String
    ): Response<MoveCommonDetailModel>

    @GET("move-target/{moveTargetId}")
    suspend fun getMoveTargetDetailById(
        @Path("moveTargetId") moveTargetId: String
    ): Response<MoveCommonDetailModel>

    @GET("item-attribute/{itemAttributeId}")
    suspend fun getItemAttributeDetailById(
        @Path("itemAttributeId") itemAttributeId: String
    ): Response<ItemAttributeDetailModel>

    @GET("item-category/{itemCategoryId}")
    suspend fun getItemCategoryDetailById(
        @Path("itemCategoryId") itemCategoryId: String
    ): Response<ItemCategoryDetailModel>

    @GET("item-fling-effect/{itemFlingEffectId}")
    suspend fun getItemFlingEffectDetailById(
        @Path("itemFlingEffectId") itemFlingEffectId: String
    ): Response<ItemFlingEffectDetailModel>

    @GET("item-pocket/{itemPocketId}")
    suspend fun getItemPocketDetailById(
        @Path("itemPocketId") itemPocketId: String
    ): Response<ItemPocketDetailModel>

}
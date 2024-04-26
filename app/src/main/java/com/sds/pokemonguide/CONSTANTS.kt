package com.sds.pokemonguide

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.CoroutineExceptionHandler

object CONSTANTS {

    var BASE_POKEMON_PREFIX = "https://pokeapi.co/api/v2/pokemon/"


    fun getPokemonSpritesById(id: Any): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }

    fun getItemSpritesByName(itemName: String?): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/$itemName.png"
    }

    fun getBerrySpritesByName(berryName: String?): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/$berryName-berry.png"
    }


    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

}


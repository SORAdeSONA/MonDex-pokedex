package com.sds.pokemonguide.di

import android.content.Context
import androidx.room.Room
import com.sds.pokemonguide.room.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDi {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            PokemonDatabase::class.java,
            "pokedex_db"
        ).build()

    @Singleton
    @Provides
    fun providePokemonDao(db: PokemonDatabase) = db.pokemonDao()

    @Singleton
    @Provides
    fun provideItemDao(db: PokemonDatabase) = db.itemDao()

    @Singleton
    @Provides
    fun provideBerryDao(db: PokemonDatabase) = db.berryDao()

    @Singleton
    @Provides
    fun provideMoveDao(db: PokemonDatabase) = db.moveDao()

    @Singleton
    @Provides
    fun provideCustomListNamesDao(db: PokemonDatabase) = db.customListNamesDao()

    @Singleton
    @Provides
    fun provideSavedListsPokemonsDao(db: PokemonDatabase) = db.savedListsPokemonsDao()
}
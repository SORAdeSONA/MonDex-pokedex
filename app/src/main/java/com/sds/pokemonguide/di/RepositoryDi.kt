package com.sds.pokemonguide.di

import com.sds.pokemonguide.api.ApiService
import com.sds.pokemonguide.api.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryDi {

    @Provides
    fun provideDataRepository(apiService: ApiService): PokemonRepository {
        return PokemonRepository(apiService)
    }
}
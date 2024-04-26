package com.sds.pokemonguide.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sds.pokemonguide.api.PokemonRepository
import com.sds.pokemonguide.model.Result
import com.sds.pokemonguide.status.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class BerriePagingSource(
    private val repository: PokemonRepository,
    private val loadingStatusMain: MutableLiveData<LoadingStatus?>,
) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val currentPage = params.key ?: 0

            if (currentPage == 0){
                loadingStatusMain.value = LoadingStatus.LOADING
            }else loadingStatusMain.value = LoadingStatus.SUCCESS

            val response = withContext(Dispatchers.IO) {
                repository.getBerryList(currentPage)
            }

            if (response.isSuccessful) {
                val responseData = mutableListOf<Result>()
                val data = response.body()?.results ?: emptyList()
                responseData.addAll(data)

                LoadResult.Page(
                    data = responseData,
                    prevKey = if (currentPage == 0) null else -20,
                    nextKey = if (currentPage == 80) null else currentPage.plus(20)
                )
            } else {
                loadingStatusMain.value = LoadingStatus.ERROR
                LoadResult.Error(IOException("Failed to fetch data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }
}
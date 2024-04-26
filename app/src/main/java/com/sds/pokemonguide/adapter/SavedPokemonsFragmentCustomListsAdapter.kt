package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.CallbackList
import com.sds.pokemonguide.model.CallbackListSavedPokemons
import com.sds.pokemonguide.room.RoomCustomListNamesModel
import com.sds.pokemonguide.room.RoomSavedListsPokemonsModel
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import com.sds.pokemonguide.viewmodel.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedPokemonsFragmentCustomListsAdapter(val viewModel: PokemonViewModel) : RecyclerView.Adapter<SavedPokemonsFragmentCustomListsAdapter.DataViewHolder>() {

    private var lists: ArrayList<RoomCustomListNamesModel> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_list_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val listName = lists[position].name
        val listDescription = lists[position].description



        holder.itemView.findViewById<TextView>(R.id.list_name).text = listName
        holder.itemView.findViewById<TextView>(R.id.list_description).text = listDescription

        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val allPokemonsFromCustomLists = viewModel.getAllSavedListsPokemons()

                val listOfPokemons = mutableListOf<RoomSavedListsPokemonsModel>()

                for (value in allPokemonsFromCustomLists){
                    if (value.listName == listName) {
                        listOfPokemons.add(value)
                    }
                }

                withContext(Dispatchers.Main){
                    holder.itemView.findViewById<TextView>(R.id.list_items_count).text = listOfPokemons.size.toString()
                }

            }
        }


        holder.itemView.setOnClickListener {
            onItemClickListenerListName?.let {
                it(CallbackListSavedPokemons(listName = listName, listDescription = listDescription))
            }
        }
    }

    private var onItemClickListenerListName: ((CallbackListSavedPokemons) -> Unit)? = null

    fun setOnItemClickListenerListName(listener: (CallbackListSavedPokemons) -> Unit) {
        onItemClickListenerListName = listener
    }

    fun addLists(listsFromDb: List<RoomCustomListNamesModel>) {
        this.lists.apply {
            clear()
            addAll(listsFromDb)
            notifyDataSetChanged()
        }
    }



}
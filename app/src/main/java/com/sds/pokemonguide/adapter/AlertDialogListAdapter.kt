package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.CallbackListDetailFragmentAlertDialog
import com.sds.pokemonguide.model.CallbackListSavedPokemons
import com.sds.pokemonguide.room.RoomCustomListNamesModel
import com.sds.pokemonguide.room.RoomSavedListsPokemonsModel
import com.sds.pokemonguide.viewmodel.PokemonViewModel
import com.sds.pokemonguide.viewmodel.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlertDialogListAdapter(val viewModel: PokemonViewModel) : RecyclerView.Adapter<AlertDialogListAdapter.DataViewHolder>() {

    private var listNames: ArrayList<RoomCustomListNamesModel> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_list_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = listNames.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyListName = listNames[position].name
        val everyListDescription = listNames[position].description

        holder.itemView.findViewById<TextView>(R.id.list_name).text = everyListName
        holder.itemView.findViewById<TextView>(R.id.list_description).text = everyListDescription


        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val allPokemonsFromCustomLists = viewModel.getAllSavedListsPokemons()

                val listOfPokemons = mutableListOf<RoomSavedListsPokemonsModel>()

                for (value in allPokemonsFromCustomLists){
                    if (value.listName == everyListName) {
                        listOfPokemons.add(value)
                    }
                }

                withContext(Dispatchers.Main){
                    holder.itemView.findViewById<TextView>(R.id.list_items_count).text = listOfPokemons.size.toString()
                }

            }

        }

        holder.itemView.setOnClickListener {
            onItemClickListenerName?.let {
                it(CallbackListDetailFragmentAlertDialog(everyListName, position))
            }
        }

    }


    private var onItemClickListenerName: ((CallbackListDetailFragmentAlertDialog) -> Unit)? = null

    fun setOnItemClickListenerName(listener: (CallbackListDetailFragmentAlertDialog) -> Unit) {
        onItemClickListenerName = listener
    }

    fun addLists(listNamesFromDb: List<RoomCustomListNamesModel>) {
        this.listNames.apply {
            clear()
            addAll(listNamesFromDb)
            notifyDataSetChanged()
        }
    }

    fun updateList(position: Int, updatedItem: RoomCustomListNamesModel) {
        if (position in 0 until listNames.size) {
            listNames[position] = updatedItem
            notifyItemChanged(position)
        }
    }

}